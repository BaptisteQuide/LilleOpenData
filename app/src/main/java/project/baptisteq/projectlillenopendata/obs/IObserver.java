package project.baptisteq.projectlillenopendata.obs;

import java.util.List;

import project.baptisteq.projectlillenopendata.beans.Record;

/**
 * Interface pour les fragments qui sont à l'écoute des updates
 */
public interface IObserver {

    public void notify(List<Record> records);

}
