package project.baptisteq.projectlillenopendata.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

import project.baptisteq.projectlillenopendata.fragment.BikeList;
import project.baptisteq.projectlillenopendata.fragment.MapsGlobal;

/**
 * Created by Baptiste on 20/05/18.
 */

public class PagerAdapterBike extends FragmentPagerAdapter {


    private List<Fragment> fragments = Arrays.asList((Fragment)new MapsGlobal(), (Fragment)new BikeList());


    public PagerAdapterBike(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * Retourne la liste des Observateurs
     * @return
     */
    public List<? extends Fragment> getFragments() {
        return fragments;
    }
}
