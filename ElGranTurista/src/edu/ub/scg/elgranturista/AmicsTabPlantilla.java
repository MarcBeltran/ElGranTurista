package edu.ub.scg.elgranturista;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
public class AmicsTabPlantilla extends Fragment {
	ExternalDB db = new ExternalDB();
	String filtre, usrName;
	List<User> Users;
	View rootView;
	Activity activity;
	
    public AmicsTabPlantilla(String string,String name) {
		filtre = string;
		usrName = name;
	}
    public AmicsTabPlantilla(){
    	
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		activity=this.getActivity();
		
    	rootView = inflater.inflate(R.layout.amics_tab, container, false);
    	
    	
    	new Thread(){
			public void run(){
		    	if (filtre == "General" || filtre == null) {
		    		Users =  db.getAmistatsUser(db.getUser(usrName));
		    	}
		    	else {
		    		Users =  db.getAmistatsUserAmbPuntsFiltrats(db.getUser(usrName),filtre);
		    	}
		    	activity.runOnUiThread(new Runnable(){
	      	 		 public void run(){
						ListView llista = (ListView) rootView.findViewById(R.id.userList);
						UserAdapter tmp = new UserAdapter(activity, R.layout.user, Users, 0);
						llista.setAdapter(tmp);
						llista.setOnItemClickListener(new OnItemClickListener() {
					        @Override
					        public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
					                long arg3) {
					        	class ContaExecutar extends AsyncTask<String, Void, String> {
			        				ProgressDialog pd;
			        				@Override
			        		        protected String doInBackground(String... urls) {
			        					String nomUsr = ((TextView) arg1.findViewById(R.id.nom)).getText().toString();
							        	User u = db.getUser(nomUsr);
						        		Intent i = new Intent(getActivity(),ContaActivity.class);
						        		i.putExtra("usr", u);					
						        		startActivity(i);
			        		            return null;
			        		        }
			        		 
			        		        
			        		        @Override
			        		        protected void onPreExecute() {
			        		        	pd= new ProgressDialog(rootView.getContext());
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
						ProgressBar spin= (ProgressBar)activity.findViewById(R.id.pbSpin);
      	 		    	spin.setVisibility(View.INVISIBLE);
      	 		    	llista.setVisibility(View.VISIBLE);
	      	 		 }
		    	});
			}
    	}.start();
        return rootView;
    }
}
