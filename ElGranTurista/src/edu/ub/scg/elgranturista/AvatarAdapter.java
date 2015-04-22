package edu.ub.scg.elgranturista;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class AvatarAdapter extends ArrayAdapter<String>{
	List<String> llistaFotos = null;
	int idLayout;
	Context mContext;
	
	public AvatarAdapter(Context context,int fromLayout,List<String> fotos) {
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
			inflater = ((AvatarsActivity) mContext).getLayoutInflater();
            v = inflater.inflate(idLayout, parent, false);
		}
		String s = (String)llistaFotos.get(position);
		ImageView avatar = (ImageView)v.findViewById(R.id.imgAvatar);
		if (s=="tour_avatar"){
			avatar.setImageResource(R.drawable.tour_avatar);
		}
		if (s=="draven"){
			avatar.setImageResource(R.drawable.draven);		
		}
		if (s=="charmander"){
			avatar.setImageResource(R.drawable.charmander);
		}
		if (s=="mario"){
			avatar.setImageResource(R.drawable.mario);
		}
		return v;
	}

}
