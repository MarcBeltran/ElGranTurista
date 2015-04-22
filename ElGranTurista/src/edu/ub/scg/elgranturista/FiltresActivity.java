package edu.ub.scg.elgranturista;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class FiltresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filtres);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filtres, menu);
		return true;
	}

}
