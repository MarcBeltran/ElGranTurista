package edu.ub.scg.elgranturista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InternalDB extends SQLiteOpenHelper {
	 
	private static InternalDB instance = null;
	// Logcat monument
    private static final String LOG = InternalDB.class.getName();
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "dades";
 
    // Table Names
    private static final String TABLE_MONUMENTS = "monuments";
    private static final String TABLE_TAGS_MONUMENTS = "tags_monuments";
    private static final String TABLE_FILTRES = "filtres";
    private static final String TABLE_DISTANCIA = "distancies";
 
    // Common column names
    private static final String KEY_ID = "id";
 
    // MONUMENTS Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_POINTS = "points";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_Q = "q";
    private static final String KEY_AR = "ar";
    private static final String KEY_AW1 = "aw1";
    private static final String KEY_AW2 = "aw2";
    private static final String KEY_Q2 = "q2";
    private static final String KEY_AR2 = "ar2";
    private static final String KEY_AW12 = "aw12";
    private static final String KEY_AW22 = "aw22";
    private static final String KEY_Q3 = "q3";
    private static final String KEY_AR3 = "ar3";
    private static final String KEY_AW13 = "aw13";
    private static final String KEY_AW23 = "aw23";
    
    //FILTRES Table
    private static final String KEY_FILTRE_NOM = "filtre";
    private static final String KEY_FILTRE_BOOL = "activat";
 
    //DISTANCIA Table
    private static final String KEY_DISTANCIA_NOM = "distancia";
    
    // MONUMENTS_TagNS Table - column names
    private static final String KEY_TAG = "tag";
    private static final String KEY_MONUMENT = "monument";
 
    // Table Create Statements
 
    // Monument table create statement
    private static final String CREATE_TABLE_MONUMENTS = "CREATE TABLE " + TABLE_MONUMENTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_POINTS + " INT, " + KEY_IMAGE + " TEXT, " + KEY_LAT + " DOUBLE, " + 
            KEY_LNG + " DOUBLE, "  + KEY_Q + " TEXT, "+ 
            KEY_AR + " TEXT, " +  KEY_AW1 + " TEXT, " + KEY_AW2 + " TEXT, " +
            KEY_Q2 + " TEXT, "+ 
            KEY_AR2 + " TEXT, " +  KEY_AW12 + " TEXT, " + KEY_AW22 + " TEXT, " +
            KEY_Q3 + " TEXT, "+ 
            KEY_AR3 + " TEXT, " +  KEY_AW13 + " TEXT, " + KEY_AW23 + " TEXT " +")";
 
    private static final String CREATE_TABLE_TAGS_MONUMENTS = "CREATE TABLE "
            + TABLE_TAGS_MONUMENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TAG + " INTEGER NOT NULL," + KEY_MONUMENT + " INTEGER NOT NULL " + ")";
    
    private static final String CREATE_TABLE_FILTRES = "CREATE TABLE "
    		+ TABLE_FILTRES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
    	    + KEY_FILTRE_NOM + " TEXT," + KEY_FILTRE_BOOL + " INTEGER " + ")";
    
    private static final String CREATE_TABLE_DISTANCIA = "CREATE TABLE "
    		+ TABLE_DISTANCIA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
    	    + KEY_DISTANCIA_NOM + " DOUBLE" + ")";
    
    private static final String CREATE_TABLE_CURRENTUSER = "CREATE TABLE currentUser (id INTEGER PRIMARY KEY," +
    		"nom TEXT, punts INTEGER NOT NULL, imatge TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_VISITATS = "CREATE TABLE visitats (nom TEXT," +
    		"mon TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_OPINATS = "CREATE TABLE opinats (nom TEXT," +
    		"mon TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_QUESTIONARIATS = "CREATE TABLE questionariats (nom TEXT," +
    		"mon TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_FOTOGRAFIATS = "CREATE TABLE fotografiats (nom TEXT," +
    		"mon TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_LIKES = "CREATE TABLE likes (nom TEXT," +
    		"opinio TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_REPORTS = "CREATE TABLE reports (nom TEXT," +
    		"opinio TEXT)";
    private static final String CREATE_TABLE_CURRENTUSER_LIKES_FOTOS = "CREATE TABLE likes_fotos (nom TEXT," +
    		"foto TEXT)";
    
    private static final String CREATE_TABLE_CURRENTUSER_REPORTS_FOTOS = "CREATE TABLE reports_fotos (nom TEXT," +
    		"foto TEXT)";
        
    public static InternalDB getInstance(Context ctx){
		if (instance == null) {
            instance = new InternalDB(ctx);
        }
        return instance;
	}
    
    private InternalDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
//    public InternalDB(Context context){
//    	super(context,DATABASE_NAME,null,DATABASE_VERSION);
//    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_MONUMENTS);
        db.execSQL(CREATE_TABLE_TAGS_MONUMENTS);
        db.execSQL(CREATE_TABLE_FILTRES);
        db.execSQL(CREATE_TABLE_DISTANCIA);
        db.execSQL(CREATE_TABLE_CURRENTUSER);
        db.execSQL(CREATE_TABLE_CURRENTUSER_VISITATS);
        db.execSQL(CREATE_TABLE_CURRENTUSER_OPINATS);
        db.execSQL(CREATE_TABLE_CURRENTUSER_QUESTIONARIATS);
        db.execSQL(CREATE_TABLE_CURRENTUSER_FOTOGRAFIATS);
        db.execSQL(CREATE_TABLE_CURRENTUSER_LIKES);
        db.execSQL(CREATE_TABLE_CURRENTUSER_REPORTS);
        db.execSQL(CREATE_TABLE_CURRENTUSER_LIKES_FOTOS);
        db.execSQL(CREATE_TABLE_CURRENTUSER_REPORTS_FOTOS);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_MONUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTANCIA);
        db.execSQL("DROP TABLE IF EXISTS " + "currentUser");
        db.execSQL("DROP TABLE IF EXISTS " + "visitats");
        db.execSQL("DROP TABLE IF EXISTS " + "opinats");
        db.execSQL("DROP TABLE IF EXISTS " + "questionariats");
        db.execSQL("DROP TABLE IF EXISTS " + "fotografiats");
        db.execSQL("DROP TABLE IF EXISTS " + "likes");
        db.execSQL("DROP TABLE IF EXISTS " + "reports");
        db.execSQL("DROP TABLE IF EXISTS " + "likes_fotos");
        db.execSQL("DROP TABLE IF EXISTS " + "reports_fotos");

        this.onCreate(db);
    }
    
    public void setCurrentUser(String name, int points, String imatge){
	    SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO currentUser (nom, punts,imatge) VALUES "+
	    		"('" + name + "'," + points+ ",'"+imatge+"')";
	    db.execSQL(stmt);
    }
    
    public User getCurrentUser(){
    	SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT nom, punts, imatge " +
				"FROM currentUser " +
				"WHERE id=1"; 
		Cursor c = db.rawQuery(stmt, null); 
		if (c != null) {
            c.moveToFirst();
		}
    	User cU = new User();
		cU.setNom(c.getString(0));
        cU.setPunts(Integer.parseInt(c.getString(1)));
        cU.setImatge(c.getString(2));
		return cU;
    }
    
    public void deleteCurrentUser(){
    	SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "DELETE FROM currentUser " +
	 		"WHERE id=1"; 
		db.execSQL(stmt); 
    }
    
    public void updatePuntsCurrentUser(int pts){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String stmt = "UPDATE currentUser SET punts="+String.valueOf(getCurrentUser().getPunts()+pts)+" WHERE nom='"+getCurrentUser().getNom()+"'";
		db.execSQL(stmt); 

    }
    
    public void updateImatgeCurrentUser(String i){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String stmt = "UPDATE currentUser SET imatge='"+i+"' WHERE nom='"+getCurrentUser().getNom()+"'";
		db.execSQL(stmt); 

    }
    
    public void addMonumentVisitat(User cU, String m){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO visitats (nom, mon) VALUES "+
	    		"('" + cU.getNom() + "','" + m+ "')";
	    db.execSQL(stmt);
    }
    public void addMonumentOpinats(User cU, String m){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO opinats (nom, mon) VALUES "+
	    		"('" + cU.getNom() + "','" + m+ "')";
	    db.execSQL(stmt);
    }
    public void addMonumentQuestionariats(User cU, String m){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO questionariats (nom, mon) VALUES "+
	    		"('" + cU.getNom() + "','" + m+ "')";
	    db.execSQL(stmt);
    }
    public void addMonumentFotografiats(User cU, String m){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO fotografiats (nom, mon) VALUES "+
	    		"('" + cU.getNom() + "','" + m+ "')";
	    db.execSQL(stmt);
    }
    public boolean hasMonumentBeenVisitat(User cU, Monument m) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM visitats " +
				"WHERE nom ='"+cU.getNom()+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(m.getNom())){
	    	return true;
	    }
		return false;
	} 
    
    public boolean hasMonumentBeenOpinat(User cU, Monument m) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM opinats " +
				"WHERE nom ='"+cU.getNom()+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(m.getNom())){
	    	return true;
	    }
		return false;
	} 
    
    public boolean hasMonumentBeenQuestionariat(User cU, Monument m) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM questionariats " +
				"WHERE nom ='"+cU.getNom()+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(m.getNom())){
	    	return true;
	    }
		return false;
	}
    public boolean hasMonumentBeenFotografiat(User cU, Monument m) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM fotografiats " +
				"WHERE nom ='"+cU.getNom()+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(m.getNom())){
	    	return true;
	    }
		return false;
	} 
    public void addOpinioLike(User cU, String op){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO likes (nom, opinio) VALUES "+
	    		"('" + cU.getNom() + "','" + op+ "')";
	    db.execSQL(stmt);
    }
    public void addOpinioReport(User cU, String op){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO reports (nom, opinio) VALUES "+
	    		"('" + cU.getNom() + "','" + op+ "')";
	    db.execSQL(stmt);
    }
    public void removeOpinioLike(User cU, String op){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "DELETE FROM likes WHERE opinio ='"+op+"'";
	    db.execSQL(stmt);
    }
    public void removeOpinioReport(User cU, String op){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "DELETE FROM reports WHERE opinio ='"+op+"'";
	    db.execSQL(stmt);
    }
    public boolean hasUserLikedOpinio(String cU, Opinio o) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM likes " +
				"WHERE nom ='"+cU+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(o.getId())){
	    	return true;
	    }
		return false;
	} 
    public boolean hasUserReportedOpinio(String cU, Opinio o) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM reports " +
				"WHERE nom ='"+cU+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(o.getId())){
	    	return true;
	    }
		return false;
	} 
    public void addFotoLike(User cU, String f){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO likes_fotos (nom, foto) VALUES "+
	    		"('" + cU.getNom() + "','" + f+ "')";
	    db.execSQL(stmt);
    }
    public void addFotoReport(User cU, String f){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "INSERT INTO reports_fotos (nom, foto) VALUES "+
	    		"('" + cU.getNom() + "','" + f+ "')";
	    db.execSQL(stmt);
    }
    public void removeFotoLike(User cU, String f){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "DELETE FROM likes_fotos WHERE foto ='"+f+"'";
	    db.execSQL(stmt);
    }
    public void removeFotoReport(User cU, String f){
    	SQLiteDatabase db = this.getWritableDatabase();
	    String stmt = "DELETE FROM reports_fotos WHERE foto ='"+f+"'";
	    db.execSQL(stmt);
    }
    public boolean hasUserLikedFoto(String cU, String f) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM likes_fotos " +
				"WHERE nom ='"+cU+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(f)){
	    	return true;
	    }
		return false;
	} 
    public boolean hasUserReportedFoto(String cU, String f) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM reports_fotos " +
				"WHERE nom ='"+cU+"'"; 
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> ms = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				ms.add(c.getString(1));
	           } while (c.moveToNext());
		} 
		c.close(); 
	    if(ms.contains(f)){
	    	return true;
	    }
		return false;
	}  
    
    public void deleteAllMonumentsVOQ() { 
		SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "DELETE FROM visitats " +
	 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM opinats " +
		 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM questionariats " +
		 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM likes " +
			 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM reports " +
		 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM likes_fotos " +
		 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM reports_fotos " +
		 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
		stmt = "DELETE FROM fotografiats " +
		 		"WHERE nom='" + getCurrentUser().getNom()+ "'"; 
		db.execSQL(stmt); 
	}
    
	public void createMonument(String name, int points, String image, double lat, double lng,
			String q, String ar, String aw1, String aw2,
			String q2, String ar2, String aw12, String aw22,
			String q3, String ar3, String aw13, String aw23) { 
	    SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "INSERT INTO monuments (name, points, image, lat, lng,q,ar,aw1,aw2," +
				"q2,ar2,aw12,aw22,q3,ar3,aw13,aw23) " +
	 		"VALUES ('" + name + "'," + points+ ",'"+ image+ "',"+lat+ ","+lng + 
	 		",'"+q+"','"+ar+"','"+aw1+"','"+aw2+
	 		"','"+q2+"','"+ar2+"','"+aw12+"','"+aw22+
	 		"','"+q3+"','"+ar3+"','"+aw13+"','"+aw23+"')"; 
	 	db.execSQL(stmt); 
	} 

	public void deleteMonument(int id) { 
		SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "DELETE FROM monuments " +
	 		"WHERE id=" + id+ ""; 
		db.execSQL(stmt); 
	} 

	public Cursor selectMonumentCursor(int id) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * " +
				"FROM monuments " +
				"WHERE id=" + id+ " "; 
		return db.rawQuery(stmt, null); 
	} 
	
	public Monument selectMonument(int id) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * " +
				"FROM monuments " +
				"WHERE id=" + id+ " "; 
		Cursor c = db.rawQuery(stmt, null); 
		if (c != null) {
            c.moveToFirst();
		}
		Monument m = new Monument();
		m.setId(Integer.parseInt(c.getString(0)));
		m.setNom(c.getString(1));
        m.setPunts(Integer.parseInt(c.getString(2)));
        m.setImatge(c.getString(3));
        m.setLat(Double.parseDouble(c.getString(4)));
        m.setLng(Double.parseDouble(c.getString(5)));
        m.setQ(new String[]{c.getString(6),c.getString(10),c.getString(14)});
        m.setAr(new String[]{c.getString(7),c.getString(11),c.getString(15)});
        m.setAw1(new String[]{c.getString(8),c.getString(12),c.getString(16)});
        m.setAw2(new String[]{c.getString(9),c.getString(13),c.getString(17)});


        return m;
	} 
	
	public Monument selectMonumentByName(String nom) { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * " +
				"FROM monuments " +
				"WHERE name='" + nom+ "' "; 
		Cursor c = db.rawQuery(stmt, null); 
		if (c != null) {
            c.moveToFirst();
		}
		Monument m = new Monument();
		m.setId(Integer.parseInt(c.getString(0)));
		m.setNom(c.getString(1));
        m.setPunts(Integer.parseInt(c.getString(2)));
        m.setImatge(c.getString(3));
        m.setLat(Double.parseDouble(c.getString(4)));
        m.setLng(Double.parseDouble(c.getString(5)));
        m.setQ(new String[]{c.getString(6),c.getString(10),c.getString(14)});
        m.setAr(new String[]{c.getString(7),c.getString(11),c.getString(15)});
        m.setAw1(new String[]{c.getString(8),c.getString(12),c.getString(16)});
        m.setAw2(new String[]{c.getString(9),c.getString(13),c.getString(17)});

        return m;
	} 

	public Cursor selectAllMonumentsCursor() { 
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * " +
				"FROM monuments " +
				"ORDER BY points DESC"; 
		return db.rawQuery(stmt, null); 
	} 
	
	public ArrayList<Monument> selectAllMonuments() { 
		ArrayList<Monument> monuments = new ArrayList<Monument>();
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT * "
				+ "FROM monuments " +
				"ORDER BY points DESC"; 
		Cursor c = db.rawQuery(stmt, null); 
		Monument m;
		if (c.moveToFirst()) { 
			do {
				m = new Monument();
				m.setId(Integer.parseInt(c.getString(0)));
				m.setNom(c.getString(1));
	            m.setPunts(Integer.parseInt(c.getString(2)));
	            m.setImatge(c.getString(3));
	            m.setLat(Double.parseDouble(c.getString(4)));
	            m.setLng(Double.parseDouble(c.getString(5)));
	            m.setQ(new String[]{c.getString(6),c.getString(10),c.getString(14)});
	            m.setAr(new String[]{c.getString(7),c.getString(11),c.getString(15)});
	            m.setAw1(new String[]{c.getString(8),c.getString(12),c.getString(16)});
	            m.setAw2(new String[]{c.getString(9),c.getString(13),c.getString(17)});
	            monuments.add(m);
	           } while (c.moveToNext());
		} 
		c.close(); 
	    
		return monuments;
	} 
	
	public void createTagMonument(String tag, Monument monument){
		SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "INSERT INTO tags_monuments (tag, monument) " +
		 		"VALUES ('" + tag + "','" + monument.getNom() + "')"; 
		db.execSQL(stmt); 
	}
	
	public ArrayList<Monument> getMonumentsTag(String tag){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Monument> monuments = new ArrayList<Monument>();
		String stmt =  "SELECT monument " +
				"FROM tags_monuments " +
				"WHERE tag= '" + tag+ "' ";
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> s = new ArrayList<String>();;
		if (c.moveToFirst()) { 
			do {
				s.add(c.getString(0));
	           } while (c.moveToNext());
		} 
		c.close();
		Monument m;
		for (int i=0;i<s.size();i++){
			stmt =  "SELECT * " +
					"FROM monuments " +
					"WHERE name='" + s.get(i)+ "' ";
			c = db.rawQuery(stmt, null); 
			if (c.moveToFirst()) { 
				do {
					m = new Monument();
					m.setId(Integer.parseInt(c.getString(0)));
					m.setNom(c.getString(1));
		            m.setPunts(Integer.parseInt(c.getString(2)));
		            m.setImatge(c.getString(3));
		            m.setLat(Double.parseDouble(c.getString(4)));
		            m.setLng(Double.parseDouble(c.getString(5)));
		            m.setQ(new String[]{c.getString(6),c.getString(10),c.getString(14)});
		            m.setAr(new String[]{c.getString(7),c.getString(11),c.getString(15)});
		            m.setAw1(new String[]{c.getString(8),c.getString(12),c.getString(16)});
		            m.setAw2(new String[]{c.getString(9),c.getString(13),c.getString(17)});
		            monuments.add(m);
		           } while (c.moveToNext());
			}
		}
		c.close();
		Collections.sort(monuments,new Comparator<Monument>(){
		    public int compare(Monument u1, Monument u2) {
		        return u2.getPunts()-u1.getPunts();
		    }
		});
		return monuments;
	}
	
	public ArrayList<String> getTagsMonument(Monument m){
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt =  "SELECT tag " +
				"FROM tags_monuments " +
				"WHERE monument= '" + m.getNom()+ "' ";
		Cursor c = db.rawQuery(stmt, null); 
		ArrayList<String> s = new ArrayList<String>();
		if (c.moveToFirst()) { 
			do {
				s.add(c.getString(0));
	           } while (c.moveToNext());
		} 
		c.close();
		
		return s;
	}
	
	public void createFiltre(String name) { 
		SQLiteDatabase db = this.getWritableDatabase();
		if(isFiltre(name)){
		}
		else{
			String stmt = "INSERT INTO filtres (filtre) " +
		 		"VALUES ('" + name + "')"; 
		 	db.execSQL(stmt); 
		}
	} 
	
	public void deleteFiltre(String name){
		SQLiteDatabase db = this.getWritableDatabase();
		if(isFiltre(name)){
			String stmt = "DELETE FROM filtres "+
					"WHERE filtre='"+name+"'";
			db.execSQL(stmt);
		}
		else{
		}
	}

	public void deleteFiltreById(int id) { 
		SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "DELETE FROM filtres " +
	 		"WHERE id=" + id+ ""; 
		db.execSQL(stmt); 
	} 
	
	public boolean isFiltre(String nom){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> f = getFiltresActius();
		return f.contains(nom);
	}
	
	public ArrayList<String> getFiltresActius(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> f = new ArrayList<String>();
		String stmt = "SELECT filtre FROM filtres";
		Cursor c = db.rawQuery(stmt, null); 
		if (c.moveToFirst()) { 
			do {
				f.add(c.getString(0));
	           } while (c.moveToNext());
		} 
		c.close(); 
		return f;
	}
	
	public void createDistancia(double d) { 
		SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "INSERT INTO distancies (distancia) " +
	 		"VALUES (" + d + ")"; 
	 	db.execSQL(stmt); 
	} 
	
	public void updateDistancia(double d){
		SQLiteDatabase db = this.getWritableDatabase();
		String stmt = "UPDATE distancies SET distancia="+d+" WHERE distancia='"+getDistancia()+"'";
	 	db.execSQL(stmt); 
	}
	
	public double getDistancia(){
		SQLiteDatabase db = this.getReadableDatabase();
		String stmt = "SELECT distancia FROM distancies";
		Cursor c = db.rawQuery(stmt, null); 
		c.moveToFirst();
		double dis = c.getDouble(0);
		c.close(); 
		return dis;
	}

}
