package edu.ub.scg.elgranturista;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.Parse;

public class OpinionsActivity extends Activity {
	ExternalDB eDB = new ExternalDB();
	InternalDB iDB = InternalDB.getInstance(this);
	List<Opinio> opinions;
	Opinio o;
	View view;
	TextView txt;
	String nomMonument;
	Activity activity;
	ListView llista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_opinions);
	    nomMonument = (String)getIntent().getSerializableExtra("mon");
	    activity=this;
	    
	    refresh();
	}
	
	private void refresh() {
		// TODO Auto-generated method stub
		try{
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
		new Thread(){
			public void run(){
				opinions = eDB.getOpinions(nomMonument);
				activity.runOnUiThread(new Runnable(){ 
					public void run(){ 
						llista = (ListView) activity.findViewById(R.id.opinionsList);
						
						OpinioAdapter tmp = new OpinioAdapter(activity, R.layout.opinio, opinions);

						llista.setAdapter(tmp);
						
						llista.setOnItemClickListener(new OnItemClickListener() {
					        @Override
					        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					        	int index = arg2;
					        	Opinio opi = opinions.get(index);
					        	showPopup(opi, arg1);
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

	void showPopup(Opinio opi, View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		o = opi;
		view = v;
		String title = o.getUser();
		
		builder.setTitle(title);
		builder.setMessage(o.getText());
		
		if (!iDB.hasUserLikedOpinio(iDB.getCurrentUser().getNom(), o)){
			builder.setNegativeButton(R.string.like , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					eDB.likeOpinio(iDB.getCurrentUser(),o);
					iDB.addOpinioLike(iDB.getCurrentUser(),o.getId());
					txt = (TextView)view.findViewById(R.id.likes);
					txt.setText("Likes: "+String.valueOf(o.getLikes()+1));
	           	}
			});
        }	else {
        	builder.setNegativeButton(R.string.dislike , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					eDB.dislikeOpinio(iDB.getCurrentUser(),o);
					iDB.removeOpinioLike(iDB.getCurrentUser(), o.getId());
					txt = (TextView)view.findViewById(R.id.likes);
					txt.setText("Likes: "+String.valueOf(o.getLikes()));
	           	}
			});
        }
		
		if (!iDB.hasUserReportedOpinio(iDB.getCurrentUser().getNom(), o)){
			builder.setNeutralButton(R.string.report , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					eDB.reportOpinio(iDB.getCurrentUser(),o);
					iDB.addOpinioReport(iDB.getCurrentUser(), o.getId());
					txt = (TextView)view.findViewById(R.id.reports);
					txt.setText("Reports: "+String.valueOf(o.getReports()+1));
				}
			});
        }	else {
			builder.setNeutralButton(R.string.unreport , new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					eDB.unreportOpinio(iDB.getCurrentUser(),o);
					iDB.removeOpinioReport(iDB.getCurrentUser(), o.getId());
					txt = (TextView)view.findViewById(R.id.reports);
					txt.setText("Reports: "+String.valueOf(o.getReports()));
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
