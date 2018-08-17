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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.controller.DetailActivity;
import project.baptisteq.projectlillenopendata.view.InfoWindowAdapterFieldsDetailAddress;

/**
 * A simple {@link Fragment} subclass.
 */

/***
 * Fragment pour afficher le détail d'un élément de la carte
 * Utilisé le plus souvent dans une activité de détail
 */
public class MapsDetail extends Fragment implements OnMapReadyCallback {

    /**
     * Element à afficher
     */
    private Fields fields;

    private MapView mapView;
    private GoogleMap map;


    public MapsDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps_detail, container, false);

        mapView = (MapView) view.findViewById(R.id.mapViewDetail);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fields = (Fields)this.getArguments().getSerializable(DetailActivity.KEY_FIELDS_DETAIL);

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
    }

    /**
     * Callback quand la carte est prête
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

     //   MapsInitializer.initialize(getActivity());
        mapView.onResume();

        map.setInfoWindowAdapter(new InfoWindowAdapterFieldsDetailAddress(LayoutInflater.from(getActivity())));

        /**
         * Ici on place le marker sur la carte
         */

        MarkerOptions markerOptions = new MarkerOptions().position(fields.getPosition());
        Marker marker = map.addMarker(markerOptions);
        marker.setTag(fields);
        /**
         * On zoome au max
         */
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(fields.getPosition(), 14));

        /**
         * Ouverture info window
         */
        marker.showInfoWindow();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && map != null)
            map.setMyLocationEnabled(true);
    }
}
