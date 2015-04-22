package edu.ub.scg.elgranturista;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ExternalDB {
	private ArrayList<Opinio> ops;
	private ArrayList<User> us;
	private ArrayList<String> ids;

	public ExternalDB(){
		
	}
	
	public ArrayList<Opinio> getOpinions(String monument) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		ops = new ArrayList<Opinio>();
		query.whereEqualTo("Monument", monument);
		try {
			List<ParseObject> opinionsQuery = query.find();
    	    for(int i=0;i<opinionsQuery.size();i++){
    	    	Opinio o = new Opinio(opinionsQuery.get(i).getObjectId(),
    	    			opinionsQuery.get(i).getString("Text"),
    	    			opinionsQuery.get(i).getString("User"),
    	    			opinionsQuery.get(i).getInt("Likes"), 
    	    			opinionsQuery.get(i).getInt("Reports"),
    	    			opinionsQuery.get(i).getString("Imatge"),
    	    			opinionsQuery.get(i).getInt("Puntuacio"));
    	    	ops.add(o);
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return ops;
	}
	
	public ArrayList<String> getFotosUserHasLiked() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		ArrayList<String>imatges = new ArrayList<String>();
		InternalDB iDB=InternalDB.getInstance(null);
		query.whereContainsAll("LikesUsers", Arrays.asList(iDB.getCurrentUser().getNom()));
		try {
			List<ParseObject> opinionsQuery = query.find();
    	    for(int i=0;i<opinionsQuery.size();i++){
    	    	
    	    	imatges.add(opinionsQuery.get(i).getObjectId());
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return imatges;
	}
	public ArrayList<String> getFotosUserHasReported() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		ArrayList<String>imatges = new ArrayList<String>();
		InternalDB iDB=InternalDB.getInstance(null);
		query.whereContainsAll("ReportsUsers", Arrays.asList(iDB.getCurrentUser().getNom()));
		try {
			List<ParseObject> opinionsQuery = query.find();
    	    for(int i=0;i<opinionsQuery.size();i++){
    	    	
    	    	imatges.add(opinionsQuery.get(i).getObjectId());
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return imatges;
	}
	
	
	public ArrayList<Opinio> getOpinionsWhereUserLiked() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		ops = new ArrayList<Opinio>();
		InternalDB iDB=InternalDB.getInstance(null);
		query.whereContainsAll("LikesUsers", Arrays.asList(iDB.getCurrentUser().getNom()));
		try {
			List<ParseObject> opinionsQuery = query.find();
    	    for(int i=0;i<opinionsQuery.size();i++){
    	    	Opinio o = new Opinio(opinionsQuery.get(i).getObjectId(),
    	    			opinionsQuery.get(i).getString("Text"),
    	    			opinionsQuery.get(i).getString("User"),
    	    			opinionsQuery.get(i).getInt("Likes"), 
    	    			opinionsQuery.get(i).getInt("Reports"),
    	    			opinionsQuery.get(i).getString("Imatge"),
    	    			opinionsQuery.get(i).getInt("Puntuacio"));
    	    	ops.add(o);
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return ops;
	}
	
	public ArrayList<Opinio> getOpinionsWhereUserReported() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		ops = new ArrayList<Opinio>();
		InternalDB iDB=InternalDB.getInstance(null);
		query.whereContainsAll("ReportsUsers", Arrays.asList(iDB.getCurrentUser().getNom()));
		try {
			List<ParseObject> opinionsQuery = query.find();
    	    for(int i=0;i<opinionsQuery.size();i++){
    	    	Opinio o = new Opinio(opinionsQuery.get(i).getObjectId(),
    	    			opinionsQuery.get(i).getString("Text"),
    	    			opinionsQuery.get(i).getString("User"),
    	    			opinionsQuery.get(i).getInt("Likes"), 
    	    			opinionsQuery.get(i).getInt("Reports"),
    	    			opinionsQuery.get(i).getString("Imatge"),
    	    			opinionsQuery.get(i).getInt("Puntuacio"));
    	    	ops.add(o);
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return ops;
	}
	
	public ArrayList<String> getFotosId(String monument) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		ids = new ArrayList<String>();
		query.whereEqualTo("nomMonument", monument);
		try {
			List<ParseObject> opinionsQuery = query.find();
    	    for(int i=0;i<opinionsQuery.size();i++){
    	    	ids.add(opinionsQuery.get(i).getObjectId());
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return ids;
	}
	
	public void opinarMonument(User cU,String monument, int IDM, String text, float f){
		ParseObject o = new ParseObject("Opinio");
		o.put("Text", text);
		o.put("Monument", monument);
		o.put("Puntuacio", f);
		o.put("User", cU.getNom());
		o.put("Likes", 0);
		o.put("Reports", 0);
		o.saveInBackground();
		afegirOpinioUsuari(monument,cU);
		addPuntsUserMonument(monument,IDM, f);
	}

	public void likeOpinio(User cU,Opinio o){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		 
		try {
			ParseObject obj = query.get(o.getId());
			obj.increment("Likes");
			obj.saveInBackground();
			obj.addAll("LikesUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void likeFoto(User cU, String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		try {
			ParseObject obj = query.get(id);
			obj.increment("likes",1);
			obj.saveInBackground();
			obj.addAll("LikesUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dislikeOpinio(User cU,Opinio o){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		 
		try {
			ParseObject obj = query.get(o.getId());
			obj.increment("Likes",-1);
			obj.saveInBackground();
			obj.removeAll("LikesUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dislikeFoto(User cU, String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		try {
			ParseObject obj = query.get(id);
			obj.increment("likes",-1);
			obj.saveInBackground();
			obj.removeAll("LikesUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void reportOpinio(User cU, Opinio o){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		 
		try {
			ParseObject obj = query.get(o.getId());
			obj.increment("Reports");
			obj.saveInBackground();
			obj.addAll("ReportsUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reportFoto(User cU, String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		try {
			ParseObject obj = query.get(id);
			obj.increment("reports",1);
			obj.saveInBackground();
			obj.addAll("ReportsUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void unreportOpinio(User cU, Opinio o){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		 
		try {
			ParseObject obj = query.get(o.getId());
			obj.increment("Reports",-1);
			obj.saveInBackground();
			obj.removeAll("ReportsUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void unreportFoto(User cU, String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		try {
			ParseObject obj = query.get(id);
			obj.increment("reports",-1);
			obj.saveInBackground();
			obj.removeAll("ReportsUsers", Arrays.asList(cU.getNom()));
			obj.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean hasUserLikedOpinio(String u, Opinio o){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		query.whereEqualTo("objectId",o.getId());
		query.whereContainsAll("LikesUsers", Arrays.asList(u));
		try {
			List<ParseObject> p = query.find();
			if(p.size()==1){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean hasUserLikedFoto(String u, String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		query.whereEqualTo("objectId",id);
		query.whereContainsAll("LikesUsers", Arrays.asList(u));
		try {
			List<ParseObject> p = query.find();
			if(p.size()==1){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean hasUserReportedOpinio(String u, Opinio o){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Opinio");
		query.whereEqualTo("objectId",o.getId());
		query.whereContainsAll("ReportsUsers", Arrays.asList(u));
		try {
			List<ParseObject> p = query.find();
			if(p.size()==1){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean hasUserReportedFoto(String u, String id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ImatgeCongrats");
		query.whereEqualTo("objectId",id);
		query.whereContainsAll("ReportsUsers", Arrays.asList(u));
		try {
			List<ParseObject> p = query.find();
			if(p.size()==1){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<User> getUsers() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		us = new ArrayList<User>();
		try {
			List<ParseUser> usersQuery = query.find();
    	    for(int i=0;i<usersQuery.size();i++){
    	    	User u = new User(usersQuery.get(i).getObjectId(),
    	    			usersQuery.get(i).getString("username"),
    	    			usersQuery.get(i).getInt("Punts"), 
    	    			usersQuery.get(i).getString("Avatar"));
    	    	us.add(u);
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return us;
	}
	
	
	public User getCurrentUser(){
		ParseUser pu = ParseUser.getCurrentUser();
		if(pu==null){
			return null;
		}
		User user = getUser(pu.getString("username"));
		User u = new User(pu.getObjectId(),
    			pu.getString("username"),
    			user.getPunts(), 
    			user.getImatge());
		return u;
	}
	
	public User getUser(String nom){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereContains("username", nom);
		try {
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			User u = new User(user.getObjectId(),
	    			user.getString("username"),
					user.getInt("Punts"), 
					user.getString("Avatar"));
			return u;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public User getUserOnPuntsEstanFiltrats(String nom, String filtre){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereContains("username", nom);
		try {
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			User u = new User(user.getObjectId(),
	    			user.getString("username"),
					user.getInt("Punts"+filtre), 
					user.getString("Avatar"));
			return u;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void addPointsUser(User u, int punts){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.increment("Punts", punts);
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public void addPointsFiltreUser(User u, int punts, String filtre){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			String col = "Punts"+filtre;
			user.increment(col, punts);
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> getUsersOrderedPoints() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.orderByDescending("Punts");
		us = new ArrayList<User>();
		try {
			List<ParseUser> usersQuery = query.find();
    	    for(int i=0;i<usersQuery.size();i++){
    	    	User u = new User(usersQuery.get(i).getObjectId(),
    	    			usersQuery.get(i).getString("username"),
    	    			usersQuery.get(i).getInt("Punts"), 
    	    			usersQuery.get(i).getString("Avatar"));
    	    	us.add(u);
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return us;
	}
	
	/*
	 * IMPORTANT: Aquest ArrayList de Users retorna el User on el
	 * camp de Points correspon als POINTS DEL FILTRE, NO AL GLOBAL.
	 */
	public ArrayList<User> getUsersOrderedPointsCategoria(String filtre) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.orderByDescending("Punts"+filtre);
		us = new ArrayList<User>();
		try {
			List<ParseUser> usersQuery = query.find();
    	    for(int i=0;i<usersQuery.size();i++){
    	    	User u = new User(usersQuery.get(i).getObjectId(),
    	    			usersQuery.get(i).getString("username"),
    	    			usersQuery.get(i).getInt("Punts"+filtre), 
    	    			usersQuery.get(i).getString("Avatar"));
    	    	us.add(u);
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return us;
	}

	public void afegirMonumentUsuari(Monument m, User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.addUnique("MonumentsVisitats", m.getNom());
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	public void afegirMonumentUsuariFotografiat(Monument m, User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.addUnique("MonumentsFotografiats", m.getNom());
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public void afegirMedallaUsuari(String medalla, User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.addUnique("medalles", medalla);
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getMonumentsVisitatsUsuari(User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		ArrayList<String> ms = new ArrayList<String>();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			if(usersQuery.get(0).getJSONArray("MonumentsVisitats")==null){
				return ms;
			}
    	    for(int i=0;i<usersQuery.get(0).getJSONArray("MonumentsVisitats").length();i++){
    	    	try {
					ms.add(usersQuery.get(0).getJSONArray("MonumentsVisitats").getString(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return ms;
	}
	
	public ArrayList<Monument> getMonumentsVisitatsUsuariMonument(User u){
		InternalDB iDB = InternalDB.getInstance(null);
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		ArrayList<Monument> ms = new ArrayList<Monument>();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			if(usersQuery.get(0).getJSONArray("MonumentsVisitats")==null){
				return ms;
			}
    	    for(int i=0;i<usersQuery.get(0).getJSONArray("MonumentsVisitats").length();i++){
    	    	try {
					ms.add(iDB.selectMonumentByName(usersQuery.get(0).getJSONArray("MonumentsVisitats").getString(i)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		Collections.sort(ms,new Comparator<Monument>(){
		    public int compare(Monument u1, Monument u2) {
		        return u2.getPunts()-u1.getPunts();
		    }
		});
		return ms;
	}
	
	
	
	public ArrayList<String> getMedallesUsuari(User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		ArrayList<String> medalles = new ArrayList<String>();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			if(usersQuery.get(0).getJSONArray("medalles")==null){
				return medalles;
			}
    	    for(int i=0;i<usersQuery.get(0).getJSONArray("medalles").length();i++){
    	    	try {
					medalles.add(usersQuery.get(0).getJSONArray("medalles").getString(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return medalles;
	}
	
	public void addAmistat(User cU,User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", cU.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.addUnique("Amics", u.getNom());
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public void removeAmistat(User cU,User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", cU.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.removeAll("Amics",Arrays.asList(u.getNom()));
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getAmistats(User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		ArrayList<String> amics = new ArrayList<String>();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			if(usersQuery.get(0).getJSONArray("Amics")==null){
				return amics;
			}
    	    for(int i=0;i<usersQuery.get(0).getJSONArray("Amics").length();i++){
    	    	try {
					amics.add(usersQuery.get(0).getJSONArray("Amics").getString(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return amics;
	}
	
//	public String getAvatar(User u){
//		ParseQuery<ParseUser> query = ParseUser.getQuery();
//		ArrayList<String> amics = new ArrayList<String>();
//		query.whereEqualTo("username", u.getNom());
//		try {
//			List<ParseUser> usersQuery = query.find();
//			if(usersQuery.get(0).getJSONArray("Amics")==null){
//				return amics;
//			}
//    	    for(int i=0;i<usersQuery.get(0).getJSONArray("Amics").length();i++){
//    	    	try {
//					amics.add(usersQuery.get(0).getJSONArray("Amics").getString(i));
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    	    }
//		return amics;
//	}

	public ArrayList<User> getAmistatsUser(User u){
		ArrayList<String> amics = new ArrayList<String>();
		ArrayList<User> allUs = getUsersOrderedPoints();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			if(usersQuery.get(0).getJSONArray("Amics")==null){
				return allUs;
			}
    	    for(int i=0;i<usersQuery.get(0).getJSONArray("Amics").length();i++){
    	    	try {
					amics.add(usersQuery.get(0).getJSONArray("Amics").getString(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		for(int i=0;i<allUs.size();i++){
			if((!amics.contains(allUs.get(i).getNom()))){
				allUs.remove(i);
				i--;
			}
		}
		return allUs;
	}
	
	public boolean isUserAmic(User cU,User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", cU.getNom());
		query.whereContainsAll("Amics", Arrays.asList(u.getNom()));
		try {
			List<ParseUser> p = query.find();
			if(p.size()==1){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<User> getAmistatsUserAmbPuntsFiltrats(User u, String filtre){
		ArrayList<String> amics = new ArrayList<String>();
		ArrayList<User> allUs = getUsersOrderedPointsCategoria(filtre);
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			if(usersQuery.get(0).getJSONArray("Amics")==null){
				return allUs;
			}
    	    for(int i=0;i<usersQuery.get(0).getJSONArray("Amics").length();i++){
    	    	try {
					amics.add(usersQuery.get(0).getJSONArray("Amics").getString(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		for(int i=0;i<allUs.size();i++){
			if((!amics.contains(allUs.get(i).getNom()))){
				allUs.remove(i);
				i--;
			}
		}
		return allUs;
	
	}

	public void logOut() {
		ParseUser.logOut();
	}
	
	public void addPuntsUserMonument(String m, int IDM, float punts){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Monument");
		try {
			List<ParseObject> pobj = query.find();
			ParseObject obj = pobj.get(0);
			JSONArray a = obj.getJSONArray("PuntsUser");
			try {
				a.put(IDM-1, Float.parseFloat(a.get(IDM-1).toString())+punts);
				obj.saveInBackground();
				a = obj.getJSONArray("NumeroOpinions");
				a.put(IDM-1, Float.parseFloat(a.get(IDM-1).toString())+1);
				obj.saveInBackground();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public ArrayList<Float> getMonumentsPuntsUsers(){
		ArrayList<Float> p = new ArrayList<Float>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Monument");
		List<ParseObject> pobj;
		try {
			pobj = query.find();
			ParseObject obj = pobj.get(0);
			JSONArray a = obj.getJSONArray("PuntsUser");
			JSONArray b = obj.getJSONArray("NumeroOpinions");
			float m1=0;
			for(int i=0;i<a.length();i++){
				m1=0;
				try {
					if(Float.parseFloat(b.get(i).toString())>0){
						m1 = Float.parseFloat(a.get(i).toString())/Float.parseFloat(b.get(i).toString());
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				p.add(m1);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	
	
	public void afegirOpinioUsuari(String m, User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.addUnique("MonumentsOpinats", m);
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public void addQuestionariCompletat(User cU,Monument m){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", cU.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.addUnique("QuestionarisCompletats", m.getNom());
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public boolean hasFotoBeenTaken(Monument m) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAvatar(User cU, String str) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", cU.getNom());
		try{
			List<ParseUser> pu = query.find();
			ParseUser user = pu.get(0);
			user.put("Avatar", str);
			user.saveInBackground();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	
	public String getAvatarUser(User u){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", u.getNom());
		try {
			List<ParseUser> usersQuery = query.find();
			return usersQuery.get(0).getString("Avatar");
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	
	
}
