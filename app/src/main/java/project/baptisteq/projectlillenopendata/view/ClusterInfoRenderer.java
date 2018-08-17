package project.baptisteq.projectlillenopendata.view;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;

/**
 * Created by Baptiste on 22/05/18.
 */

public class ClusterInfoRenderer extends DefaultClusterRenderer<Fields> {

    /**
     * Point saisi avant un changement de configuration d'écran
     */
    private Fields currentFields;

    public ClusterInfoRenderer(Context context, GoogleMap map, ClusterManager<Fields> clusterManager, Fields currentFields) {
        super(context, map, clusterManager);
        this.currentFields = currentFields;
    }

    @Override
    protected void onClusterItemRendered(Fields clusterItem, Marker marker) {

        if (currentFields != null && currentFields.getPosition().equals(marker.getPosition()))
            marker.showInfoWindow();

        marker.setTag(clusterItem);

        super.onClusterItemRendered(clusterItem, marker);
    }

    @Override
    protected void onBeforeClusterItemRendered(Fields item, MarkerOptions markerOptions) {

        /* Par défaut */
        BitmapDescriptor bitmapDescriptor = null;

        if (item.getNbvelosdispo() == 0)
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_nok);
        else if (item.getNbplacesdispo() == 0)
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_full);
        else
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_ok);

        markerOptions.icon(bitmapDescriptor);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
