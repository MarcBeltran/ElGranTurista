package edu.ub.scg.elgranturista;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InformacionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView lblInfo = (TextView)findViewById(R.id.lblInfo);
        lblInfo.setText(getString(R.string.textinfo));

	}

}
