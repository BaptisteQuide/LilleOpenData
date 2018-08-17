package project.baptisteq.projectlillenopendata.beans;


import java.io.Serializable;

/**
 * Classe qui embarque un objet Fields et son recordId associé
 * Utile à la gestion des favoris
 */
public class FieldsWrapper implements Serializable {

    public static final long serialVersionUID = 536871008L;

    private Fields fields;
    private String recordId;

    public FieldsWrapper(Fields fields, String recordId) {
        this.fields = fields;
        this.recordId = recordId;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
