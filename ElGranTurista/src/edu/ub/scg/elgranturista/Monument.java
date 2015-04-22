package edu.ub.scg.elgranturista;

import java.io.Serializable;

public class Monument implements Serializable{
	private int id;
	private String nom;
	private int punts;
	private String imatge;
	private double lat;
	private double lng;
	private String[] q;
	private String[] ar;
	private String[] aw1;
	private String[] aw2;
	private float puntsUsers;
	
	public float getPuntsUsers() {
		return puntsUsers;
	}

	public void setPuntsUsers(float puntsUsers) {
		this.puntsUsers = puntsUsers;
	}

	public Monument(int id, String nom, int punts, String imatge,
			double lat, double lng, String q, String ar, String aw1, String aw2,
			String q2,String ar2, String aw12, String aw22, String q3,
			String ar3, String aw13, String aw23) {
		super();
		this.id = id;
		this.nom = nom;
		this.punts = punts;
		this.imatge = imatge;
		this.lat = lat;
		this.lng = lng;
		this.q = new String[]{q,q2,q3};
		this.ar = new String[]{ar,ar2,ar3};
		this.aw1 = new String[]{aw1,aw12,aw13};
		this.aw2 = new String[]{aw2,aw22,aw23};
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Monument(){
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String[] getQ() {
		return q;
	}

	public void setQ(String[] q) {
		this.q = q;
	}

	public String[] getAr() {
		return ar;
	}

	public void setAr(String[] ar) {
		this.ar = ar;
	}

	public String[] getAw1() {
		return aw1;
	}

	public void setAw1(String[] aw1) {
		this.aw1 = aw1;
	}

	public String[] getAw2() {
		return aw2;
	}

	public void setAw2(String[] aw2) {
		this.aw2 = aw2;
	}
}
