package project.baptisteq.projectlillenopendata.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Record;

/**
 * Created by Baptiste on 12/05/18.
 * Listener de réponse aux événements du DrawerLayout
 */

public class OnNavigationItemSelectedListenerOpenData implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout drawerLayout;

    private ArrayList<Record> currentRecords;

    /**
     * Constante globale pour la demande d'informations
     */
    private static final String MAIN_MAIL_APROPOS = "baptistequide@gmail.com";
    private static final String MAIN_MAIL_SUBJECT = "Demande d'informations";


    public OnNavigationItemSelectedListenerOpenData(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        /**
         * Do nothing
         */
        if (item.isChecked()) {
            drawerLayout.closeDrawers();
            return true;
        }

        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        drawerLayout.closeDrawers();

        /*
        Si l'utilisateur est déjà présent, on ne réalise aucune action
         */

        /**
         * Lancement de l'activité Carte pour géolocalisation des vélos
         */
        if (!item.isChecked() && item.getItemId() == R.id.nav_bike) {
            Intent intent = new Intent(context, MapsBikeActivity.class);
            context.startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_parking) {
            Toast.makeText(context, "Parking", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.nav_fav) {
            Intent intent = new Intent(context, StarActivity.class);
            intent.putParcelableArrayListExtra(StarActivity.KEY_ALL_RECORDS,currentRecords);
            context.startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_apropos) {
            openMailApp();
        }

        return true;
    }

    /**
     * Ouvre une activité afin d'envoyer un mail
     * Demande d'informations
     */
    private void openMailApp() {
        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{MAIN_MAIL_APROPOS});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, MAIN_MAIL_SUBJECT);

        /* Send it off to the Activity-Chooser */
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void setCurrentRecords(ArrayList<Record> currentRecords) {
        this.currentRecords = currentRecords;
    }
}
