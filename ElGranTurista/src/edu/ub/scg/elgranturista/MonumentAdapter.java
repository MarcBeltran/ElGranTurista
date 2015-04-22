package edu.ub.scg.elgranturista;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MonumentAdapter extends ArrayAdapter<Monument> implements LocationListener{
	List<Monument> llistaMonuments = null;
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("@@##");
	private final float[] distanceArray = new float[1];
	float dis;
	int idLayout, opt;
	Context mContext;
	Location l;
	
	public MonumentAdapter(Context context,int fromLayout,List<Monument> monuments, int opcio) {
		super(context,fromLayout,monuments);
		this.mContext = context;
		this.idLayout = fromLayout;
		this.llistaMonuments = monuments;	
		this.opt = opcio;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater inflater = null;
		
		if(v==null){
			if (opt == 1) {
				inflater = ((MonumentsVisitatsActivity) mContext).getLayoutInflater();
			}
			if (opt == 0 || opt == 2) {
				inflater = ((MainExploraActivity) mContext).getLayoutInflater();
			}
            v = inflater.inflate(idLayout, parent, false);
		}
		
		LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER )) {  
        	l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
        }else{
        	l = lm.getLastKnownLocation(lm.GPS_PROVIDER);
        }
		if(l==null){
			l = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
		}
		Monument m = (Monument)llistaMonuments.get(position);
		if(l!=null){
			Location.distanceBetween(m.getLat(), m.getLng(), l.getLatitude(), l.getLongitude(),  distanceArray);
	    } else{
	    	Location.distanceBetween(0,0,0,0,distanceArray);
	    }
		dis = distanceArray[0];
		
		
		
		TextView nom = (TextView)v.findViewById(R.id.nom);
		nom.setText(m.getNom());
		TextView info = (TextView)v.findViewById(R.id.info);
		String textStr;
	    if (dis<1000.0) {
	        textStr = DECIMAL_FORMAT.format(dis) + " m";          
	    } else {
	        double d=dis/1000.0;
	        textStr =  DECIMAL_FORMAT.format(d) + "km";
	    }
		
		info.setText(textStr);
		TextView punts = (TextView)v.findViewById(R.id.punts);
		if (opt == 0|| opt == 1){
			punts.setText(String.valueOf(m.getPunts()));
		}
		if (opt == 2){
			punts.setText(String.valueOf(round(m.getPuntsUsers(),1)));
		}
		
		return v;
	}

	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
