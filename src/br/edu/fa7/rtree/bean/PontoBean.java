package br.edu.fa7.rtree.bean;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.edu.fa7.rtree.entity.Ponto;
import br.edu.fa7.rtree.service.PontoService;
import br.edu.fa7.rtree.util.Dateutil;
import br.edu.fa7.rtree.util.Messages;
import br.edu.fa7.rtree.util.Rtree;
import spatialindex.rtree.RTree;


@Component("pontoBean")
@Scope("session")
public class PontoBean extends EntityBean<Integer, Ponto>{

	@Autowired
	private PontoService pontoService;

	private Ponto ponto;
	private List<Ponto> pontos = new ArrayList<>();
	private String points;

	private Ponto pontoProximo;
	private Float lat;
	private Float lng;
	private String desc;
	private Double distance;

	private String metodo = "R-Tree";
	private Boolean avaliacao = true;


	@Override
	protected Ponto createNewEntity() {
		Ponto p = new Ponto();
		p.setReferencia(new Ponto());
		p.setAvaliacaoAplicacao(true); // default value
		p.setMetodo("R-Tree"); // default value
		return p;
	}

	@Override
	protected Integer retrieveEntityId(Ponto entity) {
		return entity.getId();
	}


	@Override
	protected PontoService retrieveEntityService() {
		return this.pontoService;
	}

	@Override
	public String prepareSave() {
		this.lat = null;
		this.lng = null;
		points = new String();
		metodo = "R-Tree";
		avaliacao = true;
		return super.prepareSave();
	}

	public String search(){
		this.entity = createNewEntity();
		points = new String();
		metodo = "R-Tree";
		avaliacao = true;
		return super.search();
	}

	
	public String save() {

		pontos = new ArrayList<>();
		points = points.replace(",", "");
		String[] array = points.split("#%"); 
		if ( array.length > 1 ){

			for (String s :array){
				Ponto p = new Ponto();
				p.setDescricao("gmaps insert");
				this.lng = Float.parseFloat(s.split(";")[1]);
				this.lat = Float.parseFloat(s.split(";")[0]);
				Point p1 = new Point(this.lng, this.lat);
				p1.setSrid(31984);
				p.setGeometry(p1);
				p.setX(this.lng);
				p.setY(this.lat);
				p.setAvaliacaoAplicacao(this.avaliacao);
				p.setMetodo(this.metodo);
				pontos.add(p);
			}
		}
		int tot = pontos.size();

		if ( tot > 1 ){
			for (Ponto  tosave: pontos) {
				this.entity = tosave;
				if(this.avaliacao == true && this.metodo.equals("R-Tree")){
					try {
						encontrarReferenciaRtree();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.save();
			}
			Messages.addInfo( pontos.size() + " Pontos inseridos!!");
			return SUCCESS;

		} else {

			if(this.lat != null && this.lng != null){
				Point geomPonto = new Point(this.lng, this.lat);
				geomPonto.setSrid(31984);
				this.entity.setGeometry(geomPonto);
				this.entity.setX(this.lng);
				this.entity.setY(this.lat);
				this.entity.setDescricao("gmaps insert");
				this.entity.setAvaliacaoAplicacao(this.avaliacao);
				this.entity.setMetodo(this.metodo);
				if(this.avaliacao == true && this.metodo.equals("R-Tree")){
					try {
						encontrarReferenciaRtree();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.save();
				Messages.addInfo("1 Ponto inserido!!");
				return SUCCESS;
			} else {
				return FAIL;
			}
		}

	}
	
	public void encontrarReferenciaRtree() throws Exception{
		Long timeIni = System.currentTimeMillis();
		Long timeFim = System.currentTimeMillis();
		
		List<Ponto> allPontos = pontoService.retrieveAll();
		Map<Integer, RTree> arvorePontos = Rtree.findPontos(allPontos, 0);
		
		Date ini = new Date();
		System.out.println("Begin: " + Dateutil.parseAsString("dd/MM/yyyy HH:mm:ss", ini));
		
		if (this.entity.getReferencia() == null){
			this.entity.setReferencia(new Ponto());
		}
		
		Rtree.updateReferencia(this.entity, arvorePontos.get(0));
		
		timeFim = System.currentTimeMillis();
		Date fim = new Date();
		System.out.println("End: " + Dateutil.parseAsString("dd/MM/yyyy HH:mm:ss", fim));
		
		// gravando tempos
		float total = Dateutil.tempoEntreDatasLong(ini, fim);

		System.out.println("Duration:" + Dateutil.tempoEntreDatasLong(ini, fim));
	}


	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public Ponto getPontoProximo() {
		return pontoProximo;
	}

	public void setPontoProximo(Ponto pontoProximo) {
		this.pontoProximo = pontoProximo;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Ponto getPonto() {
		return ponto;
	}

	public void setPonto(Ponto ponto) {
		this.ponto = ponto;
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}

	public PontoService getPontoService() {
		return pontoService;
	}

	public void setPontoService(PontoService pontoService) {
		this.pontoService = pontoService;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Boolean getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Boolean avaliacao) {
		this.avaliacao = avaliacao;
	}

}
