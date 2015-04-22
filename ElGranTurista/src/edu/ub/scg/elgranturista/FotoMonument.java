package edu.ub.scg.elgranturista;

import android.graphics.Bitmap;

public class FotoMonument {
	private String id;
	private String nomUsr;
	private int likes;
	private int reports;
	private Bitmap image;
	
	public FotoMonument(String nomUsr, int likes, int reports, 
			Bitmap image) {
		super();
		this.nomUsr = nomUsr;
		this.likes = likes;
		this.reports = reports;
		this.image = image;
	}
	
	public FotoMonument(String id, String nomUsr, int likes, int reports, 
			Bitmap image) {
		super();
		this.id = id;
		this.nomUsr = nomUsr;
		this.likes = likes;
		this.reports = reports;
		this.image = image;
	}
	
	public FotoMonument(){
		super();
	}
	
	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getReports() {
		return reports;
	}

	public void setReports(int reports) {
		this.reports = reports;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return nomUsr;
	}

	public void setUser(String nomUsr) {
		this.nomUsr = nomUsr;
	}

}
