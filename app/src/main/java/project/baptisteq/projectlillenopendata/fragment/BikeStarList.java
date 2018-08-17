package project.baptisteq.projectlillenopendata.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import project.baptisteq.projectlillenopendata.bdd.StarFields;
import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.obs.IObserver;
import project.baptisteq.projectlillenopendata.utils.LogUtils;
import project.baptisteq.projectlillenopendata.utils.StarUtils;

public class BikeStarList extends AbstractBikeList implements IObserver {

    /**
     * Nécessité de stocker l'ensemble des Records pour mise à jour des favoris
     */

    /**
     * Ici super.currentRecords =
     */
    private List<Record> allRecords;

    public BikeStarList() {
        this.allRecords = new ArrayList<Record>();
    }

    /**
     * Methode appelée après une recherche
     * @param filterText
     */
    @Override
    @Subscribe
    public void onSearchBikesReady(String filterText) {
        return;
    }

    @Override
    public void notify(final List<Record> records) {

        if (records == null) {
            releaseExceptionAndDoNothing();
            return;
        }

        allRecords.clear();
        allRecords.addAll(records);
        currentRecords.addAll(currentRecords);

        if (bikeAdapter != null)
            bikeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LogUtils.logScreen("HELLO ACTIVITY OK");
    }

    /**
     * @param record
     * @return
     */
    private boolean isStarRecord(Record record) {
        List<StarFields> starFields = StarUtils.getAllStarFields();
        for (StarFields star : starFields)
            if (star.getRecordId().equals(record.getRecordid()))
                return true;

        return false;
    }

}
