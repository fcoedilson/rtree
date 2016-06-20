package br.edu.fa7.rtree.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.fa7.rtree.entity.Ponto;
import br.edu.fa7.rtree.util.Visitor;
import spatialindex.rtree.RTree;
import spatialindex.spatialindex.Point;
import spatialindex.spatialindex.Region;
import spatialindex.spatialindex.SpatialIndex;
import spatialindex.storagemanager.PropertySet;

public class Rtree {
	
	// identificando o ponto de referencia com prioridades
	public static void updateReferencia(Ponto ponto, RTree prioridades, RTree pontos) {

		Ponto referencia = ponto.getReferencia();
		Point point = new Point(new double[]{referencia.getX(), referencia.getY()});
		Visitor visitor = new Visitor();
		Ponto novaReferencia = new Ponto();
		novaReferencia.setDistancia(Float.MAX_VALUE);

		if (prioridades != null) {
			prioridades.nearestNeighborQuery(1, point, visitor);
			novaReferencia.setId(visitor.data.getIdentifier());
			novaReferencia.setDistancia(distancia(point, (Region) visitor.data.getShape()));
		}
		visitor = new Visitor();
		if (novaReferencia.getDistancia() > 50) {
			pontos.nearestNeighborQuery(1, point, visitor);
			float distancia = distancia(point, (Region) visitor.data.getShape());
			if (distancia < novaReferencia.getDistancia()) {
				novaReferencia.setId(visitor.data.getIdentifier());
				novaReferencia.setDistancia(distancia);
			}
		}
		ponto.setReferencia(novaReferencia);
	}

	
	// identificando o ponto de referencia
	// Aplica padrão de projeto Visitor para percorrer estrutura de nós
	public static void updateReferencia(Ponto ponto, RTree pontos) {

		try {
			Ponto referencia = ponto.getReferencia();
			
			float x =  referencia.getX() == null ? 0 :referencia.getX();
			float y =  referencia.getY() == null ? 0 :referencia.getY();

			Point point = new Point(new double[]{x, y});
			Visitor visitor = new Visitor();
			Ponto novaReferencia = new Ponto();
			novaReferencia.setDistancia(Float.MAX_VALUE);

			visitor = new Visitor();
			if (novaReferencia.getDistancia() > 30) {
				pontos.nearestNeighborQuery(1, point, visitor);
				float distancia = distancia(point, (Region) visitor.data.getShape());
				if (distancia < novaReferencia.getDistancia()) {
					novaReferencia.setId(visitor.data.getIdentifier());
					novaReferencia.setDistancia(distancia);
				}
			}
			ponto.setReferencia(novaReferencia);

		} catch (Exception e) {
			System.out.println("ERROR:" + e.getMessage());
		}
	}

	private static Float distancia(Point p1, Region  p2) {
		double lat1 = p1.getCoord(0) / (180D / (22D / 7D));
		double lng1 = p1.getCoord(1) / (180D / (22D / 7D));
		double lat2 = p2.getCenter()[0] / (180D / (22D / 7D));
		double lng2 = p2.getCenter()[1] / (180D / (22D / 7D));
		return (float) (long) (6378800D * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1)));
	}
	
	public static Map<Integer, RTree> findPontos(List<Ponto> points, Integer rootPoint) throws Exception {

		Map<Integer, RTree> result = new HashMap<Integer, RTree>();
		try {
			for (Ponto p : points) {
				Ponto ponto = p;
				if (!result.containsKey(rootPoint)) {
					PropertySet propertySet = new PropertySet();
					propertySet.setProperty("IndexCapacity", 5);
					propertySet.setProperty("LeafCapactiy", 5);
					result.put(rootPoint, new RTree(propertySet, SpatialIndex.createMemoryStorageManager(null)));
				}
				Point point = new Point(new double[]{ponto.getX(), ponto.getY()});
				result.get(rootPoint).insertData(ponto.getDescricao().getBytes(), point, ponto.getId());
			}

		} catch (Exception e) {
			System.out.println(Dateutil.parseDateAsString(new Date()) + "[Não foi possivel montar a Árvore RTree de pontos]");
			e.printStackTrace();
		}
		return result;
	}
	
}