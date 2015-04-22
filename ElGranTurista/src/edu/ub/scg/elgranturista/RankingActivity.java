package edu.ub.scg.elgranturista;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RankingActivity extends Activity {
	String usrName;
	int opcio=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        usrName = (String)getIntent().getSerializableExtra("usr");
        opcio = (Integer)getIntent().getSerializableExtra("opt");
        tabs();
        
    }


	private void tabs() {
	    ActionBar.Tab Tab1, Tab2, Tab3, Tab4, Tab5, Tab6;
	    Fragment  fragmentTab1 = null;
	    Fragment  fragmentTab2 = null;
	    Fragment  fragmentTab3 = null;
	    Fragment  fragmentTab4 = null;
	    Fragment  fragmentTab5 = null;
	    Fragment  fragmentTab6 = null;
	    
	    if (opcio == 0) {
		    fragmentTab1 = new RankingTabPlantilla("General",usrName);
		    fragmentTab2 = new RankingTabPlantilla(getResources().getString(R.string.filtresMonument),usrName);
		    fragmentTab3 = new RankingTabPlantilla(getResources().getString(R.string.filtresCultura),usrName);
		    fragmentTab4 = new RankingTabPlantilla(getResources().getString(R.string.filtresMusica),usrName);
		    fragmentTab5 = new RankingTabPlantilla(getResources().getString(R.string.filtresEsportiu),usrName);
		    fragmentTab6 = new RankingTabPlantilla(getResources().getString(R.string.filtresArt),usrName);
	    }
	    if (opcio == 1) {
	    	fragmentTab1 = new AmicsTabPlantilla("General",usrName);
		    fragmentTab2 = new AmicsTabPlantilla(getResources().getString(R.string.filtresMonument),usrName);
		    fragmentTab3 = new AmicsTabPlantilla(getResources().getString(R.string.filtresCultura),usrName);
		    fragmentTab4 = new AmicsTabPlantilla(getResources().getString(R.string.filtresMusica),usrName);
		    fragmentTab5 = new AmicsTabPlantilla(getResources().getString(R.string.filtresEsportiu),usrName);
		    fragmentTab6 = new AmicsTabPlantilla(getResources().getString(R.string.filtresArt),usrName);
	    }
	    if (opcio == 2) {
		    fragmentTab1 = new RankingTabPlantilla("General");
		    fragmentTab2 = new RankingTabPlantilla(getResources().getString(R.string.filtresMonument));
		    fragmentTab3 = new RankingTabPlantilla(getResources().getString(R.string.filtresCultura));
		    fragmentTab4 = new RankingTabPlantilla(getResources().getString(R.string.filtresMusica));
		    fragmentTab5 = new RankingTabPlantilla(getResources().getString(R.string.filtresEsportiu));
		    fragmentTab6 = new RankingTabPlantilla(getResources().getString(R.string.filtresArt));
	    }
	    
	    
		ActionBar actionBar = getActionBar();
		 
        actionBar.setDisplayShowHomeEnabled(false);   
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        Tab1 = actionBar.newTab().setText("General");
        Tab2 = actionBar.newTab().setText("Monuments");
        Tab3 = actionBar.newTab().setText("Cultura");
        Tab4 = actionBar.newTab().setText("Musica");
        Tab5 = actionBar.newTab().setText("Esportiu");
        Tab6 = actionBar.newTab().setText("Art");
        
        Tab1.setTabListener(new RankingTabListener(fragmentTab1));
        Tab2.setTabListener(new RankingTabListener(fragmentTab2));
        Tab3.setTabListener(new RankingTabListener(fragmentTab3));
        Tab4.setTabListener(new RankingTabListener(fragmentTab4));
        Tab5.setTabListener(new RankingTabListener(fragmentTab5));
        Tab6.setTabListener(new RankingTabListener(fragmentTab6));

        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);
        actionBar.addTab(Tab4);
        actionBar.addTab(Tab5);
        actionBar.addTab(Tab6);	
	}
}
