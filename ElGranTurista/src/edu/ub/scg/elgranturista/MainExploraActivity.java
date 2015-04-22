package edu.ub.scg.elgranturista;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.parse.Parse;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainExploraActivity extends Activity {
	int offline;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_explora);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        try{
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
        tabs();
        
		
	}

	
	private void tabs() {
	    ActionBar.Tab Tab1, Tab2, Tab3, Tab4;
		offline = (int)getIntent().getIntExtra("Offline", 0);
		Fragment fragmentTab2 = null;
		Fragment fragmentTab3 = null;
		Fragment fragmentTab4= null;

	    Fragment fragmentTab1 = new ExploraTabPlantilla(0);
	    if(offline!=1){
		    fragmentTab2 = new ExploraTabPlantilla(1);
		    fragmentTab3 = new FiltresTab();
		    fragmentTab4 = new ExploraMap();
	    }
		ActionBar actionBar = getActionBar();
		 
	    actionBar.setDisplayShowHomeEnabled(false);   
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    Tab1 = actionBar.newTab().setText(getResources().getString(R.string.general));
	    Tab1.setTabListener(new RankingTabListener(fragmentTab1));
	    actionBar.addTab(Tab1);

	    if(offline!=1){
		    Tab2 = actionBar.newTab().setText(getResources().getString(R.string.populars));
		    Tab3 = actionBar.newTab().setText(getResources().getString(R.string.filtres));
		    Tab4 = actionBar.newTab().setText(getResources().getString(R.string.mapa));
		    
		    Tab2.setTabListener(new RankingTabListener(fragmentTab2));
		    Tab3.setTabListener(new RankingTabListener(fragmentTab3));
		    Tab4.setTabListener(new RankingTabListener(fragmentTab4));
		    

		    actionBar.addTab(Tab2);
		    actionBar.addTab(Tab3);
			actionBar.addTab(Tab4);
		    
	    }
		
	}
	


}
