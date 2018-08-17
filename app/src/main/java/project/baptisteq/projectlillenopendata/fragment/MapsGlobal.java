package project.baptisteq.projectlillenopendata.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.beans.FieldsWrapper;
import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.controller.ApplicationProjectLilleOpenData;
import project.baptisteq.projectlillenopendata.controller.MapsBikeActivity;
import project.baptisteq.projectlillenopendata.obs.IObserver;
import project.baptisteq.projectlillenopendata.utils.LogUtils;
import project.baptisteq.projectlillenopendata.view.ClusterInfoRenderer;
import project.baptisteq.projectlillenopendata.view.InfoWindowAdapterFields;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsGlobal extends Fragment implements IObserver, OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<Fields>, ClusterManager.OnClusterItemInfoWindowClickListener<Fields> {

    private MapView mapView;
    private GoogleMap map;

    private ClusterManager<Fields> clusterManager;
    private ArrayList<Record> currentRecords;

    private Fields currentFields;

    public MapsGlobal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater
                .inflate(R.layout.fragment_maps, container, false);


        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (currentRecords != null) {
            outState.putParcelableArrayList(MapsBikeActivity.KEY_RECORD_LIST, currentRecords);
        }

        if (currentFields != null) {
            outState.putParcelable(MapsBikeActivity.KEY_CURRENTFIELD, currentFields);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * Restauration des éléments si changement de configuration
         */
        restoreRecordsIfNeeded(savedInstanceState);
    }

    /**
     * Restaure l'ensemble de la liste des Records si sauvegarde précédente
     *
     * @param savedInstanceState
     */
    private void restoreRecordsIfNeeded(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<Record> saveRecords = savedInstanceState.getParcelableArrayList(MapsBikeActivity.KEY_RECORD_LIST);
            if (saveRecords != null) {
                currentRecords = new ArrayList<Record>();
                currentRecords.addAll(saveRecords);
            }
            currentFields = savedInstanceState.getParcelable(MapsBikeActivity.KEY_CURRENTFIELD);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        MapsInitializer.initialize(getActivity());

        mapView.onResume();

        clusterManager = new ClusterManager<Fields>(getActivity(), map);

        clusterManager.setRenderer(new ClusterInfoRenderer(getActivity(), map, clusterManager, currentFields));
        map.setOnCameraIdleListener(clusterManager);

        /**
         * Chargement des données sur la disponibilité des vélos
         */

        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new InfoWindowAdapterFields(LayoutInflater.from(getActivity()))); /* Redéfinition de l'affichage des markers */
        clusterManager.setOnClusterItemClickListener(this); /* Click sur le point d'affichage  */
        clusterManager.setOnClusterItemInfoWindowClickListener(this); /* Click sur la fenêtre d'un Marker */

        map.setOnMarkerClickListener(clusterManager);
        map.setOnInfoWindowClickListener(clusterManager);
        map.setInfoWindowAdapter(clusterManager.getMarkerManager());

        /**
         * Appelé uniquement après une restauration d'écran
         * Ou si l'information a été récupérée avant le chargement de la carte
         */
        if (currentRecords != null)
            fillMapWithBikes();

        setMyLocationOnMap();
    }

    /**
     * Méthode de notification par la classe Observable
     *
     * @param records
     */
    @Override
    public void notify(final List<Record> records) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (records == null) {
                    LogUtils.logException(new Exception("records are NULL from service"));
                    return;
                }

                currentRecords = new ArrayList<Record>();
                currentRecords.addAll(records);

                if (map != null)
                    fillMapWithBikes();
            }
        });
    }

    /**
     * Peuple le cluster avec la réponse RETROFIT contenant les vélos
     */
    private void fillMapWithBikes() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        clusterManager.clearItems();

        for (Record record : currentRecords) {
            if (record.getFields() != null) {
                Fields fields = record.getFields();
                fields.setReferenceRecordId(record.getRecordid());
                clusterManager.addItem(record.getFields());
                builder.include(record.getFields().getPosition());
            }

        }

        /* Premier passage */
        if (currentFields == null) {

            /**
             * Utile en cas de swipe intensif du tablayout
             */
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding));
        }
        /* Restauration d'écran, on zoome au max sur la station cliquée */
        else
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentFields.getPosition(), 14));

    }

    @Override
    public boolean onClusterItemClick(Fields fields) {
        currentFields = fields;
        return false;
    }

    /**
     * Click sur une "Window" d'un Marker
     * On avertit le contrôleur
     *
     * @param fields
     */
    @Override
    public void onClusterItemInfoWindowClick(Fields fields) {
        ApplicationProjectLilleOpenData.getBus().post(new FieldsWrapper(fields, fields.getReferenceRecordId()));
    }

    private void setMyLocationOnMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && map != null)
            map.setMyLocationEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        if (mapView != null)
            mapView.onResume();
        super.onResume();
    }



}
