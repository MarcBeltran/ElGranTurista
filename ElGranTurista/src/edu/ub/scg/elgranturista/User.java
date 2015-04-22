package edu.ub.scg.elgranturista;

import java.io.Serializable;

public class User implements Serializable {
	private String id;
	private String nom;
	private int punts;
	private String imatge;
	private float[] coordenades;
	
	public User(String id, String nom, int punts, String imatge,
			float[] coordenades) {
		super();
		this.id = id;
		this.nom = nom;
		this.punts = punts;
		this.imatge = imatge;
		this.coordenades = coordenades;
	}
	
	public User(String id, String nom, int punts, String imatge){
		super();
		this.id=id;
		this.nom=nom;
		this.punts=punts;
		this.imatge=imatge;
	}
	
	public User(){
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getPunts() {
		return punts;
	}
	public void setPunts(int punts) {
		this.punts = punts;
	}
	public String getImatge() {
		return imatge;
	}
	public void setImatge(String imatge) {
		this.imatge = imatge;
	}
	public float[] getCoordenades() {
		return coordenades;
	}
	public void setCoordenades(float[] coordenades) {
		this.coordenades = coordenades;
	}
}
