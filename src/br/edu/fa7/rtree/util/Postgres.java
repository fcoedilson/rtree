package br.edu.fa7.rtree.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.fa7.rtree.entity.Ponto;
import br.edu.fa7.rtree.util.Dateutil;
import spatialindex.rtree.RTree;
import spatialindex.spatialindex.Point;
import spatialindex.spatialindex.SpatialIndex;
import spatialindex.storagemanager.PropertySet;

public class Postgres {

	private static final String url = "jdbc:postgresql://localhost:5432/rtree";
	private static final String user = "postgres";
	private static final String passwd = "postgres";

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() {
		return getConn(url);
	}

	public static Connection getConn(String url) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {}
	}

	public static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {}
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

	
	// Montando -Arvore Rtree do conjunto de pontos
	public static Map<Integer, RTree> findPontos(Connection conn) throws Exception {

		String findpontos = "SELECT id, descricao, x, y FROM pontos";
		Integer start = 0;

		Map<Integer, RTree> result = new HashMap<Integer, RTree>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn.prepareStatement(findpontos);
			rs = stmt.executeQuery();
			while (rs.next()) {

				Ponto ponto = new Ponto();
				ponto.setId(rs.getInt("id"));
				ponto.setX(rs.getFloat("X"));
				ponto.setY(rs.getFloat("Y"));
				ponto.setDescricao(rs.getString("descricao"));

				if (!result.containsKey(start)) {
					PropertySet propertySet = new PropertySet();
					propertySet.setProperty("IndexCapacity", 5);
					propertySet.setProperty("LeafCapactiy", 5);
					result.put(start, new RTree(propertySet, SpatialIndex.createMemoryStorageManager(null)));
				}
				Point point = new Point(new double[]{ponto.getX(), ponto.getY()});
				result.get(start).insertData(ponto.getDescricao().getBytes(), point, ponto.getId());
			}

		}  catch (SQLException e) {
			System.out.println(Dateutil.parseDateAsString(new Date()) + 
					"[Não foi possivel montar a Árvore RTree de pontos]");
		} finally {
			close(rs);
			close(stmt);
		}
		return result;
	}
	
	// lista de pontos
	public static List<Ponto> retrievePontos(){
		
		String findpontos = "SELECT id, descricao, x, y FROM pontos";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Ponto> result = new ArrayList<Ponto>();
		
		try {
			conn = getConn(url);
			stmt = conn.prepareStatement(findpontos);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Ponto p = new Ponto();
				int pid = rs.getInt("id");
				String descp = rs.getString("descricao");
				float x = rs.getFloat("x");
				float y = rs.getFloat("y");
				p.setId(pid);
				p.setDescricao(descp);
				p.setX(x);
				p.setY(y);
				result.add(p);
			}
		} catch (SQLException e) {
			System.out.println(Dateutil.parseDateAsString(new Date()) + "[Não foi possivel realizar a consulta no banco de dados]");
		} finally {
			close(rs);
			close(stmt);
			close(conn);
		}
		return result;
	}
	
	// lista de pontos sem referencia
	public static List<Ponto> pontosSemReferencia(Connection conn)throws Exception {
		
		String query = "SELECT id, x,y FROM pontos WHERE ponto_id IS NULL order by id LIMIT 1000;";
		List<Ponto> result = new ArrayList<Ponto>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Ponto p = new Ponto();
				p.setId(rs.getInt("id"));
				p.setX(rs.getFloat("X"));
				p.setY(rs.getFloat("Y"));
				result.add(p);
			}

		} finally {
			close(rs);
			close(stmt);
		}
		return result;
	}

	// attualização do ponto de referencia
	public static boolean updatePonto(Connection conn, Ponto p) throws Exception {

		String updatePonto = "UPDATE pontos SET ponto_id = ?, descricao = ? WHERE id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(updatePonto);
			stmt.setInt(1, p.getReferencia().getId());
			stmt.setString(2, "Ext-R-Tree");
			stmt.setInt(3, p.getId());
			return stmt.execute();
		} catch (SQLException e) {
			System.out.println(Dateutil.parseDateAsString(new Date()) + 
					"[Não foi possivel realizar a consulta no banco de dados]");
			return false;
		} finally {
			close(stmt);
		}
	}
	
	
	public static void gravarTempo(int pontos, float tempo) {
		
		String insert = "Insert into tempo_externo(pontos, tempo) values(?, ?)";
		PreparedStatement stmt = null;
		Connection conn = getConn(url);
		try {
			stmt = conn.prepareStatement(insert);
			stmt.setInt(1, pontos);
			stmt.setFloat(2, tempo);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(Dateutil.parseDateAsString(new Date()) + 
					"[Não foi possivel gravar resultado ....]");
		} finally {
			close(stmt);
		}
	}
	
	
	public static Ponto findReference(Ponto ponto){
		
		String query = "select id, x,y from pontos where id != ? "
				+ "order by sqrt(power( ? - p.x, 2) + power(? - p.y, 2) ) limit 1";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = getConn(url);
		Ponto p = null;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, p.getId());
			stmt.setFloat(2, p.getX());
			stmt.setFloat(3, p.getY());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				p = new Ponto();
				p.setId(rs.getInt("id"));
				p.setX(rs.getFloat("X"));
				p.setY(rs.getFloat("Y"));
			}
			
		} catch (SQLException e) {
			System.out.println(Dateutil.parseDateAsString(new Date()) + 
					"[Não foi possivel gravar resultado ....]");
		} finally {
			close(stmt);
		}
		return p;
	}
}
