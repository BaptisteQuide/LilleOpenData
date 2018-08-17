package project.baptisteq.projectlillenopendata.view;

import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;

/**
 * Classe générique afin de modifier la fenêtre popup d'un marker
 */
public abstract class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter  {

    protected LayoutInflater layoutInflater;

    public InfoWindowAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }
}
