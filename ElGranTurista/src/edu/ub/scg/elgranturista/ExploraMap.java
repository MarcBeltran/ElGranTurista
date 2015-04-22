package edu.ub.scg.elgranturista;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
 * En aquest fragment hi han dues classes:
 * 	- Markers, objectes que es mostren en el mapa
 * 	- CustomInfoWindowAdapter, ens permet visualitzar correctament les dades de cada monument
 * 	un cop es fa click en el marker en concret
 * 
 * La sequencia d'inicialització del mapa és: 
 * 
 * 	- Agafem la vista on es crearà el mapa
 * 	- Creem els markers
 * 	- Afegim el mapa
 * 	- Afegim els markers al mapa
 * 
 * Per fer tot això es comproba si tenim googlemaps instalat.
 * 
 * OBSERVACIÓ 1:
 * 
 * A totes les classes que necessiten mostrar la distancia entre la posició actual
 * de l'usuari i els diferents monuments es pot veure la seqüència següent:
 * 
 * 
 * 
 * if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
	        	l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
	        }else{
	        	l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
	        }
			if(l==null){
				l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
			}

 * 
 * Això serveix per agafar el provider disponible. També es fa una comprobació
 * al arrencar l'aplicació per mirar si disposem d'internet i GPS.
 * 
 * OBSERVACIÓ 2:
 * 
 * El mètode getLastKnownLocation ens ha donat problemes ja que si s'obre la app
 * en un lloc en concret i obtenim la posició correctament i després canviem de lloc
 * aquesta llocalització canvia només avegades.
 * 
 * Per sol·lucionar aquest problema hem intentat utilitzar el següent:
 * 
 *  	requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
 *  
 * Aquesta comanda teòricament realitza una petició a LocationManager per tal que
 * actualitzi la posició actual del dispositiu (també ho hem probat amb NETWORK_PROVIDER).
 * 
 * Aquest problema és difícil de solucionar pel simple fet que necessites canviar de localització
 * per mirar si funciona, retocar el codi i tornar a comprobar si s'ha arreglat el problema.
 * 
 * Per la versió final de la app tenim pensat crear una classe que s'encarregui de gestionar 
 * totes les peticions de canvi de localització i que informi si s'ha canviat o no de posició.
 * D'aquesta manera cada classe que necessiti d'aquest servei només haurà de crear un objecte
 * i fer l'actualització. 
 * 
 */

public class ExploraMap extends Fragment implements LocationListener,OnInfoWindowClickListener{

	
	InternalDB db = InternalDB.getInstance(getActivity());
	ExternalDB eDB = new ExternalDB();
	ArrayList<Monument> mnts;
	Location l;
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("@@##");
	private final float[] distanceArray = new float[1];
	
	private class Marker {
		
		private String nom;
		
		private int pts;
		private LatLng loc;
		
		public Marker(String nom, LatLng loc, int i) {
			super();
			this.nom = nom;
			this.pts = i;
			this.loc = loc;
		}
		public LatLng getLoc() {
			return loc;
		}
		public void setLoc(LatLng loc) {
			this.loc = loc;
		}
		public String getNom() {
			return nom;
		}
		public void setNom(String nom) {
			this.nom = nom;
		}
		public int getPts() {
			return pts;
		}
		public void setPts(int pts) {
			this.pts = pts;
		}
		
	}
	
	
	private class CustomInfoWindowAdapter implements InfoWindowAdapter {
		 
        private View view;
 
