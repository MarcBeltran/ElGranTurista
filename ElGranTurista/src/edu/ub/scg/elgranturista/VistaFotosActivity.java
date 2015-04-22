package edu.ub.scg.elgranturista;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;

public class VistaFotosActivity extends Activity {
	ExternalDB db = new ExternalDB();
	InternalDB iDB = InternalDB.getInstance(null);
	View view;
	TextView txt;
	String nomMonument;
	GridView llista;
	TextView likesTextView,reportsTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_vista_fotos);
	    nomMonument = (String)getIntent().getSerializableExtra("mon");
	    llista = (GridView) this.findViewById(R.id.gridview);
	    refresh();
	    llista.setOnItemClickListener(new OnItemClickListener()
	    {
	       @Override
	       public void onItemClick(AdapterView<?> adapter, View v, int position,
	             long arg3) 
	       {
	             ImatgeCongrats img = (ImatgeCongrats)adapter.getItemAtPosition(position);
	             Log.d("pitxat","a");
	             showPopup(img,v);
	             
	       }
	    });
	}
	
	private void refresh() {
		// TODO Auto-generated method stub
		try{
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
    			
		ImatgeCongratsAdapter mainAdapter = new ImatgeCongratsAdapter(this, nomMonument);
		llista.setAdapter(mainAdapter);
		
	}
	
	void showPopup(final ImatgeCongrats im, View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater factory = LayoutInflater.from(this);
		ImageView im1 = (ImageView) v.findViewById(R.id.icon);
		View view = factory.inflate(R.layout.popupimatge, null);
		ImageView imatg = (ImageView) view.findViewById(R.id.imagePopUp);
		imatg.setImageDrawable(im1.getDrawable());
		builder.setView(view);
		String t = im.getNomUsuari() + " L: " + im.getLikes() + " R: " + im.getReports();
		builder.setTitle(t);

		if (!iDB.hasUserLikedFoto(iDB.getCurrentUser().getNom(), im.getObjectId())){
			builder.setNegativeButton(R.string.like , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					im.setLikes(im.getLikes() + 1);
					im.saveInBackground();
					im.setLikesUsers(iDB.getCurrentUser().getNom());
					im.saveInBackground();
					iDB.addFotoLike(iDB.getCurrentUser(), im.getObjectId());
	           	}
			});
        }	else {
        	builder.setNegativeButton(R.string.dislike , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					im.setLikes(im.getLikes() - 1);
					im.saveInBackground();
					im.removeAll("LikesUsers", Arrays.asList(iDB.getCurrentUser().getNom()));
					im.saveInBackground();
					iDB.removeFotoLike(iDB.getCurrentUser(), im.getObjectId());
	           	}
			});
        }
		
		if (!iDB.hasUserReportedFoto(iDB.getCurrentUser().getNom(), im.getObjectId())){
			builder.setNeutralButton(R.string.report , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					im.setReports(im.getReports() + 1);
					im.saveInBackground();
					im.setReportsUsers(iDB.getCurrentUser().getNom());
					im.saveInBackground();
					iDB.addFotoReport(iDB.getCurrentUser(), im.getObjectId());
				}
			});
        }	else {
			builder.setNeutralButton(R.string.unreport , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					im.setReports(im.getReports() - 1);
					im.saveInBackground();
					im.removeAll("ReportsUsers", Arrays.asList(iDB.getCurrentUser().getNom()));
					im.saveInBackground();
					iDB.removeFotoReport(iDB.getCurrentUser(), im.getObjectId());
				}
			});
        }
		builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog				
           	}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.opinions, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
            	refresh();
                break;
        }
        return true;
    }
}
