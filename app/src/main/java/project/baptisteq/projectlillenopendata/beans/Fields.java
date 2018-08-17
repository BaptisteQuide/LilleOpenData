
package project.baptisteq.projectlillenopendata.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fields implements ClusterItem, Parcelable, Serializable {

    public static final long serialVersionUID = 536871008L;

    private String etat;
    private String commune;
    private Integer nbvelosdispo;
    private Integer nbplacesdispo;
    private String nom;
    private String etatconnexion;
    private Integer libelle;
    private List<Float> geo = null;
    private String adresse;
    private String type;

    private float distanceToLocation;
    private String referenceRecordId;

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public Integer getNbvelosdispo() {
        return nbvelosdispo;
    }

    public void setNbvelosdispo(Integer nbvelosdispo) {
        this.nbvelosdispo = nbvelosdispo;
    }

    public Integer getNbplacesdispo() {
        return nbplacesdispo;
    }

    public void setNbplacesdispo(Integer nbplacesdispo) {
        this.nbplacesdispo = nbplacesdispo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEtatconnexion() {
        return etatconnexion;
    }

    public void setEtatconnexion(String etatconnexion) {
        this.etatconnexion = etatconnexion;
    }

    public Integer getLibelle() {
        return libelle;
    }

    public void setLibelle(Integer libelle) {
        this.libelle = libelle;
    }

    public List<Float> getGeo() {
        return geo;
    }

    public void setGeo(List<Float> geo) {
        this.geo = geo;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public LatLng getPosition() {
        if (this.getGeo() == null)
            return null;

        return new LatLng(this.getGeo().get(0), this.getGeo().get(1));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public float getDistanceToLocation() {
        return distanceToLocation;
    }

    public void setDistanceToLocation(float distanceToLocation) {
        this.distanceToLocation = distanceToLocation;
    }

    public void setReferenceRecordId(String referenceRecordId) {
        this.referenceRecordId = referenceRecordId;
    }

    public String getReferenceRecordId() {
        return referenceRecordId;
    }

    protected Fields(Parcel in) {
        etat = in.readString();
        commune = in.readString();
        nbvelosdispo = in.readByte() == 0x00 ? null : in.readInt();
        nbplacesdispo = in.readByte() == 0x00 ? null : in.readInt();
        nom = in.readString();
        etatconnexion = in.readString();
        libelle = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            geo = new ArrayList<Float>();
            in.readList(geo, Float.class.getClassLoader());
        } else {
            geo = null;
        }
        adresse = in.readString();
        type = in.readString();
        distanceToLocation = in.readFloat();
        referenceRecordId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(etat);
        dest.writeString(commune);
        if (nbvelosdispo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(nbvelosdispo);
        }
        if (nbplacesdispo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(nbplacesdispo);
        }
        dest.writeString(nom);
        dest.writeString(etatconnexion);
        if (libelle == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(libelle);
        }
        if (geo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(geo);
        }
        dest.writeString(adresse);
        dest.writeString(type);
        dest.writeFloat(distanceToLocation);
        dest.writeString(referenceRecordId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Fields> CREATOR = new Parcelable.Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel in) {
            return new Fields(in);
        }

        @Override
        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };
}

