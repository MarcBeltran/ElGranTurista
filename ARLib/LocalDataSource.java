package com.paar.ch9;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;
    
    public LocalDataSource(Resources res) {
        if (res==null) throw new NullPointerException();
        
        createIcon(res);
    }
    
    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
        icon=BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
    }
    
    public List<Marker> getMarkers() {
    	
    	
        Marker atl = new IconMarker("HOUSE",41.411217,2.178797, 10, Color.RED, icon);
        Marker atl1 = new IconMarker("SF",41.403611,2.174444, 10, Color.RED, icon);
        Marker atl2= new IconMarker("TORRE",41.403333,2.189444, 10, Color.RED, icon);
        Marker atl3 = new IconMarker("UB",41.386553,2.163986, 10, Color.RED, icon);
        Marker atl4 = new IconMarker("PARC",41.413611,2.152778, 10, Color.RED, icon);

        cachedMarkers.add(atl);
        cachedMarkers.add(atl1);
        cachedMarkers.add(atl2);
        cachedMarkers.add(atl3);
        cachedMarkers.add(atl4);

        return cachedMarkers;
    }
}