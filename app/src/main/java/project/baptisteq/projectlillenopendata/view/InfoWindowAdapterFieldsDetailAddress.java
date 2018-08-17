package project.baptisteq.projectlillenopendata.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;

public class InfoWindowAdapterFieldsDetailAddress extends InfoWindowAdapter {

    public InfoWindowAdapterFieldsDetailAddress(LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * Ici on affiche uniquement la commune et l'adresse
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {

        View view = layoutInflater.inflate(R.layout.layout_bike_detail_address, null);
        Fields fields = (Fields) marker.getTag();
        TextView tvCommuneDetail = (TextView) view.findViewById(R.id.tvCommuneDetail);
        TextView tvAddressDetail = (TextView) view.findViewById(R.id.tvAddressDetail);

        if (fields != null) {
            tvCommuneDetail.setText(fields.getCommune());
            tvAddressDetail.setText(fields.getAdresse());
        }

        return view;
    }
}

