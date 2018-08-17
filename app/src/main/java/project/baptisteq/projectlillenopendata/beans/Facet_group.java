
package project.baptisteq.projectlillenopendata.beans;

import java.util.List;

public class Facet_group {

    private String name;
    private List<Facet> facets = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

}
