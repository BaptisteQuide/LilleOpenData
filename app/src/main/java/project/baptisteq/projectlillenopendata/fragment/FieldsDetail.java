package project.baptisteq.projectlillenopendata.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.controller.DetailActivity;
import project.baptisteq.projectlillenopendata.view.BikeAdapter;

/**
 * Created by Baptiste on 23/05/18.
 */


/**
 * Fragment de détail d'un Fields
 * Peut être un vélo ou parking, ou autre
 */
public class FieldsDetail extends Fragment {

    private Fields fields;

    private ImageView ivDetailFieldsCB;
    private TextView tvDetailFieldsEtat;
    private TextView tvRvDetailFieldsEtatConnexion;
    private TextView tvDistanceFields;
    private TextView tvDetailFieldsVelosDispos;
    private TextView tvDetailFieldsBornesDispos;

    public FieldsDetail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fields_detail, container, false);

        findViews(view);


        return view;
    }

    private void findViews(View view) {

        ivDetailFieldsCB = (ImageView)view.findViewById( R.id.ivDetailFieldsCB );
        tvDetailFieldsEtat = (TextView)view.findViewById( R.id.tvDetailFieldsEtat );
        tvRvDetailFieldsEtatConnexion = (TextView)view.findViewById( R.id.tvRvDetailFieldsEtatConnexion );
        tvDistanceFields = (TextView)view.findViewById(R.id.tvDistanceFields);
        tvDetailFieldsVelosDispos = (TextView)view.findViewById( R.id.tvDetailFieldsVelosDispos );
        tvDetailFieldsBornesDispos = (TextView)view.findViewById( R.id.tvDetailFieldsBornesDispos );


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fields = (Fields)this.getArguments().getSerializable(DetailActivity.KEY_FIELDS_DETAIL);

        if (!BikeAdapter.WITH_CB.equals(fields.getType()))
            ivDetailFieldsCB.setVisibility(View.INVISIBLE);
        else
            ivDetailFieldsCB.setVisibility(View.VISIBLE);

        tvDetailFieldsEtat.setText(fields.getEtat());
        tvDistanceFields.setText(String.valueOf(fields.getDistanceToLocation()));
        tvRvDetailFieldsEtatConnexion.setText(fields.getEtatconnexion());
        tvDetailFieldsVelosDispos.setText(String.valueOf(fields.getNbvelosdispo()));
        tvDetailFieldsBornesDispos.setText(String.valueOf(fields.getNbplacesdispo()));

    }
}
