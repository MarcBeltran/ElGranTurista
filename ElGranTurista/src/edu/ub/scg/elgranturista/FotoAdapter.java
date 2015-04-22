package edu.ub.scg.elgranturista;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FotoAdapter extends ArrayAdapter<FotoMonument>{
	List<FotoMonument> llistaFotos = null;
	int idLayout;
	Context mContext;
	
	public FotoAdapter(Context context,int fromLayout,List<FotoMonument> fotos) {
		super(context,fromLayout,fotos);
		this.mContext = context;
		this.idLayout = fromLayout;
		this.llistaFotos = fotos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater; 
		if(v==null){
			inflater = ((OpinionsActivity) mContext).getLayoutInflater();
            v = inflater.inflate(idLayout, parent, false);
		}
		
		FotoMonument o = (FotoMonument)llistaFotos.get(position);
		TextView nom = (TextView)v.findViewById(R.id.nom);
		nom.setText(o.getUser()+": ");
		TextView likes = (TextView)v.findViewById(R.id.likes);
		likes.setText("Likes: "+String.valueOf(o.getLikes()));
		TextView reports = (TextView)v.findViewById(R.id.reports);
		reports.setText("Reports: "+String.valueOf(o.getReports()));
		ImageView foto = (ImageView)v.findViewById(R.id.fotoMonu);
		foto.setImageBitmap(o.getImage());
		
		return v;
	}
}