        public CustomInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_windowinfo,
                    null);
        }

		@Override
		public View getInfoContents(
				com.google.android.gms.maps.model.Marker marker) {
			
			Monument m = db.selectMonumentByName(marker.getTitle());
			final ImageView image = ((ImageView) view.findViewById(R.id.badge));
			
			
			int resID = getResources().getIdentifier(m.getImatge(),"drawable",getActivity().getPackageName());
			image.setImageResource(resID);
			
			final String title = marker.getTitle();
			final String snippet = marker.getSnippet();
			final TextView titleUi = ((TextView) view.findViewById(R.id.title));
			final TextView distanceUi = ((TextView) view.findViewById(R.id.distance));
			LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
	        	l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
	        }else{
	        	l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
	        }
			if(l==null){
				l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
			}
			Location.distanceBetween(l.getLatitude(),l.getLongitude(),marker.getPosition().latitude, marker.getPosition().longitude, distanceArray);
			float dis = distanceArray[0];
			String textStr;
		    if (dis<1000.0) {
		        textStr = DECIMAL_FORMAT.format(dis) + " m";          
		    } else {
		        double d=dis/1000.0;
		        textStr =  DECIMAL_FORMAT.format(d) + "km";
		    }
		    distanceUi.setText(textStr);
			
			if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }
            
			return view;
		}

		@Override
		public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
			return null;
		}
 
        
    }
	

	private MapView mapView;
	private GoogleMap map;
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	
	
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.explora_map, container, false);
		
 		// Gets the MapView from the XML layout and creates it
		
		setHasOptionsMenu(true);
		
		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		
		createMarkers();
		
		setUpMap();
		
		
		addMarkers();
	
 
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.exploramap, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}

	private void setUpMap() {
        
        if(isGoogleMapsInstalled()){
        	
        		map = mapView.getMap();
        		map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        		MapsInitializer.initialize(this.getActivity());
            
            	map.setOnInfoWindowClickListener(this);
            	map.setMyLocationEnabled(true);
    
            	LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            	l = null;
            	//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this); //Corregint l'actualització de la posició actual
            	if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
    	        	l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
    	        }else{
    	        	l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
    	        }
    			if(l==null){
    				l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
    			//}
    			
            	onLocationChanged(l);
    			}
        }
	}

 
	@Override
	public void onResume() {
		mapView.onResume();
		
		LatLng latlng = new LatLng(l.getLatitude(),l.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		map.animateCamera(CameraUpdateFactory.zoomTo(18));
		
		super.onResume();
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	public void onPause(){
		mapView.onPause();
		super.onPause();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		map.animateCamera(CameraUpdateFactory.zoomTo(18));
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	public boolean isGoogleMapsInstalled()
	 {
	     try
	     {
	         ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
	         return true;
	     } 
	     catch(PackageManager.NameNotFoundException e)
	     {
	         return false;
	     }
	 }
	
	private void createMarkers(){
		double lat,lng;
		
		if(!markers.isEmpty()){
			map.clear();
			markers.clear();
		}
		
		LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
        	l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
        }else{
        	l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
        }
		if(l==null){
			l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
		}
		
		
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
					    l.distanceBetween(l.getLatitude(),l.getLongitude(),lat,lng,distanceArray);
					    float d = distanceArray[0]/1000;
					    Double dFiltres = db.getDistancia();
					    if(10<dFiltres&&dFiltres < 1000&&dFiltres!=-1){
					    	dFiltres = dFiltres/1000;
					    }
					    if(d <= dFiltres||dFiltres == -1){
						    LatLng loc = new LatLng(lat,lng);
						    Marker mark = new Marker(mnt.getNom(),loc,mnt.getPunts());
						    markers.add(mark);
					    }
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
			    l.distanceBetween(l.getLatitude(),l.getLongitude(),lat,lng,distanceArray);
			    float d = distanceArray[0]/1000;
			    Double dFiltres = db.getDistancia();
			    if(10<dFiltres&&dFiltres < 1000&&dFiltres!=-1){
			    	dFiltres = dFiltres/1000;
			    }
			    if(d <= dFiltres||dFiltres ==-1){
				    LatLng loc = new LatLng(lat,lng);
				    Marker mark = new Marker(mnt.getNom(),loc,mnt.getPunts());
				    markers.add(mark);
			    }
			}
		}
	}
	
	
	private void addMarkers() {
		
		ArrayList<String> mntVisitats = eDB.getMonumentsVisitatsUsuari(db.getCurrentUser());
		BitmapDescriptor bIcon = null;
		Iterator<Marker> it = markers.iterator();
		while(it.hasNext())
		{
		    Marker m = it.next();
		    if(mntVisitats.contains(m.getNom())){
		    	bIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
		    } else {
		    	bIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
		    }
		    map.addMarker(new MarkerOptions()
			.title(m.getNom())
			.snippet(m.getPts() + " punts")
			.icon(bIcon)
			.position(m.getLoc()));
		    
		}
		
	}	
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.ar:
	            	Intent i = new Intent(getActivity(),AR.class);
	            	startActivity(i);
	                break;
	        }
	        return true;
	    }

	@Override
	public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
		Monument m = db.selectMonumentByName(marker.getTitle());
		Intent i = new Intent(getActivity(),VistaMonumentActivity.class);
		i.putExtra("mnt", m);
		startActivity(i);
		
	}
	
 
}	

