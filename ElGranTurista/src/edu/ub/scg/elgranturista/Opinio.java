package edu.ub.scg.elgranturista;

public class Opinio {
	private String id;
	private String user;
	private String text;
	private int likes;
	private int reports;
	private String image;
	private int puntuacio;
	
	public Opinio(String text, String user, int likes, int reports, 
			String image, int puntuacio) {
		super();
		this.user=user;
		this.text = text;
		this.likes = likes;
		this.reports = reports;
		this.image = image;
		this.puntuacio = puntuacio;
	}
	
	public Opinio(String id, String text,String user, int likes, int reports, 
			String image, int puntuacio) {
		super();
		this.id = id;
		this.user=user;
		this.text = text;
		this.likes = likes;
		this.reports = reports;
		this.image = image;
		this.puntuacio = puntuacio;
	}
	
	public Opinio(){
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPuntuacio() {
		return puntuacio;
	}

	public void setPuntuacio(int puntuacio) {
		this.puntuacio = puntuacio;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPunts() {
		return puntuacio;
	}
	public void setPunts(int punts) {
		this.puntuacio = punts;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
