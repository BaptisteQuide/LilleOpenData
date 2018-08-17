package project.baptisteq.projectlillenopendata.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.beans.FieldsWrapper;
import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.beans.ResultOpen;
import project.baptisteq.projectlillenopendata.exception.APIFailureException;
import project.baptisteq.projectlillenopendata.obs.IObservable;
import project.baptisteq.projectlillenopendata.obs.IObserver;
import project.baptisteq.projectlillenopendata.utils.FloatingUtils;
import project.baptisteq.projectlillenopendata.utils.LogUtils;
import project.baptisteq.projectlillenopendata.wsapi.OpenDataLilleAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activite qui se charge d'afficher les vélos disponibles
 */
public class MapsBikeActivity extends RootActivity implements IObservable, SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener {

    /**
     * Field click onCluster
     */
    private Fields currentFields;

    private MenuItem itemSearch;
    private TabLayout tabLayout;
    private PagerAdapterBike pagerAdapter;

    private static final int RESULT_CODE_DETAIL = 500;
    private static final int REQUEST_CODE_LOCATION = 400;

    private static final int DEFAULT_TIMEOUT = 5;

    /**
     * Différentes positions dans le ViewPager
     */
    private static final int POSITION_MAPS = 0;
    private static final int POSITION_LIST = 1;

    public static final String KEY_RECORD_LIST = "KEY_SAVE_RECORD_LIST";
    public static final String KEY_CURRENTFIELD = "KEY_SAVE_CURRENTFIELD";

    /**
     * Liste des observateurs des événements récupération de données
     */
    private List<IObserver> observers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_bike);

        observers = new ArrayList<IObserver>();
        /**
         * Récupération de l'ensemble des views
         */
        findViews(getString(R.string.vlille), R.id.nav_bike);

        /**
         * Demande de permission de localisation
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);

        /* Si premier passage, chargement des vélos en temps réel */
        if (savedInstanceState == null)
            loadBikesOnRealTime();

    }



    @Override
    protected void findViews(String titleToolbar, final int idNavToShow) {

        super.findViews(titleToolbar, idNavToShow);

        ViewPager pager = setUpViewPager();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_maps);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_list);
    }

    @NonNull
    private ViewPager setUpViewPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapterBike(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(this);

        observers.clear();

        for (Fragment fragment : pagerAdapter.getFragments())
            this.addObserver((IObserver)fragment);

        return pager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        showItemSearchIfNeeded(menu);
        return true;
    }

    private void showItemSearchIfNeeded(Menu menu) {
        itemSearch = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) itemSearch.getActionView();

        if (tabLayout.getSelectedTabPosition() == POSITION_MAPS)
            itemSearch.setVisible(false);
        else
            itemSearch.setVisible(true);

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_ville));

        MenuItem itemStar = menu.findItem(R.id.action_star);
        itemStar.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_refresh:
                /**
                 * Avertissement des fragments avec un refresher
                 */

                this.loadBikesOnRealTime();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadBikesOnRealTime() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationProjectLilleOpenData.getUrlOpenData())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenDataLilleAPI openDataLilleAPIImpl = retrofit.create(OpenDataLilleAPI.class);
        openDataLilleAPIImpl.getBikesOnRealTime().enqueue(new Callback<ResultOpen>() {
            @Override
            public void onResponse(Call<ResultOpen> call, Response<ResultOpen> response) {
                /* On poste un message sur le bus pour avertir les fragments */
                if (response.body() != null && response.body().getRecords() != null) {
                    Location currentLocation = getCurrentLocation();
                    if (currentLocation != null) {
                        fillFieldsWithDistanceToCurrentLocation(response, currentLocation);
                    }

                    /**
                     * Notifie l'ensemble des fragments que les vélos sont prêts
                     */
                    notifyAllObserver(response.body().getRecords());
                    onNavigationItemSelectedListenerOpenData.setCurrentRecords((ArrayList)response.body().getRecords());
                }
                /**
                 * Rien n'a été récupéré depuis le serveur
                 */
                else {
                    openSnackbarWhenFailure(getString(R.string.records_null_from_server));
                    notifyAllObserver(null);
                }
                LogUtils.logTemp(response.toString());
            }

            @Override
            public void onFailure(Call<ResultOpen> call, Throwable t) {
                openSnackbarWhenFailure(t.getMessage());
                notifyAllObserver(null);
            }
        });
    }

    /**
     * Calcule la distance entre la localisation actuelle et l'ensemble des points données sur la carte
     * @param response
     * @param currentLocation
     */
    private void fillFieldsWithDistanceToCurrentLocation(Response<ResultOpen> response, Location currentLocation) {
        for (Record record : response.body().getRecords()) {
            Fields fields = record.getFields();
            if (fields != null) {
                LatLng position = fields.getPosition();
                float[] results = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), position.latitude, position.longitude, results);
                float roundingDistance = FloatingUtils.roundWithTwoDecimals(results[0] / 1000.0);
                fields.setDistanceToLocation(roundingDistance);
            }
        }
    }

    /**
     * Retourne la location courante si l'utilisateur a activé la mise à disposition de sa position
     * @return
     */
    private Location getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (ActivityCompat.checkSelfPermission(MapsBikeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
        }
        return location;
    }

    private void openSnackbarWhenFailure(String message) {
        APIFailureException e = new APIFailureException(message);
        LogUtils.logException(new APIFailureException(message));
                /* Avertissement des fragments */
        ApplicationProjectLilleOpenData.getBus().post(e);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayoutMapsBike), R.string.no_internet_message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBikesOnRealTime();
            }
        });
        snackbar.show();
    }

    /**
     * Méthode appelé par le bus lors d'un click sur un champ
     * @param fieldsWrapper
     */
    @Subscribe
    public void onClickedBike(final FieldsWrapper fieldsWrapper) {
        Intent intentDetailActivity = new Intent(this, DetailActivity.class);
        intentDetailActivity.putExtra(DetailActivity.KEY_FIELDS_DETAIL, (Serializable)fieldsWrapper.getFields());
        intentDetailActivity.putExtra(DetailActivity.KEY_RECORDID_DETAIL, fieldsWrapper.getRecordId());
        startActivityForResult(intentDetailActivity, RESULT_CODE_DETAIL);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ApplicationProjectLilleOpenData.getBus().post(newText);
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do nothing
    }

    @Override
    public void onPageSelected(int position) {

        if (itemSearch != null) {
            if (position == 1 || position == 2)
                itemSearch.setVisible(true);
            else
                itemSearch.setVisible(false);
        }

        IObserver observer = (IObserver)pagerAdapter.getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    /**
     * Méthode d'avertissement des observateurs
     * On leur transmet à l'ensemble leschamps récupérés
     * @param records
     */
    @Override
    public void notifyAllObserver(List<Record> records) {
        for (IObserver observer : observers)


            observer.notify(records);
    }
}
