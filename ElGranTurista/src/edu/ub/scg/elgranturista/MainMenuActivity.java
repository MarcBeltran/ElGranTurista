package edu.ub.scg.elgranturista;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;

public class MainMenuActivity extends Activity {
	ExternalDB externalDB = new ExternalDB();
	InternalDB internalDB = InternalDB.getInstance(this);
	LocationManager lm;
	int offline;
    
	@Override	
    protected void onCreate(Bundle savedInstanceState) {
		final Activity activity=this;
		
        super.onCreate(savedInstanceState);
        
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);  
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
        	showGPSDisabledAlertToUser();
        }
        
        try{
            Parse.initialize(activity, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
        
        new Thread()
        {
            public void run(){
		        
		
		        //Nomes creem la base de dades el primer cop que l'instalem
		        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		        if(!prefs.getBoolean("firstTime", false)) {
		            db();
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putBoolean("firstTime", true);
			        editor.commit();
		        }
		     }
        }.start();
        
		offline = (int)getIntent().getIntExtra("Offline", 0);
        setContentView(R.layout.activity_main_menu);
        Button cmbExplora = (Button)findViewById(R.id.cmbExplora);
		cmbExplora.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				Intent i = new Intent(MainMenuActivity.this,MainExploraActivity.class);
				i.putExtra("Offline", offline);
				startActivity(i);
			}
		});
		
		Button cmbConta = (Button)findViewById(R.id.cmbConta);
		if(offline==1){
			cmbConta.setEnabled(false);
			cmbConta.setBackground(this.getResources().getDrawable(R.drawable.shape_disabled)); 

		}
		cmbConta.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				class ContaExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					Intent i = new Intent(MainMenuActivity.this,ContaActivity.class);
    					i.putExtra("usr",internalDB.getCurrentUser());
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
				new ContaExecutar().execute();
			}
				
			
		});
		
		Button cmbRanking = (Button)findViewById(R.id.cmbRanking);
		if(offline==1){
			cmbRanking.setEnabled(false);
		    cmbRanking.setBackground(this.getResources().getDrawable(R.drawable.shape_disabled)); 
		}
		cmbRanking.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				Intent i = new Intent(MainMenuActivity.this,RankingActivity.class);
				i.putExtra("opt",2);
				startActivity(i);
			}
		});
		Button cmbLogOut = (Button)findViewById(R.id.cmbLogOutMenu);
		if(offline==1){
			cmbLogOut.setText("Log In");
		}
		cmbLogOut.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				Intent i = new Intent(MainMenuActivity.this,LogInActivity.class);
				if(offline!=1){
					externalDB.logOut();
					internalDB.deleteAllMonumentsVOQ();
					internalDB.deleteCurrentUser();
				}
				startActivity(i);
			}
		});
		Button cmbInfo = (Button)findViewById(R.id.cmdInfo);
		cmbInfo.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				Intent i = new Intent(MainMenuActivity.this,InformacionActivity.class);
				startActivity(i);
			}
		});
    }


	public void db() {    	
		int i=1;
		InputStream is = getResources().openRawResource(R.raw.dades);
		Scanner sc = new Scanner(is); 
		while (sc.hasNext()) { 
			String line = sc.nextLine(); 
			String[] parts = line.split(",");
			internalDB.createMonument(parts[0], Integer.parseInt(parts[1]), parts[2], 
					 Double.parseDouble(parts[3]), Double.parseDouble(parts[4]),
					 parts[5],parts[6],parts[7],parts[8],
					 parts[9],parts[10],parts[11],parts[12],
					 parts[13],parts[14],parts[15],parts[16]);
			int len = parts.length;
			for(int j=17;j<len;j++){
				internalDB.createTagMonument(parts[j], internalDB.selectMonument(i));
			}
			i++;
		} 
		sc.close();
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 	
		internalDB.createDistancia(-1);
		
	}


	@Override
	public void onBackPressed() {
	}
	
	@Override
	public void onDestroy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("parseInit", false);
        editor.commit();
        super.onStop();
	}
	
	private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.gpsmessage))
        .setCancelable(false)
        .setPositiveButton(getString(R.string.gpsactivar),
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }
	
	
}
