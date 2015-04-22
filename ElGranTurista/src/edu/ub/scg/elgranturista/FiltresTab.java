package edu.ub.scg.elgranturista;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
 
/*
 * Aquest fragment s'encarrega de gestionar els filtres que es passen a la vista
 * dels monuments i en el mapa.
 * 
 * El funcionament és el següent:
 * 
 * - Per defecte no hi ha cap CheckBox marcat i el radi de busqueda és de 10km.
 * - Quan el fragment crida el mètode onPause() es recorren tots els checkboxs 
 * 	 i es genera una llista de Strings que es guarda a la base de dades interna.
 * - Quan l'usuari selecciona el la vista dels monuments o el mapa cada un 
 *   d'aquests fragments agafa de la base de dades interna la llista de Strings
 *   i d'aquesta manera pot començar a gestionar la conversió. 
 * - El filtre per distancia és una mica diferent, es guarda el valor que s'ha 
 *   obtingut a la SeekBar (mirar comentari de onProgressChanged) i aquest es guarda
 *   a la base de dades interna.
 *   
 *El motiu pel qual hem triat aquesta manera de gestionar els filtres és que d'aquesta
 *manera podem guardar els filtres tot i que es tanqui l'aplicació. Quan l'usuari 
 *torni a arrencar la app continuarà amb els filtres que havia aplicat anteriorment.
 *
 *
 *OBSERVACIÓ:
 *
 *Quan el checkbox de proximitat no està seleccionat la distancia que es filtre és
 *10km (com si no s'apliques).
 *
 *Els filtres funcionen com disjuncions és a dir, si seleccionem només el filtre
 *de "cultura" surtiran només els monuments que tenen el tag de cultura. Si després 
 *seleccionem el filtre "art" surtiran els monuments que tenen només cultura, només art
 *o tots dos.
 *
 *Els filtres també s'apliquen a la AR. No s'aplica el filtre de distancia ja que
 *la realitat augmentada ja ho gestiona per si sola.
 *
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FiltresTab extends Fragment {
	InternalDB db = InternalDB.getInstance(getActivity());
	private SeekBar seekBar;
	private TextView dis;
	private double distancia;
	private double distanciaS; //quan s'inicia de nou
	private CheckBox proximitat;
	private CheckBox cultura;
	private CheckBox esportiu;
	private CheckBox gratuit;
	private CheckBox monument;
	private CheckBox musica;
	private CheckBox art;
	
	List<CheckBox> bfiltres;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_filtres, container, false);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar1);
        seekBar.getProgress();
        dis = (TextView) rootView.findViewById(R.id.txtKm);
        proximitat = (CheckBox)rootView.findViewById(R.id.chbProximitat);
        dis.setText(String.valueOf(db.getDistancia()));
		
		proximitat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!proximitat.isChecked()){
					seekBar.setEnabled(false);
					dis.setEnabled(false);
					dis.setText("");
				} else {
					seekBar.setEnabled(true);
					printDistancia(distancia);
					dis.setEnabled(true);
				}
			}
		});
		
		/*
		 * Aquest mètode serveix per canviar l'escala de la seekbar a
		 * logaritmica. 
		 * 
		 * El motiu d'aquesta conversió és que si es deixa la seekbar 
		 * per defecta el que correspondria a metres no es podria seleccionar
		 * ja que correspondria al 10% de la llargada de la barra.
		 * 
		 * Amb la conversió s'aconsegueix una divisió del 40 - 60 i així ja
		 * es poden seleccionar els metres perfectament.
		 */
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		    int progress = 0;
		      @Override
		      public void onProgressChanged(SeekBar seekBar,int progresValue, boolean fromUser) {
		        progress = progresValue;
		        if(progress < 1000){
					distancia = progress/10;
					dis.setText(String.valueOf(distancia) + " m");
				} else if (1000< progress && progress<5000) {
					distancia = 2*progress/10;
					dis.setText(String.valueOf(distancia) + " m");
			    } else {
			        distancia=(progress-4000)/1000.0;
			        dis.setText(String.valueOf(distancia) + " km");
			    }
		      }
		      
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});

		
		
        
        return rootView;
        
    }

	public void printDistancia(Double dist){
		if(10 < dist && dist < 1000 || dist == 0){
			dis.setText(String.valueOf(dist) + " m");
	    } else {
	        dis.setText(String.valueOf(dist) + " km");
	    }
	}

	@Override
	public void onStart() {
		super.onStart();
		ArrayList<String> filtres = db.getFiltresActius();
		Iterator<String> it = filtres.iterator();
		while(it.hasNext()){
			int resID = getResources().getIdentifier("chb"+it.next(),"id",getActivity().getPackageName());
			CheckBox ch = (CheckBox)getActivity().findViewById(resID);
			ch.setChecked(true);
		}
		Double dist = db.getDistancia();
		if(dist!=10.0 && dist!=0.0 && dist!=-1){
			CheckBox ch = (CheckBox)getActivity().findViewById(R.id.chbProximitat);
			ch.setChecked(true);
			printDistancia(db.getDistancia());
			distanciaS = dist;
		}
		
	}

	private void initCheckBox() {
		cultura= (CheckBox)getActivity().findViewById(R.id.chbCultura);
		esportiu= (CheckBox)getActivity().findViewById(R.id.chbDeporte);
		gratuit= (CheckBox)getActivity().findViewById(R.id.chbGratuito);
		monument= (CheckBox)getActivity().findViewById(R.id.chbMonumento);
		musica= (CheckBox)getActivity().findViewById(R.id.chbMusica);
		art= (CheckBox)getActivity().findViewById(R.id.chbArte);
		bfiltres = new ArrayList<CheckBox>();
		
		bfiltres.add(cultura);
		bfiltres.add(esportiu);
		bfiltres.add(gratuit);
		bfiltres.add(monument);
		bfiltres.add(musica);
		bfiltres.add(art);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!proximitat.isChecked()){
	        seekBar.setEnabled(false);
	        dis.setText("");
	        seekBar.setProgress(0);
	        distancia = 0;
	        distanciaS = 0;
			dis.setEnabled(false);
	    }
		
	}

	@Override
	public void onPause() {
		initCheckBox();
		
		Iterator<CheckBox> it = bfiltres.iterator();
		while(it.hasNext())
		{
		    CheckBox nouFiltre = it.next();
		    if(nouFiltre.isChecked()){
		    	db.createFiltre((String)nouFiltre.getText());
		    } else {
		    	db.deleteFiltre((String)nouFiltre.getText());
		    	
		    }
		    
		}
		if(distanciaS > 0 && seekBar.getProgress() == 1 && proximitat.isChecked()){
			db.updateDistancia(distanciaS);
		} else {	
			if(proximitat.isChecked()){
				db.updateDistancia(distancia);
			} else {
				db.updateDistancia(-1);
			}
		}
		
		super.onPause();
	} 
}
