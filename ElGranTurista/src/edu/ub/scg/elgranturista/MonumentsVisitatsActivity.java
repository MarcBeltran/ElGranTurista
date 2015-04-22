package edu.ub.scg.elgranturista;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MonumentsVisitatsActivity extends Activity {
	InternalDB db = InternalDB.getInstance(this);
	ExternalDB eDB = new ExternalDB();
	List<Monument> monuments;
	Monument m;
	Activity activity;
	String nomUser;
	ListView llista;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.explora_tab);
	    activity=this;
		nomUser = (String)getIntent().getSerializableExtra("usr");
	    new Thread(){
			public void run(){
				monuments = eDB.getMonumentsVisitatsUsuariMonument(eDB.getUser(nomUser));	
				activity.runOnUiThread(new Runnable(){ 
					public void run(){ 
						llista = (ListView) activity.findViewById(R.id.listMonuments);
						
						MonumentAdapter tmp = new MonumentAdapter(activity, R.layout.monument, monuments, 1);
					
						llista.setAdapter(tmp);
						
						llista.setOnItemClickListener(new OnItemClickListener() {
					        @Override
					        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					        	String nomMonu = ((TextView) arg1.findViewById(R.id.nom)).getText().toString();
					        	Monument m = db.selectMonumentByName(nomMonu);
					        	
								Intent i = new Intent(MonumentsVisitatsActivity.this,VistaMonumentActivity.class);
								i.putExtra("mnt", m);					
								startActivity(i);
					        }
					    });
						ProgressBar spin= (ProgressBar)activity.findViewById(R.id.pbSpin);
      	 		    	spin.setVisibility(View.INVISIBLE);
      	 		    	llista.setVisibility(View.VISIBLE);
						;}
					}); 
				
			}
		}.start();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monuments_visitats, menu);
		return true;
	}

}
