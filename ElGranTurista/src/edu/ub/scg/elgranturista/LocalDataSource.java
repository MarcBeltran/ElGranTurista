package edu.ub.scg.elgranturista;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.paar.ch9.DataSource;
import com.paar.ch9.IconMarker;
import com.paar.ch9.Marker;

/*
 * Aquesta classe genera els markers de la realitat augmentada
 * apartir dels monuments de la base de dades interna.
 */
public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    Resources res;
    
    InternalDB db;
    
    @SuppressLint("Instantiatable")
	public LocalDataSource(Resources res, InternalDB db) {
        if (res==null) throw new NullPointerException();
        this.db = db;
        this.res = res;
    }
   
    
    public List<Marker> getMarkers() {
    	double lat,lng;
		
		List<String> filtres = db.getFiltresActius();
		
		List<Monument> mnts;
		
		if(!filtres.isEmpty()){
			mnts = new ArrayList<Monument>();
			List<Monument> volMnts = new ArrayList<Monument>();
			Iterator<String> it = filtres.iterator();
			while(it.hasNext())
			{
			    volMnts = db.getMonumentsTag(it.next());
			    Iterator<Monument> m = volMnts.iterator();
				while(m.hasNext())
				{
				    Monument mnt = m.next();
				    if(!mnts.contains(mnt)){
				    	mnts.add(mnt);
					    lat = mnt.getLat();
					    lng = mnt.getLng();
					    int resID = res.getIdentifier(mnt.getImatge(),"drawable","edu.ub.scg.elgranturista");
					    Bitmap icon=null;
				    	icon = BitmapFactory.decodeResource(res,resID);
				    	Marker mark = new IconMarker(mnt.getNom(),lat,lng, 10, Color.RED, icon);
					    cachedMarkers.add(mark);
					    
				    }
				}
			}
		} else {
			mnts = db.selectAllMonuments();
			Iterator<Monument> it = mnts.iterator();
			while(it.hasNext())
			{
			    Monument mnt = it.next();
			    lat = mnt.getLat();
			    lng = mnt.getLng();
			    int resID = res.getIdentifier(mnt.getImatge(),"drawable","edu.ub.scg.elgranturista");
			    Bitmap icon=null;
		    	icon = BitmapFactory.decodeResource(res,resID);
		    	Marker mark = new IconMarker(mnt.getNom(),lat,lng, 10, Color.RED, icon);
			    cachedMarkers.add(mark);
			}
		}
    	
        
        return cachedMarkers;
    }
}