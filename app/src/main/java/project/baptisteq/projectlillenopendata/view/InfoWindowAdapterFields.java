package project.baptisteq.projectlillenopendata.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;

/**
 * Created by Baptiste on 23/05/18.
 */

/**
 * Classe générique qui s'occupe de rédéfinir la popup d'affichage d'un Marker sur la carte
 * Utilise dans l'affichage principale sur la carte des vélos
 */
public class InfoWindowAdapterFields extends InfoWindowAdapter {

    public InfoWindowAdapterFields(LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }


    /**
     * Redéfinition de la view "popup" d'un marker
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {

        View view = layoutInflater.inflate(R.layout.layout_bike, null);
        Fields fields = (Fields)marker.getTag();
        TextView tvBikeName = (TextView) view.findViewById(R.id.tvBikeName);
        TextView tvEtat = (TextView) view.findViewById(R.id.tvEtat);
        TextView tvStationBikeAvailable = (TextView) view.findViewById(R.id.tvStationBikeAvailable);
        TextView tvStationBorneAvailable = (TextView) view.findViewById(R.id.tvStationBorneAvailable);

        if (fields != null) {
            tvBikeName.setText(fields.getNom());
            tvEtat.setText(fields.getEtat());
            tvStationBikeAvailable.setText(String.valueOf(fields.getNbvelosdispo()));
            tvStationBorneAvailable.setText(String.valueOf(fields.getNbplacesdispo()));
        }

        return view;
    }
}
