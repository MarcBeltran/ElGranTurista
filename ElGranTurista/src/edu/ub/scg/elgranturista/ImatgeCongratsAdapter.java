package edu.ub.scg.elgranturista;

import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class ImatgeCongratsAdapter extends ParseQueryAdapter<ImatgeCongrats>{
	 
    public ImatgeCongratsAdapter(Context context,final String mnt) {
        super(context, new ParseQueryAdapter.QueryFactory<ImatgeCongrats>() {
        	public ParseQuery<ImatgeCongrats> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("ImatgeCongrats");
                query.whereContainedIn("nomMonument", Arrays.asList(mnt));
                return query;
            }
        });
    }
    
    @Override
    public View getItemView(ImatgeCongrats im, View v, ViewGroup parent) {
     
        if (v == null) {
            v = View.inflate(getContext(), R.layout.imagecongratslist, null);
        }
        super.getItemView(im, v, parent);
     
        ParseImageView imCongrats = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = im.getParseFile("photo");
        if (photoFile != null) {
            imCongrats.setParseFile(photoFile);
            imCongrats.loadInBackground();
        }
        /*TextView titleTextView = (TextView) v.findViewById(R.id.text1);
            titleTextView.setText(im.getNomUsuari());
        TextView likesTextView = (TextView) v
                .findViewById(R.id.likes);
        likesTextView.setText(String.valueOf(im.getLikes()));
        TextView reportsTextView = (TextView) v
                .findViewById(R.id.reports);
        reportsTextView.setText(String.valueOf(im.getReports()));*/
        return v;
    } 
 
}