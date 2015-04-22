package edu.ub.scg.elgranturista;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.Parse;

public class ContaActivity extends Activity {
	ExternalDB eDB = new ExternalDB();
	InternalDB iDB = InternalDB.getInstance(this);
	User u;
	Activity activity;
	String avatar;
	Button cmbAfegirAmistat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conta);
		activity=this;
        try{
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
		u = (User)getIntent().getSerializableExtra("usr");
		

		
		final ImageView questionari = (ImageView)findViewById(R.id.imgAchievementQuest);
		final ImageView visita = (ImageView)findViewById(R.id.imgAchievementVisita);
		final ImageView camera = (ImageView)findViewById(R.id.imgAchievementCamera);
		questionari.setImageResource(R.drawable.grey_quest_logo);
		visita.setImageResource(R.drawable.grey_visita_logo);
		camera.setImageResource(R.drawable.grey_camera_logo);
		new Thread()
	         {
	             public void run(){ 
	            	 if(!iDB.getCurrentUser().getNom().equals(u.getNom())){
							if(!eDB.isUserAmic(iDB.getCurrentUser(),u)){
								activity.runOnUiThread(new Runnable(){
			          	 		    public void run(){
			          	 		    	cmbAfegirAmistat = (Button)findViewById(R.id.cmbAfegirAmistatConta);
			          	 		    	cmbAfegirAmistat.setEnabled(true);
			          	 		    	cmbAfegirAmistat.setVisibility(View.VISIBLE);
			    						cmbAfegirAmistat.setOnClickListener(new View.OnClickListener(){
			    							public void onClick (View v){
			    								eDB.addAmistat(iDB.getCurrentUser(),u);
			    								Bundle tempBundle = new Bundle();
			    								onCreate(tempBundle);
			    							}
			    						});
			          	 		    }
			          	 		});
								
							} else {
								activity.runOnUiThread(new Runnable(){
			          	 		    public void run(){
			          	 		    	cmbAfegirAmistat = (Button)findViewById(R.id.cmbAfegirAmistatConta);
			          	 		    	cmbAfegirAmistat.setEnabled(true);
			          	 		    	cmbAfegirAmistat.setVisibility(View.VISIBLE);
			          	 		    	cmbAfegirAmistat.setText("Elimina Amistat");
			    						cmbAfegirAmistat.setOnClickListener(new View.OnClickListener(){
			    							public void onClick (View v){
			    								eDB.removeAmistat(iDB.getCurrentUser(),u);
			    								Bundle tempBundle = new Bundle();
			    								onCreate(tempBundle);
			    							}
			    						});
			          	 		    }
			          	 		});
								
							}
						}else{
							activity.runOnUiThread(new Runnable(){
		          	 		    public void run(){
		          	 		    	Button cmbAfegirAmistat=(Button)findViewById(R.id.cmbAfegirAmistatConta);
		          	 		    	cmbAfegirAmistat.setEnabled(false);
		          	 		    	cmbAfegirAmistat.setVisibility(View.INVISIBLE);
		          	 		    }
		          	 		});
						}
					ArrayList<String> medalles=eDB.getMedallesUsuari(u);
				    Iterator<String> m = medalles.iterator();
					while(m.hasNext())
					{
					    String medalla = m.next();
					    if(medalla.equals("visita")){
					    	 activity.runOnUiThread(new Runnable(){
			          	 		    public void run(){
			          	 		    	visita.setImageResource(R.drawable.visita_logo);
			          	 		    }
			          	 		});
					    	
					    }else if(medalla.equals("camera")){
					    	 activity.runOnUiThread(new Runnable(){
			          	 		    public void run(){
			          	 		    	camera.setImageResource(R.drawable.camera_logo);
			          	 		    }
			          	 		});
					    	
					    }else if(medalla.equals("questionari")){
					    	 activity.runOnUiThread(new Runnable(){
			          	 		    public void run(){
			          	 		    	questionari.setImageResource(R.drawable.quest_logo);
			          	 		    }
			          	 		});	 
					    }
					}
					activity.runOnUiThread(new Runnable(){
          	 		    public void run(){
          	 		    	ProgressBar spin = (ProgressBar)findViewById(R.id.pbSincronitzar);
    					    spin.setVisibility(View.INVISIBLE);
    					    
          	 		    }
          	 		});
					
					
	             }
	         }.start();
		
		
		TextView lblNom = (TextView)findViewById(R.id.lblTitolConta);
		lblNom.setText(u.getNom());
		TextView lblPunts = (TextView)findViewById(R.id.lblPuntsConta);
		lblPunts.setText(u.getPunts() + " pts");
		
		
		
		ImageView img = (ImageView)findViewById(R.id.imgAvatarConta);
		avatar = u.getImatge();
		if (avatar == null || avatar.matches("tour_avatar")) {
			img.setImageResource(R.drawable.tour_avatar);		
		}
		else if (avatar.matches("draven")){
			img.setImageResource(R.drawable.draven);		
		}
		else if (avatar.matches("charmander")){
			img.setImageResource(R.drawable.charmander);
		}
		else if (avatar.matches("mario")){
			img.setImageResource(R.drawable.mario);
		}
		
		
		if(iDB.getCurrentUser().getNom().equals(u.getNom())){
			img.setOnClickListener(new View.OnClickListener(){
				public void onClick (View v){
					class RankingExecutar extends AsyncTask<String, Void, String> {
	    				ProgressDialog pd;
	    				
	    		        protected String doInBackground(String... urls) {
	    					Intent i = new Intent(ContaActivity.this,AvatarsActivity.class);
	    					startActivity(i);
	    					finish();
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
					new RankingExecutar().execute();
				}
			});
		}
		
		Button cmbRanking = (Button)findViewById(R.id.cmbRankingConta);
		cmbRanking.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				class RankingExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					Intent i = new Intent(ContaActivity.this,RankingActivity.class);
    					i.putExtra("opt",0);
    					i.putExtra("usr",u.getNom());
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
				new RankingExecutar().execute();
			}
		});
		
		Button cmbAmics = (Button)findViewById(R.id.cmbAmicsConta);
		cmbAmics.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				class AmicsExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					Intent i = new Intent(ContaActivity.this,RankingActivity.class);
    					i.putExtra("usr", u.getNom());			
    					i.putExtra("opt",1);
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
				new AmicsExecutar().execute();
			}
		});
		
		Button cmbLlocs = (Button)findViewById(R.id.cmbLlocsVisitatsConta);
		cmbLlocs.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				class LlocsVisitatsExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					Intent i = new Intent(ContaActivity.this,MonumentsVisitatsActivity.class);
    					i.putExtra("usr", u.getNom());		
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
				new LlocsVisitatsExecutar().execute();
			}
		});
		
		questionari.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				
				
				builder.setTitle("Resopn un questionari");
				
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
		           	}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		visita.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				
				
				builder.setTitle("Visita un monument");
				
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
		           	}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		camera.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				
				
				builder.setTitle("Fes una foto");
				
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
		           	}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
	}

}

