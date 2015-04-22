package edu.ub.scg.elgranturista;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LogInActivity extends Activity{
	Activity activity;
	InternalDB iDB = InternalDB.getInstance(this);
    ExternalDB eDB = new ExternalDB();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if(!isNetworkAvailable()){
        	showInternetDisabledAlertToUser();
		} 
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        try{
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }

        
        if(eDB.getCurrentUser()!=null){
			Intent i = new Intent(LogInActivity.this,MainMenuActivity.class);
			startActivity(i);
		}
		Button cmbLogIn = (Button)findViewById(R.id.cmbLogOutMenu);
		cmbLogIn.setOnClickListener(new View.OnClickListener(){
			
			public void onClick (View v){
				class LoginExecutar extends AsyncTask<String, Void, String> {
    				ProgressDialog pd;
    				@Override
    		        protected String doInBackground(String... urls) {
    					EditText txtNom = (EditText)findViewById(R.id.txtNomLogIn);
    				    EditText txtPass = (EditText)findViewById(R.id.txtPassLogIn);
    				    String nom = txtNom.getText().toString();
    				    String pass = txtPass.getText().toString();
    					login(nom.toLowerCase(),pass);		            
    					
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
				new LoginExecutar().execute();
			}
		        
			
		});
		
		Button cmbSignUp = (Button)findViewById(R.id.cmbSignUpMenu);
		cmbSignUp.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				if(!isNetworkAvailable()){
					Toast.makeText(getApplicationContext(), "Network not found", Toast.LENGTH_SHORT).show();
				} else {
					Intent i = new Intent(LogInActivity.this,SignUpActivity.class);
					startActivity(i);
				}
			}
		});
		
		Button cmbOffline = (Button)findViewById(R.id.cmbOfflineAccess);
		cmbOffline.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){			
				Intent i = new Intent(LogInActivity.this,MainMenuActivity.class);
				i.putExtra("Offline", 1);
				startActivity(i);
			}
		});
	}
	
	private void showInternetDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Network access is disabled in your device. Would you like to enable it?")
        .setCancelable(false)
        .setPositiveButton("Enable",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callInternetSettingIntent = new Intent(
                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(callInternetSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
		
	}

	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void login(String nom, String password) {
		// TODO Auto-generated method stub
		try {
			ParseUser.logIn(nom, password);
			
			//Set current user into internal DB.
			ParseUser pu = ParseUser.getCurrentUser();
			User u = new User(pu.getObjectId(),
	    			pu.getString("username"),
	    			pu.getInt("Punts"), 
	    			pu.getString("Avatar"));
			iDB.setCurrentUser(u.getNom(), u.getPunts(),u.getImatge());
			
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("username", u.getNom());
			List<ParseUser> uQ = query.find();
			for(int i=0;i<uQ.get(0).getJSONArray("MonumentsVisitats").length();i++){
				try {
					iDB.addMonumentVisitat(iDB.getCurrentUser(), uQ.get(0).getJSONArray("MonumentsVisitats").getString(i));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

        	for(int i=0;i<uQ.get(0).getJSONArray("MonumentsOpinats").length();i++){
        		try {
					iDB.addMonumentOpinats(iDB.getCurrentUser(), uQ.get(0).getJSONArray("MonumentsOpinats").getString(i));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	
        	for(int i=0;i<uQ.get(0).getJSONArray("QuestionarisCompletats").length();i++){
        		try {
					iDB.addMonumentQuestionariats(iDB.getCurrentUser(), uQ.get(0).getJSONArray("QuestionarisCompletats").getString(i));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	for(int i=0;i<uQ.get(0).getJSONArray("MonumentsFotografiats").length();i++){
        		try {
					iDB.addMonumentFotografiats(iDB.getCurrentUser(), uQ.get(0).getJSONArray("MonumentsFotografiats").getString(i));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	ArrayList<Opinio>ops=eDB.getOpinionsWhereUserLiked();
        	for(int i=0;i<ops.size();i++){
        		iDB.addOpinioLike(iDB.getCurrentUser(), ops.get(i).getId());
        	}
        	ops=eDB.getOpinionsWhereUserReported();
        	for(int i=0;i<ops.size();i++){
        		iDB.addOpinioReport(iDB.getCurrentUser(), ops.get(i).getId());
        	}
        	ArrayList<String>fts=eDB.getFotosUserHasLiked();
        	for(int i=0;i<fts.size();i++){
        		iDB.addFotoLike(iDB.getCurrentUser(), fts.get(i));
        	}
        	fts=eDB.getFotosUserHasReported();
        	for(int i=0;i<fts.size();i++){
        		iDB.addFotoReport(iDB.getCurrentUser(), fts.get(i));
        	}
			Intent in = new Intent(getApplicationContext(),MainMenuActivity.class);
			in.putExtra("Offline", 0);
			startActivity(in);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(!isNetworkAvailable()){ 
				activity.runOnUiThread(new Runnable(){ 
					public void run(){ 
						Toast.makeText(getApplicationContext(), "Network not found", Toast.LENGTH_SHORT).show()
						;}
					}); 
				} 
			else { 
				activity.runOnUiThread(new Runnable(){ 
					public void run(){ 
						Toast.makeText(getApplicationContext(), "Incorrect User/Pass", Toast.LENGTH_SHORT).show()
						;}
					});
			}
		}
	}

	private void loginSuccessful() {
		// TODO Auto-generated method stub
		Intent i = new Intent(LogInActivity.this,MainMenuActivity.class);
		startActivity(i);
	}
	
	@Override
	public void onBackPressed() {
	}
	
	private void loginUnSuccessful() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Incorrect User/Pass", Toast.LENGTH_SHORT).show();
	}
}
