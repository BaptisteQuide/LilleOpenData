
package project.baptisteq.projectlillenopendata.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Geometry implements Parcelable {

    private String type;
    private List<Float> coordinates = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Float> coordinates) {
        this.coordinates = coordinates;
    }


    protected Geometry(Parcel in) {
        type = in.readString();
        if (in.readByte() == 0x01) {
            coordinates = new ArrayList<Float>();
            in.readList(coordinates, Float.class.getClassLoader());
        } else {
            coordinates = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        if (coordinates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(coordinates);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Geometry> CREATOR = new Parcelable.Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}