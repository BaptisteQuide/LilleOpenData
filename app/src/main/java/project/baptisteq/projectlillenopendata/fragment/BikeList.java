package project.baptisteq.projectlillenopendata.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.controller.ApplicationProjectLilleOpenData;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * Fragment qui représente l'ensemble des stations disponibles
 */
public class BikeList extends AbstractBikeList {

    public BikeList() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    @Subscribe
    public void onSearchBikesReady(String filterText) {

        List<Record> filterRecords = getFilterRecords(filterText);

        bikeAdapter.setRecords(filterRecords);
        bikeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        ApplicationProjectLilleOpenData.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ApplicationProjectLilleOpenData.getBus().unregister(this);
    }


    /**
     * Gestion de l'ensemble des stations
     * @param records
     */
    protected void addAllAndRefresh(List<Record> records) {
        currentRecords.clear();
        Collections.sort(records);
        currentRecords.addAll(records);
        bikeAdapter.notifyDataSetChanged();
        updateRefreshIfNeeded();
    }


    @Override
    public void notify(List<Record> records) {

        if (records == null) {
            releaseExceptionAndDoNothing();
            return;
        }

        addAllAndRefresh(records);
    }

}
