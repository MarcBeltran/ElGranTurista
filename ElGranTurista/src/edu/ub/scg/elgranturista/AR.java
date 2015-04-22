package edu.ub.scg.elgranturista;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

import com.paar.ch9.ARData;
import com.paar.ch9.AugmentedActivity;
import com.paar.ch9.Marker;
import com.paar.ch9.NetworkDataSource;

/*
 * Aquesta classe gestiona la realitat augmentada juntament amb la llibreria ARLib.
 * Hi ha mètodes per gestionar descàrrega d'informació per internet ja que es podria
 * fer en futur tot i així ara mateix no es criden mai.
 * 
 * El funcionament és el següent:
 * 
 * - Es crea un objecte LocalDataSource i aquest crea els markers que surtiran al mapa.
 * - Un cop fer taixò s'afegeixen els marquers a ARData i apartir d'aquí la llibreria
 *   s'encarrega de gestionar totes les funcionalitats.
 */

public class AR extends AugmentedActivity {
    private static final String TAG = "MainActivity";
    private static final String locale = "en";
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);
	private static final Map<String,NetworkDataSource> sources = new ConcurrentHashMap<String,NetworkDataSource>();    
	InternalDB db;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        db = InternalDB.getInstance(this);
        LocalDataSource localData = new LocalDataSource(this.getResources(),db);
        if(!ARData.getMarkers().isEmpty()){
			ARData.CleanMarkers();
		}
        ARData.addMarkers(localData.getMarkers());
    }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
    public void onStart() {
        super.onStart();
        
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
   

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showRadar:
                showRadar = !showRadar;
                item.setTitle(((showRadar)? "Hide" : "Show")+" Radar");
                break;
            case R.id.showZoomBar:
                showZoomBar = !showZoomBar;
                item.setTitle(((showZoomBar)? "Hide" : "Show")+" Zoom Bar");
                zoomLayout.setVisibility((showZoomBar)?LinearLayout.VISIBLE:LinearLayout.GONE);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

	@Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        
        updateData(location.getLatitude(),location.getLongitude(),location.getAltitude());
    }

	@Override
	protected void markerTouched(Marker marker) {
		Monument m = db.selectMonumentByName(marker.getName());
		Intent i = new Intent(this,VistaMonumentActivity.class);
		i.putExtra("mnt", m);
		startActivity(i);
	}

    @Override
	protected void updateDataOnZoom() {
	    super.updateDataOnZoom();
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
	}
    
    private void updateData(final double lat, final double lon, final double alt) {
        try {
            exeService.execute(
                new Runnable() {
                    
                    public void run() {
                        for (NetworkDataSource source : sources.values())
                            download(source, lat, lon, alt);
                    }
                }
            );
        } catch (RejectedExecutionException rej) {
            Log.w(TAG, "Not running new download Runnable, queue is full.");
        } catch (Exception e) {
            Log.e(TAG, "Exception running download Runnable.",e);
        }
    }
    
    private static boolean download(NetworkDataSource source, double lat, double lon, double alt) {
		if (source==null) return false;
		
		String url = null;
		try {
			url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);    	
		} catch (NullPointerException e) {
			return false;
		}
    	
		List<Marker> markers = null;
		try {
			markers = source.parse(url);
		} catch (NullPointerException e) {
			return false;
		}

    	ARData.addMarkers(markers);
    	return true;
    }
}