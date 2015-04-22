package edu.ub.scg.elgranturista;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class CongratulationsActivity extends Activity {
	Monument m;
	ExternalDB eDB = new ExternalDB();
	InternalDB db = InternalDB.getInstance(this);
	ParseFile photoFile;
	ImatgeCongrats img;
	Activity activity;
	TextView primer;
	Button cmbOpina,cmbQuestionari;
	Uri u;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		crearView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thank_you, menu);
		return true;
	}
	
	public void onResume(){
		super.onResume();
		crearView();
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(0));
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	        	u = Uri.fromFile(photoFile);
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,u);
	            db.addMonumentFotografiats(db.getCurrentUser(), m.getNom());
	            db.updatePuntsCurrentUser(200);
	            eDB.afegirMonumentUsuariFotografiat(m, db.getCurrentUser());
	            eDB.addPointsUser(db.getCurrentUser(), 200);
	            startActivityForResult(takePictureIntent, 1);
	        }
	    }
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	    	new Thread(){
	    		public void run(){
	    			ContentResolver cr = getContentResolver();
	    			Bitmap bitmap = null;
	    			Bitmap scaledBitmap = null;
					try {
						bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, u);
						scaledBitmap = Bitmap.createScaledBitmap(bitmap,480,640,false);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		        crearImatge(scaledBitmap);
	    		        eDB.afegirMedallaUsuari("camera",db.getCurrentUser());
	    		}
	    	}.start();
	      
	    }
	}
	private void crearImatge(Bitmap imageBitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        
        byte[] scaledData = bos.toByteArray();
        
        
        photoFile = new ParseFile("imatgeCongrats.jpg", scaledData);
        
        photoFile.saveInBackground();
        img = new ImatgeCongrats();
        img.setNomUsuari(eDB.getCurrentUser().getNom());
        img.setNomMonument(m.getNom());
        img.setLikes(0);
        img.setReports(0);
        img.setImatge(photoFile);        
        img.saveInBackground();
		
	}

	
	private void crearView(){
		activity=this;
		setContentView(R.layout.activity_congratulations);
		
		ParseObject.registerSubclass(ImatgeCongrats.class);
		


        try{
        	
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }

		m = (Monument)getIntent().getSerializableExtra("mnt");
		
		
		
		primer = (TextView)findViewById(R.id.lblCongratulations);
		primer.setText(getResources().getString(R.string.congratulations1)+" "+m.getNom()+getResources().getString(R.string.congratulations2)+" "+Integer.toString(m.getPunts())+getResources().getString(R.string.congratulations3));
		primer.setTextSize(15);
		
		 new Thread()
         {
             public void run(){
            	 if(db.hasMonumentBeenVisitat(db.getCurrentUser(),m)){
            		  activity.runOnUiThread(new Runnable(){
          	 		    public void run(){
          	 		    	primer.setText(getString(R.string.congratulations1)+" "+m.getNom()+ " "+
          	 		    			getString(R.string.congratulations2)+" "+String.valueOf(m.getPunts())+
          	 		    			" "+getString(R.string.congratulations3));
          	 		    }
          	 		});
            	 }else{
            		eDB.afegirMonumentUsuari(m, db.getCurrentUser());
            		db.addMonumentVisitat(db.getCurrentUser(), m.getNom());
          			assignarPunts();
          			eDB.afegirMedallaUsuari("visita",db.getCurrentUser());
            	 }   		      
         }
         }.start();
		

		
		
		cmbOpina = (Button)findViewById(R.id.cmbOpina);
		cmbOpina.setEnabled(false);
   	 	if(db.hasMonumentBeenOpinat(db.getCurrentUser(),m)){
 	 		    	cmbOpina.setText(getString(R.string.yahasopinado));
 	 		    	cmbOpina.setEnabled(false);
 	 		    	cmbOpina.setBackground(this.getResources().getDrawable(R.drawable.shape_disabled)); 
 	 		    
 		}else{         	 		    	
      		 cmbOpina.setEnabled(true);
      	}    		      
		cmbOpina.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				Intent i = new Intent(CongratulationsActivity.this,OpinarActivity.class);
				i.putExtra("mnt", m);
				startActivity(i);
			}
		});

		Button cmbFoto = (Button)findViewById(R.id.cmbFoto);
		cmbFoto.setEnabled(false);
   	 	if(db.hasMonumentBeenFotografiat(db.getCurrentUser(),m)){
 	 		    	cmbFoto.setText(getString(R.string.yahasfoto));
 	 		    	cmbFoto.setEnabled(false);
 	 		    	cmbFoto.setBackground(this.getResources().getDrawable(R.drawable.shape_disabled)); 
 	 		    	TextView tv1 = (TextView)findViewById(R.id.lblFotoPoints);
 	 		    	tv1.setText("A");
 	 		    
 		}else{       	 		    	
      		 cmbFoto.setEnabled(true);
      	}    		      

		cmbFoto.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				dispatchTakePictureIntent();
			}
		});
		
		cmbQuestionari = (Button)findViewById(R.id.cmbQuestionari);
		cmbQuestionari.setEnabled(false);
           	 if(db.hasMonumentBeenQuestionariat(db.getCurrentUser(),m)){
         	 		    	cmbQuestionari.setText(getString(R.string.yahasquestionario));
         	           		cmbQuestionari.setEnabled(false);
         	           		cmbQuestionari.setBackground(this.getResources().getDrawable(R.drawable.shape_disabled)); 
	         	           	TextView tv1 = (TextView)findViewById(R.id.lblQuestionariPoints);
	     	 		    	tv1.setText("A");
         		}
           	 else{         	 		    	
           		 cmbQuestionari.setEnabled(true);
           	 }

		cmbQuestionari.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v){
				Intent i = new Intent(CongratulationsActivity.this,QuestionariActivity.class);
				i.putExtra("mnt", m);
				startActivity(i);
			}
		});
		
		TextView lblFP = (TextView)findViewById(R.id.lblFotoPoints);
		TextView lblOP = (TextView)findViewById(R.id.lblOpinaPoints);
		TextView lblQP = (TextView)findViewById(R.id.lblQuestionariPoints);
		lblFP.setText(getResources().getString(R.string.congratulations_puntsfoto));
		lblOP.setText(getResources().getString(R.string.congratulations_puntsopina));
		lblQP.setText(getResources().getString(R.string.congratulations_puntsquestionari));
	
		
	}
	private void assignarPunts(){
		eDB.addPointsUser(db.getCurrentUser(), m.getPunts());
		db.updatePuntsCurrentUser(m.getPunts());
		ArrayList<String> tags = db.getTagsMonument(m);
		Iterator<String> it = tags.iterator();
		while(it.hasNext())
		{
		    eDB.addPointsFiltreUser(db.getCurrentUser(),m.getPunts(),it.next());
		}
	}

}
