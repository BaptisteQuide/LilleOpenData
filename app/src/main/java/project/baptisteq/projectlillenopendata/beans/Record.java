
package project.baptisteq.projectlillenopendata.beans;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Record implements Parcelable, Comparable<Record> {

    private String datasetid;
    private String recordid;
    private Fields fields;
    private Geometry geometry;
    private String record_timestamp;

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getRecord_timestamp() {
        return record_timestamp;
    }

    public void setRecord_timestamp(String record_timestamp) {
        this.record_timestamp = record_timestamp;
    }


    protected Record(Parcel in) {
        datasetid = in.readString();
        recordid = in.readString();
        fields = (Fields) in.readValue(Fields.class.getClassLoader());
        geometry = (Geometry) in.readValue(Geometry.class.getClassLoader());
        record_timestamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(datasetid);
        dest.writeString(recordid);
        dest.writeValue(fields);
        dest.writeValue(geometry);
        dest.writeString(record_timestamp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int compareTo(@NonNull Record o) {
        if (o == null) return 0;

        if (this.getFields() == null || o.getFields() == null) return 0;

        if (this.getFields().getDistanceToLocation() < o.getFields().getDistanceToLocation())
            return -1;

        return 1;
    }
}