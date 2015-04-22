package edu.ub.scg.elgranturista;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OpinioAdapter extends ArrayAdapter<Opinio>{
	List<Opinio> llistaOpinions = null;
	int idLayout;
	Context mContext;
	
	public OpinioAdapter(Context context,int fromLayout,List<Opinio> opinions) {
		super(context,fromLayout,opinions);
		this.mContext = context;
		this.idLayout = fromLayout;
		this.llistaOpinions = opinions;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater; 
		if(v==null){
			inflater = ((OpinionsActivity) mContext).getLayoutInflater();
            v = inflater.inflate(idLayout, parent, false);
		}
		
		Opinio o = (Opinio)llistaOpinions.get(position);
		TextView nom = (TextView)v.findViewById(R.id.nom);
		nom.setText(o.getUser()+": ");
		TextView text = (TextView)v.findViewById(R.id.textOpinio);
		text.setText(o.getText());
		TextView likes = (TextView)v.findViewById(R.id.likes);
		likes.setText("Likes: "+String.valueOf(o.getLikes()));
		TextView reports = (TextView)v.findViewById(R.id.reports);
		reports.setText("Reports: "+String.valueOf(o.getReports()));
		TextView puntuacio = (TextView)v.findViewById(R.id.puntuacio);
		puntuacio.setText(String.valueOf(o.getPuntuacio())+" / 5");
		
		return v;
	}
}
