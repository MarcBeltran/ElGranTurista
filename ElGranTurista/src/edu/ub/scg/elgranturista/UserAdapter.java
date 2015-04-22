package edu.ub.scg.elgranturista;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User> {
	List<User> llistaUsers = null;
	int idLayout;
	Context mContext;
	int amic; //==1 amics, ==0 ranking general.
	
	public UserAdapter(Context context,int fromLayout,List<User> users, int amic) {
		super(context,fromLayout,users);
		this.mContext = context;
		this.idLayout = fromLayout;
		this.llistaUsers = users;
		this.amic = amic;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater; 
		if(v==null){
			inflater = ((RankingActivity) mContext).getLayoutInflater();
            v = inflater.inflate(idLayout, parent, false);
		}
		
		User u = (User)llistaUsers.get(position);
		TextView nom = (TextView)v.findViewById(R.id.nom);
		nom.setText(u.getNom());
		TextView punts = (TextView)v.findViewById(R.id.punts);
		punts.setText(String.valueOf(u.getPunts())+"pts");
		
		return v;
	}
}