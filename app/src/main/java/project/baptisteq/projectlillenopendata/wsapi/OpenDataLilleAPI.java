package project.baptisteq.projectlillenopendata.wsapi;

import project.baptisteq.projectlillenopendata.beans.ResultOpen;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Baptiste on 12/05/18.
 * Interface qui regroupe l'ensemble des fonctions d'appels aux WS de la ville de Lille
 */

public interface OpenDataLilleAPI {

    @GET("?dataset=vlille-realtime&rows=300&facet=libelle&facet=nom&facet=commune&facet=etat&facet=type&facet=etatconnexion")
    Call<ResultOpen> getBikesOnRealTime();

    @GET("?dataset=disponibilite-parkings&facet=libelle&facet=ville&facet=etat")
    Call<ResultOpen> getAvailableParkingOnRealTime();


}
