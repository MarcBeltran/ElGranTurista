package edu.ub.scg.elgranturista;

import java.util.Arrays;

import org.json.JSONArray;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("ImatgeCongrats")
public class ImatgeCongrats extends ParseObject {
	String nomUsuari,nomMonument;
	int likes,reports;
	ParseFile imatge;
	JSONArray likesUsers,reportsUsers;
	
	public ImatgeCongrats() {
	    // A default constructor is required.
	}
	
	public String getNomMonument() {
		 return getString("nomMonument");
	}

	public void setNomMonument(String nomMonument) {
		put("nomMonument",nomMonument);
	}

	public String getNomUsuari() {
		 return getString("nomUsuari");
	}
	public void setNomUsuari(String nomUsuari) {
		put("nomUsuari",nomUsuari);
	}
	public Integer getLikes() {
		 return getInt("likes");
	}
	public void setLikes(int likes) {
		put("likes",likes);
	}
	public Integer getReports() {
		 return getInt("reports");
	}
	public void setReports(int reports) {
		put("reports",reports);
	}

	public ParseFile getImatge() {
	    return getParseFile("photo");
	}
	public void setImatge(ParseFile imatge) {
		put("photo",imatge);
	}
	
	public JSONArray getLikesUsers() {
	    return getJSONArray("LikesUsers");
	}
	public void setLikesUsers(String us) {
		addAll("LikesUsers", Arrays.asList(us));
	}
	public JSONArray getReportsUsers() {
	    return getJSONArray("ReportsUsers");
	}
	public void setReportsUsers(String us) {
		addAll("ReportsUsers", Arrays.asList(us));
	}
	
}
