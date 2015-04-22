package edu.ub.scg.elgranturista;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;

public class AvatarsActivity extends Activity {
	ExternalDB db = new ExternalDB();
	InternalDB iDB = InternalDB.getInstance(this);
	List<String> fotos;
	String avatar;
	View view;
	TextView txt;
	String nomMonument;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_avatars);
		try{
            Parse.initialize(this, "QOkByz3mOBBeqB6fYdYdW913fOBiqSkvkqCxUvBp", "KQF7thPnPiDtOCMk63iZyYd4kOsAXibBq4SBJ6Fh");        	
        } catch (Exception e){
        }
		
		fotos = Arrays.asList("tour_avatar","draven","charmander","mario");
    			
		ListView llista = (ListView)findViewById(R.id.avatarsList);
		
		AvatarAdapter tmp = new AvatarAdapter(this, R.layout.avatar, fotos);

		llista.setAdapter(tmp);
		
		llista.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	        	int index = arg2;
	        	String str = fotos.get(index);
	        	showPopup(str, arg1);
	        }
	    });
	}

	protected void showPopup(String str, View arg1) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String title = str;
		avatar = str;
		
		builder.setTitle(title);
		
		builder.setNegativeButton(R.string.establir , new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				User usr = iDB.getCurrentUser();
				db.setAvatar(iDB.getCurrentUser(),avatar);
				usr.setImatge(avatar);
				iDB.updateImatgeCurrentUser(avatar);
				Intent i = new Intent(AvatarsActivity.this,ContaActivity.class);
				i.putExtra("usr", usr);
				startActivity(i);
				finish();
           	}
		});
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
		getMenuInflater().inflate(R.menu.avatars, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
