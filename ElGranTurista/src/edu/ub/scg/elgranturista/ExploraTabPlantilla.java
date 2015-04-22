package edu.ub.scg.elgranturista;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

 
@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ExploraTabPlantilla extends Fragment {
	Activity activity;
	InternalDB db= InternalDB.getInstance(getActivity());
	ExternalDB eDB = new ExternalDB();
	Location l;
	List<Monument> monuments;
	int opcio;
	
	private final float[] distanceArray = new float[1];
	
	public ExploraTabPlantilla(int i) {
		// TODO Auto-generated constructor stub
		opcio = i;
	}
	
	public ExploraTabPlantilla(){
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 activity=this.getActivity();
		
    	View rootView = inflater.inflate(R.layout.explora_tab, container, false);
    	ProgressBar spin= (ProgressBar)rootView.findViewById(R.id.pbSpin);
    	TextView lblPunts = (TextView)rootView.findViewById(R.id.punts);
        
    	if (opcio==0){
    		generarMonuments();
    	}
    	if (opcio==1){
    		generarMonuments();
    		ArrayList<Float> ps = eDB.getMonumentsPuntsUsers();
    		Collections.sort(monuments,new Comparator<Monument>(){
    		    public int compare(Monument u1, Monument u2) {
    		        return (int) -(u2.getId()-u1.getId());
    		    }
    		});
    		for(int i=0;i<ps.size();i++){
    			monuments.get(i).setPuntsUsers(ps.get(i));
    		}
    		Collections.sort(monuments,new Comparator<Monument>(){
    		    public int compare(Monument u1, Monument u2) {
    		        return (int) (round(u2.getPuntsUsers(),1)*10-round(u1.getPuntsUsers()*10,1));
    		    }
    		});
    	}
    	
		ListView llista = (ListView) rootView.findViewById(R.id.listMonuments);
		
		MonumentAdapter tmp;
		
		if (opcio==0){
			tmp = new MonumentAdapter(this.getActivity(), R.layout.monument, monuments, 0);
			lblPunts.setText("Punts");
		}
		else{
			tmp = new MonumentAdapter(this.getActivity(), R.layout.monument, monuments, 2);
			lblPunts.setText("Punts/5");
		}
		
		llista.setAdapter(tmp);
		
		
		llista.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
	                long arg3) {
	        	class VistaMonumentExecutar extends AsyncTask<String, Void, String> {
	        				ProgressDialog pd;
	        				@Override
	        		        protected String doInBackground(String... urls) {
	        						String nomMonu = ((TextView) arg1.findViewById(R.id.nom)).getText().toString();
		        		        	Monument m = db.selectMonumentByName(nomMonu);
		        		        	
		        					Intent i = new Intent(getActivity(),VistaMonumentActivity.class);
		        					i.putExtra("mnt", m);					
		        					startActivity(i);
									
	        		            return null;
	        		        }
	        		 
	        		        
	        		        @Override
	        		        protected void onPreExecute() {
	        		        	pd= new ProgressDialog(activity);
	        		        	pd.setTitle(getString(R.string.processing));
	        					pd.setMessage(getString(R.string.pleasewait));
	        					pd.setCancelable(false);
	        					pd.setIndeterminate(true);
	        		    		pd.show();
	        		        }
	        		        @Override
	        		        protected void onPostExecute(String result) {
	        		        	pd.dismiss();
	        		        }
	        	}
	        	new VistaMonumentExecutar().execute();
	        }
	    });
		
		
    	spin.setVisibility(View.INVISIBLE);
    	llista.setVisibility(View.VISIBLE);
        return rootView;
    }
	
	
	public void generarMonuments(){
		if (opcio==0) {
			double lat,lng;
	
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
			monuments = new ArrayList<Monument>();
			if(!filtres.isEmpty()){
				List<Monument> volMnts = new ArrayList<Monument>();
				Iterator<String> it = filtres.iterator();
				while(it.hasNext())
				{
				    volMnts = db.getMonumentsTag(it.next());
				    Iterator<Monument> m = volMnts.iterator();
					while(m.hasNext())
					{
					    Monument mnt = m.next();
					    if(!monuments.contains(mnt)){
						    lat = mnt.getLat();
						    lng = mnt.getLng();
						    if(l!=null){
						    	l.distanceBetween(l.getLatitude(),l.getLongitude(),lat,lng,distanceArray);
						    } else{
						    	l.distanceBetween(0,0,0,0,distanceArray);
						    }
						    float d = distanceArray[0]/1000;
						    Double dFiltres = db.getDistancia();
						    if(10 < dFiltres&&dFiltres < 1000&&dFiltres!=-1){
						    	dFiltres = dFiltres/1000;
						    }
						    if(d <= dFiltres || dFiltres == -1){
						    	monuments.add(mnt);
						    }
					    }
					}
				}
			} else {
				ArrayList<Monument> mnuts = db.selectAllMonuments();
				Iterator<Monument> it = mnuts.iterator();
				while(it.hasNext())
				{
				    Monument mnt = it.next();
				    lat = mnt.getLat();
				    lng = mnt.getLng();
				    if(l!=null){
				    	l.distanceBetween(l.getLatitude(),l.getLongitude(),lat,lng,distanceArray);
				    } else{
				    	l.distanceBetween(0,0,0,0,distanceArray);
				    }				    float d = distanceArray[0]/1000;
				    Double dFiltres = db.getDistancia();
				    if(10<dFiltres&&dFiltres < 1000&&dFiltres!=-1){
				    	dFiltres = dFiltres/1000;
				    }
				    if(d <= dFiltres || dFiltres == -1){
				    	monuments.add(mnt);
				    }
				}
			}
		}
		if (opcio==1) {
			monuments = db.selectAllMonuments();
		}
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}


