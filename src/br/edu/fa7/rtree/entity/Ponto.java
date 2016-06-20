package br.edu.fa7.rtree.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.postgis.Geometry;

@Entity
@Table(name = "pontos")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Ponto implements Serializable{
	
	private static final long serialVersionUID = 1313916916086964373L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String descricao;
	@Column(name="x")
	private Float x;
	
	@Column(name="y")
	private Float y;
	
	@Column(name="distancia")
	private Float distancia;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="ponto_id")
	private Ponto referencia;
	
	@Type(type="br.edu.fa7.rtree.util.GeometryType")
	@Column(name = "the_geom", nullable = false)
	private Geometry geometry;
	
	@Column(name="avalia_aplicacao", nullable=false)
	private Boolean avaliacaoAplicacao;
	
	@Column(name="avalia_metodo", nullable=false)
	private String metodo;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	public Ponto getReferencia() {
		return referencia;
	}

	public void setReferencia(Ponto referencia) {
		this.referencia = referencia;
	}

	public Float getDistancia() {
		return distancia;
	}

	public void setDistancia(Float distancia) {
		this.distancia = distancia;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Boolean getAvaliacaoAplicacao() {
		return avaliacaoAplicacao;
	}

	public void setAvaliacaoAplicacao(Boolean avaliacaoAplicacao) {
		this.avaliacaoAplicacao = avaliacaoAplicacao;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	
	public int hashCode() {

		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		result = 31 * result + ((x == null) ? 0 : x.hashCode());
		result = 31 * result + ((y == null) ? 0 : y.hashCode());

		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Ponto other = (Ponto) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((descricao == null && other.x == null) || (x != null && x.equals(other.x))) &&
		((descricao == null && other.y == null) || (y != null && y.equals(other.y)));
	}

}
