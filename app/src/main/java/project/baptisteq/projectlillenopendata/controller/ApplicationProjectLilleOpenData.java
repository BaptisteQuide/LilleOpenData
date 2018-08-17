package project.baptisteq.projectlillenopendata.controller;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.greenrobot.greendao.database.Database;

import io.fabric.sdk.android.Fabric;
import project.baptisteq.projectlillenopendata.bdd.DaoMaster;
import project.baptisteq.projectlillenopendata.bdd.DaoSession;

/**
 * Created by Baptiste on 07/05/18.
 */

public class ApplicationProjectLilleOpenData extends Application {


    private static Bus bus;
    private static final String URL_OPEN_DATA = "https://opendata.lillemetropole.fr/api/records/1.0/search/";

    private static final String NAME_OPENDATA_BDD = "opendata.db";

    /**
     * Objet DAO
     */
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();


        Fabric.with(this, new Crashlytics());
        Stetho.initializeWithDefaults(this);

        bus = new Bus(ThreadEnforcer.ANY);

        setupDatabase();

       // daoSession.getStarFieldsDao().deleteAll();
    }

    /**
     * Création de la base
     */
    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, NAME_OPENDATA_BDD);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static Bus getBus() {
        return bus;
    }

    public static String getUrlOpenData() {
        return URL_OPEN_DATA;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
