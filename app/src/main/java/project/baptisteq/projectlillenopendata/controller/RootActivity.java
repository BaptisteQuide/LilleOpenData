package project.baptisteq.projectlillenopendata.controller;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import project.baptisteq.projectlillenopendata.R;

/**
 * Created by Baptiste on 27/05/18.
 */

/**
 * Activité principale qui embarque la gestion de la Toolbar et du NavigationDrawer
 * Impossible de l'instancier. Doit-être étendu pour être utilisé
 */

public abstract class RootActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected OnNavigationItemSelectedListenerOpenData onNavigationItemSelectedListenerOpenData;
    protected ActionBar actionbar;
    protected TextView tvToolbarTitle;
    protected Toolbar toolbar;

    protected void findViews(String titleToolbar, final int idMenuToShow) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerRoot);

        setUpToolbar(titleToolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        MenuItem itemBike = navigationView.getMenu().findItem(idMenuToShow);
        itemBike.setChecked(true);
        onNavigationItemSelectedListenerOpenData = new OnNavigationItemSelectedListenerOpenData(this, drawerLayout);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListenerOpenData);
    }

    protected void setUpToolbar(String nameToolbar) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvToolbarTitle.setText(nameToolbar);

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        /* Register to the bus */
        ApplicationProjectLilleOpenData.getBus().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        /* Unregister to the bus */
        ApplicationProjectLilleOpenData.getBus().unregister(this);
    }

}
