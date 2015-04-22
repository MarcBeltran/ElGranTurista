package edu.ub.scg.elgranturista;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class OpinarActivity extends Activity {
	ExternalDB eDB = new ExternalDB();
	InternalDB iDB = InternalDB.getInstance(this);
	Monument m;
	EditText txaOpinio;
	RatingBar rating;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinar);
		
		txaOpinio = (EditText) findViewById(R.id.txtOpinioPersonal);
		rating = (RatingBar) findViewById(R.id.ratingOpinio);
		Button cmbPublica = (Button) findViewById(R.id.cmbPublica);
		m = (Monument)getIntent().getSerializableExtra("mnt");
		cmbPublica.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				new Thread()
		        {
		            public void run(){
		            	eDB.opinarMonument(iDB.getCurrentUser(),m.getNom(), m.getId(), txaOpinio.getText().toString(), rating.getRating());
		        }
		        }.start();
            	iDB.addMonumentOpinats(iDB.getCurrentUser(), m.getNom());
            	iDB.updatePuntsCurrentUser(50);
				eDB.addPointsUser(iDB.getCurrentUser(), 50);
//				Intent i = new Intent(OpinarActivity.this,CongratulationsActivity.class);
//				i.putExtra("mnt", m);
//				startActivity(i);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.opinar, menu);
		return true;
	}

}
