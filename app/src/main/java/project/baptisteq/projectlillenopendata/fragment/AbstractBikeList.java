package project.baptisteq.projectlillenopendata.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.controller.MapsBikeActivity;
import project.baptisteq.projectlillenopendata.exception.APIFailureException;
import project.baptisteq.projectlillenopendata.obs.IObserver;
import project.baptisteq.projectlillenopendata.utils.LogUtils;
import project.baptisteq.projectlillenopendata.view.BikeAdapter;

public abstract class AbstractBikeList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IObserver {

    protected ArrayList<Record> currentRecords;

    protected SwipeRefreshLayout srlBike;
    protected RecyclerView rvBike;
    protected BikeAdapter bikeAdapter;

    public AbstractBikeList() {
        currentRecords = new ArrayList<Record>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bike_list, container, false);

        findViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        srlBike.setOnRefreshListener(this);
        rvBike.setLayoutManager(new LinearLayoutManager(getActivity()));
        bikeAdapter = new BikeAdapter(currentRecords, getActivity());
        rvBike.setAdapter(bikeAdapter);

        restoreRecordsIfNeeded(savedInstanceState);
    }

    protected void restoreRecordsIfNeeded(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<Record> saveRecords = savedInstanceState.getParcelableArrayList(MapsBikeActivity.KEY_RECORD_LIST);
            currentRecords.addAll(saveRecords);
        }
    }

    @Subscribe
    public abstract void onSearchBikesReady(String filterText);

    @NonNull
    protected List<Record> getFilterRecords(String filterText) {

        if (filterText.isEmpty())
            return currentRecords;

        /**
         * Ici on trie la liste courante de records avec le texte entré
         * On filtre dans un premier temps sur le nom de la commune
         */
        String filterTextUpperCase = filterText.toUpperCase();
        List<Record> filterRecords = new ArrayList<Record>();

        for (Record record : this.currentRecords) {
            Fields fields = record.getFields();
            if (fields != null && fields.getCommune() != null) {
                String communeUpperCase = fields.getCommune().toUpperCase();
                if (communeUpperCase.contains(filterTextUpperCase))
                    filterRecords.add(record);
            }
        }
        return filterRecords;
    }

    protected void findViews(View view) {
        srlBike = (SwipeRefreshLayout) view.findViewById(R.id.srlBike);
        rvBike = (RecyclerView) view.findViewById(R.id.rvBike);
    }


    protected void updateRefreshIfNeeded() {
        if (srlBike.isRefreshing())
            srlBike.setRefreshing(false);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (currentRecords != null) {
            /** Sauvegarde de la liste */
            outState.putParcelableArrayList(MapsBikeActivity.KEY_RECORD_LIST, currentRecords);
        }
    }

    @Override
    public void onRefresh() {
        /**
         * On demande à l'activité de se rafraichir
         */
        ((MapsBikeActivity) getActivity()).loadBikesOnRealTime();
    }

    protected void releaseExceptionAndDoNothing() {
        LogUtils.logException(new APIFailureException("records are NULL from service"));
        updateRefreshIfNeeded();
    }

}
