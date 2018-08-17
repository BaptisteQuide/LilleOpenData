package project.baptisteq.projectlillenopendata.obs;

import java.util.List;

import project.baptisteq.projectlillenopendata.beans.Record;

public interface IObservable {

    public void addObserver(IObserver observer);
    public void notifyAllObserver(List<Record> records);

}
