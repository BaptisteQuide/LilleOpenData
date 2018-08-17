package project.baptisteq.projectlillenopendata.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.fragment.BikeStarList;

public class StarActivity extends RootActivity implements SearchView.OnQueryTextListener {

    private static final String KEY_FRAG_LISTSTAR = "KEY_FRAG_LISTSTAR";
    public static final String KEY_ALL_RECORDS = "KEY_ALL_RECORDS";


    ArrayList<Record> records;
    private BikeStarList bikeStarListFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_star_layout);

        /**
         * Récupération de l'ensemble des views
         */
        findViews(getString(R.string.libelle_fav), R.id.nav_fav);

        createFragments(this.getIntent(), savedInstanceState);
    }

    /**
     * Création des fraguments
     * On remplace les FrameLayout afin de passer des arguments en paramètres
     *
     * @param intent
     * @param savedInstanceState
     */
    private void createFragments(Intent intent, Bundle savedInstanceState) {
        bikeStarListFragment = new BikeStarList();

        /**
         * Protection lors de la création des fragments
         * En effet, lors d'une rotation d'écran, certains comportements peuvent être innatendus
         */
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.flStarList, bikeStarListFragment, KEY_FRAG_LISTSTAR).commit();

        if (savedInstanceState == null)
            records = intent.getParcelableArrayListExtra(KEY_ALL_RECORDS);
        else
            records = savedInstanceState.getParcelableArrayList(KEY_ALL_RECORDS);

        bikeStarListFragment.notify(records);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_ALL_RECORDS, records);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) itemSearch.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_ville));

        MenuItem itemStar = menu.findItem(R.id.action_star);
        itemStar.setVisible(false);

        MenuItem itemInfo = menu.findItem(R.id.action_info);
        itemInfo.setVisible(false);

        MenuItem itemStarNav = menu.findItem(R.id.nav_fav);
        itemStar.setChecked(true);

        return true;
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
                //this.loadBikesOnRealTime();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
