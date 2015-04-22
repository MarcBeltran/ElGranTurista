package edu.ub.scg.elgranturista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class VistaMonumentActivity extends Activity implements LocationListener {
	private final float[] distanceArray = new float[1];
	InternalDB iDB =InternalDB.getInstance(this);
	float dis;
	Monument m;
	ExternalDB eDB = new ExternalDB();
	Button cmbSincronitzar;
	boolean isSincronitzat = false;
	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("VistaMonumentActivity","onCreateInit");
		 activity=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vista_monument);
 		
		ParseObject.registerSubclass(ImatgeCongrats.class);
		
		try{
          	
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
 		
		m = (Monument)getIntent().getSerializableExtra("mnt");
		TextView txtNom = (TextView)findViewById(R.id.lblNomMonument);
		txtNom.setText(m.getNom());
		int resID = getResources().getIdentifier(m.getImatge(), "drawable",getPackageName());


		RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl);
		rl.setBackgroundResource(resID);

	
	
 		Button cmbFotos = (Button)findViewById(R.id.cmbFotos);
 		cmbFotos.setOnClickListener(new View.OnClickListener(){
 			
 			public void onClick (View v){
 				class FotoExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					Intent i = new Intent(VistaMonumentActivity.this,VistaFotosActivity.class);
     					i.putExtra("mon",m.getNom());
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
				new FotoExecutar().execute();
 					
 			}
 		});
 		
 		Button cmbOpinions = (Button)findViewById(R.id.cmbOpinions);
 		cmbOpinions.setOnClickListener(new View.OnClickListener(){
 			public void onClick (View v){
 				class OpinionsExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					Intent i = new Intent(VistaMonumentActivity.this,OpinionsActivity.class);
     					i.putExtra("mon",m.getNom());
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
				new OpinionsExecutar().execute();
 					
 			}
 		});
 		
 		cmbSincronitzar = (Button)findViewById(R.id.cmbSincronitzar);
 		
 		
 		cmbSincronitzar.setOnClickListener(new View.OnClickListener(){
 			public void onClick (View v){
 				class SincronitzaExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					if(dis>100&&!isSincronitzat){
    						activity.runOnUiThread(new Runnable(){
    	          	 		    public void run(){
    	          	 		    	Toast.makeText(getApplicationContext(), getString(R.string.noCercaMonumento), Toast.LENGTH_SHORT).show();
    	    	 				
    	          	 		    }
    	          	 		});
	 					} else {
    	 					Intent i = new Intent(VistaMonumentActivity.this,CongratulationsActivity.class);
    	 					i.putExtra("mnt", m);	
    	 					startActivity(i);
	 					}
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
				new SincronitzaExecutar().execute();
 				
 				
 			}
 		});
 		
 		
 		
	     new Thread()
         {
             public void run(){
            	 
            	 if(iDB.hasMonumentBeenVisitat(iDB.getCurrentUser(),m)){
            		  activity.runOnUiThread(new Runnable(){
          	 		    public void run(){
          	 		    	cmbSincronitzar.setText("Opcions");
          	 		    }
          	 		});
          			
          			isSincronitzat = true;
          			eDB.afegirMedallaUsuari("visita",iDB.getCurrentUser());
          		}
         		
         		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
             	
             	Location l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
         		
             	if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
                 	l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
                 }else{
                 	l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
                 }
         		if(l==null){
         			l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
         		}
         		
         		

         		
         		Location.distanceBetween(m.getLat(), m.getLng(), l.getLatitude(), l.getLongitude(), distanceArray);
         		dis = distanceArray[0];
         		      
         }
         }.start();
		
         Log.i("VistaMonumentActivity","onCreateEnd");
		//finish onCreate
	}


	@Override
	protected void onResume() {
		super.onResume();
		new Thread(){
			public void run(){
				if(iDB.hasMonumentBeenVisitat(iDB.getCurrentUser(),m)){
					activity.runOnUiThread(new Runnable(){
          	 		    public void run(){
          	 		    	cmbSincronitzar.setText("Opciones");
          	 		    }
          	 		});
					
				}
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vista_monument, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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

}
