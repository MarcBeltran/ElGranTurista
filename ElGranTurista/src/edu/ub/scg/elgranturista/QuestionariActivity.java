package edu.ub.scg.elgranturista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionariActivity extends Activity {
	Monument m;
	InternalDB iDB =InternalDB.getInstance(this);
	int pregunta=0;
	int randomNum;
	TextView question;
	RadioButton rb1;
	RadioButton rb2;
	RadioButton rb3;
	ExternalDB eDB = new ExternalDB();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Activity activity=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionari);
		
		m = (Monument)getIntent().getSerializableExtra("mnt");
		question = (TextView)findViewById(R.id.lblPregunta);
		rb1 = (RadioButton)findViewById(R.id.rdbP1R1);
		rb2 = (RadioButton)findViewById(R.id.rdbP1R2);
		rb3 = (RadioButton)findViewById(R.id.rdbP1R3);
		
		printQuestion(pregunta);
		
		Button cmbQuestSeguent = (Button)findViewById(R.id.cmbQuestSeguent);
		cmbQuestSeguent.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				if((randomNum==1&&rb1.isChecked())||(randomNum==2&&rb2.isChecked())||(randomNum==3&&rb3.isChecked())){
					pregunta++;
					if (pregunta<m.getAr().length){
						rb1.setChecked(false);
						rb2.setChecked(false);
						rb3.setChecked(false);
						printQuestion(pregunta);
					}else{
						iDB.addMonumentQuestionariats(iDB.getCurrentUser(), m.getNom());
						iDB.updatePuntsCurrentUser(50);
						class QuestionariExecutar extends AsyncTask<String, Void, String> {
							
		    				ProgressDialog pd;
		    				@Override
		    		        protected String doInBackground(String... urls) {
		    					eDB.addPointsUser(iDB.getCurrentUser(), 50);
								eDB.afegirMedallaUsuari("questionari",iDB.getCurrentUser());
								eDB.addQuestionariCompletat(iDB.getCurrentUser(),m);
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
		    		        	activity.finish();
		    		        	
		    		        }
						}
						new QuestionariExecutar().execute();
						
						
					}
				} else {
					Toast.makeText(getApplicationContext(), "Almost!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questionari, menu);
		
		return true;
	}
	
	private void printQuestion(int pregunta){
		randomNum = 1 + (int)(Math.random()*2);
		question.setText(m.getQ()[pregunta]);
		switch(randomNum){
		case 1:
			rb1.setText(m.getAr()[pregunta]);
			rb2.setText(m.getAw1()[pregunta]);
			rb3.setText(m.getAw2()[pregunta]);
			break;
		case 2:
			rb2.setText(m.getAr()[pregunta]);
			rb3.setText(m.getAw1()[pregunta]);
			rb1.setText(m.getAw2()[pregunta]);
			break;
		case 3:
			rb3.setText(m.getAr()[pregunta]);
			rb1.setText(m.getAw1()[pregunta]);
			rb2.setText(m.getAw2()[pregunta]);
			break;
			
		}
	}

}
