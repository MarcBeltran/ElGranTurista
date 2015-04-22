package edu.ub.scg.elgranturista;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
 
@SuppressLint("Instantiatable")
@TargetApi(Build.VERSION_CODES.HONEYCOMB) //Tabs requereixen API 11
public class RankingTabListener implements ActionBar.TabListener {
 
    Fragment fragment;
 
    public RankingTabListener(Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.fragment = fragment;
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        ft.replace(R.id.fragment_container, fragment);
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        ft.remove(fragment);
    }
 
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
 
    }
}
