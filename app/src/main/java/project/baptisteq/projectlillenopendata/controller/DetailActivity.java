package project.baptisteq.projectlillenopendata.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.exception.DAOException;
import project.baptisteq.projectlillenopendata.fragment.FieldsDetail;
import project.baptisteq.projectlillenopendata.fragment.MapsDetail;
import project.baptisteq.projectlillenopendata.utils.LogUtils;
import project.baptisteq.projectlillenopendata.utils.StarUtils;

public class DetailActivity extends AppCompatActivity {


    /**
     * Clé de chargement du Fields lu
     */
    public static final String KEY_FIELDS_DETAIL = "KEY_FIELDS_DETAIL";
    public static final String KEY_RECORDID_DETAIL = "KEY_RECORDID_DETAIL";
    private static final String KEY_ISSTAR_DETAIL = "KEY_ISSTAR_DETAIL";


    private static final String KEY_FRAG_MAPDETAILS = "FRAG_MAPDETAILS";
    private static final String KEY_FRAG_FIELDSDETAILS = "FRAG_FIELDSDETAILS";

    private Fields fields;
    private String recordId;

    boolean isStar;

    private TextView tvToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();

        findParameters(savedInstanceState, intent);

        setUpToolbar();

        /**
         * Création des fragments
         */
        createFragments(intent, savedInstanceState);
    }

    private void findParameters(Bundle savedInstanceState, Intent intent) {
        if (savedInstanceState != null) {
            fields = (Fields) savedInstanceState.getParcelable(KEY_FIELDS_DETAIL);
            recordId = (String) savedInstanceState.getSerializable(KEY_RECORDID_DETAIL);
            isStar = (boolean) savedInstanceState.getSerializable(KEY_ISSTAR_DETAIL);

        } else {
            fields = (Fields) intent.getExtras().getSerializable(KEY_FIELDS_DETAIL);
            recordId = (String) intent.getExtras().getSerializable(KEY_RECORDID_DETAIL);
            isStar = StarUtils.isStarFields(recordId);
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        TextView tvToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvToolbarTitle.setText(fields.getNom());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        MenuItem itemInfo = menu.findItem(R.id.action_info);
        MenuItem itemRefresh = menu.findItem(R.id.action_refresh);
        MenuItem itemStar = menu.findItem(R.id.action_star);

        itemSearch.setVisible(false);
        itemInfo.setVisible(false);
        itemRefresh.setVisible(false);

        setStarIconIfNeeded(itemStar);

        return true;
    }

    private void setStarIconIfNeeded(MenuItem itemStar) {
        if (isStar)
            itemStar.setIcon(R.drawable.ic_fav_yes);
        else
            itemStar.setIcon(R.drawable.ic_fav_no);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.action_star) {
            addStarFields(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addStarFields(MenuItem item) {


        /** Suppression */
        if (isStar) {
            StarUtils.deleteStarFields(recordId);
        } else {
            if (StarUtils.addStarFields(recordId) == 0) {
                Toast.makeText(this, "Unable to add fav", Toast.LENGTH_SHORT).show();
                LogUtils.logException(new DAOException(recordId));
                return;
            }
        }

        isStar = !isStar;

        setStarIconIfNeeded(item);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_FIELDS_DETAIL, fields);
        outState.putSerializable(KEY_RECORDID_DETAIL, recordId);
        outState.putSerializable(KEY_ISSTAR_DETAIL, isStar);
    }

    /**
     * Création des fraguments
     * On remplace les FrameLayout afin de passer des arguments en paramètres
     *
     * @param intent
     * @param savedInstanceState
     */
    private void createFragments(Intent intent, Bundle savedInstanceState) {
        MapsDetail mapsDetailFragment = new MapsDetail();
        mapsDetailFragment.setArguments(intent.getExtras());

        FieldsDetail fieldsDetailFragment = new FieldsDetail();
        fieldsDetailFragment.setArguments(intent.getExtras());


        /**
         * Protection lors de la création des fragments
         * En effet, lors d'une rotation d'écran, certains comportements peuvent être innatendus
         */
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.flMapsDetail, mapsDetailFragment, KEY_FRAG_MAPDETAILS).add(R.id.flFieldsDetail, fieldsDetailFragment, KEY_FRAG_FIELDSDETAILS).commit();
    }

}
