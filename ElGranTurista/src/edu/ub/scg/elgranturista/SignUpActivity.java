package edu.ub.scg.elgranturista;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SignUpActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		Button cmbSignUp = (Button)findViewById(R.id.cmbLogOutMenu);
		cmbSignUp.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){

			    EditText txtNom = (EditText)findViewById(R.id.txtNomLogIn);
			    EditText txtPass = (EditText)findViewById(R.id.txtPassLogIn);
			    String nom = txtNom.getText().toString();
			    String pass = txtPass.getText().toString();
			    
			    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
			    try{
			    	signUp(nom,pass);
			    }catch(Exception e){
					Toast.makeText(getApplicationContext(), "Invalid User/Pass", Toast.LENGTH_SHORT).show();
			    }
			}
		});


	}

	private void signUp(String nom, String pass) {
		ParseUser user = new ParseUser();
		user.setUsername(nom.toLowerCase());
		user.setPassword(pass);

		try {
			user.signUp();
			user.addAll("MonumentsVisitats", Arrays.asList());
//			user.saveInBackground();
			user.addAll("Amics", Arrays.asList());
//			user.saveInBackground();
			user.addAll("MonumentsOpinats", Arrays.asList());
//			user.saveInBackground();
			user.addAll("QuestionarisCompletats", Arrays.asList());
//			user.saveInBackground();
			user.addAll("MonumentsFotografiats", Arrays.asList());
//			user.saveInBackground();
			user.put("Avatar", "tour_avatar");
//			user.saveInBackground();
			user.addAll("medalles", Arrays.asList());
//			user.saveInBackground();
			user.put("Punts", 0);
//			user.saveInBackground();
			user.put("PuntsCultura", 0);
//			user.saveInBackground();
			user.put("PuntsArte", 0);
//			user.saveInBackground();
			user.put("PuntsMusica", 0);
//			user.saveInBackground();
			user.put("PuntsMonumento", 0);
//			user.saveInBackground();
			user.put("PuntsDeporte", 0);
//			user.saveInBackground();
			user.put("PuntsGratuito", 0);
			user.saveInBackground();
			
			finish();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
