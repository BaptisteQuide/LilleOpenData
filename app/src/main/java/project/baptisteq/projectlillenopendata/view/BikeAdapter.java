package project.baptisteq.projectlillenopendata.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.baptisteq.projectlillenopendata.R;
import project.baptisteq.projectlillenopendata.beans.Fields;
import project.baptisteq.projectlillenopendata.beans.FieldsWrapper;
import project.baptisteq.projectlillenopendata.beans.Record;
import project.baptisteq.projectlillenopendata.controller.ApplicationProjectLilleOpenData;

/**
 * Created by Baptiste on 22/05/18.
 */

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewBikeHolder> {

    private List<Record> records;
    private Context context;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public static final String WITH_CB = "AVEC TPE";
    private static final String LIBELLE_KM = " km.";

    public BikeAdapter(List<Record> records, Context context) {
        this.records = records;
        this.context = context;
    }

    /**
     * Création dynamique d'un élément du RecyclerView
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewBikeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bike, parent, false);
        return new BikeAdapter.ViewBikeHolder(view);
    }

    /**
     * Remplissage dynamique d'un élément du RecyclerView
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewBikeHolder holder, int position) {

        final Record record = records.get(position);
        final Fields fields = record.getFields();

        if (fields != null) {
            holder.tvRvBikeName.setText(fields.getNom());
            holder.tvRvEtat.setText(buildTextFromDistance(fields.getDistanceToLocation()));
            holder.tvRvCommune.setText(fields.getCommune());
            holder.tvRvAdresse.setText(fields.getAdresse());
            holder.tvRvStationBikeAvailable.setText(String.valueOf(fields.getNbvelosdispo()));
            holder.tvRvStationBorneAvailable.setText(String.valueOf(fields.getNbplacesdispo()));

            if (!WITH_CB.equals(fields.getType()))
                holder.ivCB.setVisibility(View.INVISIBLE);
            else
                holder.ivCB.setVisibility(View.VISIBLE);

            holder.rootLayoutRvBike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationProjectLilleOpenData.getBus().post(new FieldsWrapper(fields, record.getRecordid()));
                }
            });

            // Here you apply the animation when the view is bound
            setAnimation(holder.itemView, position);
        }

    }

    private void setAnimation(View itemView, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @NonNull
    private String buildTextFromDistance(float roundDistance) {
        StringBuilder builder = new StringBuilder();
        return builder.append(roundDistance).append(LIBELLE_KM).toString();
    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewBikeHolder extends RecyclerView.ViewHolder {

        public View rootLayoutRvBike;
        public TextView tvRvBikeName;
        public ImageView ivCB;
        public TextView tvRvEtat;
        public TextView tvRvCommune;
        public TextView tvRvAdresse;
        public TextView tvRvStationBikeAvailable;
        public TextView tvRvStationBorneAvailable;

        public ViewBikeHolder(View itemView) {
            super(itemView);

            rootLayoutRvBike = (View) itemView.findViewById(R.id.rootLayoutRvBike);
            tvRvBikeName = (TextView) itemView.findViewById(R.id.tvRvBikeName);
            ivCB = (ImageView) itemView.findViewById(R.id.ivCB);
            tvRvEtat = (TextView) itemView.findViewById(R.id.tvRvEtat);
            tvRvCommune = (TextView) itemView.findViewById(R.id.tvRvCommune);
            tvRvAdresse = (TextView) itemView.findViewById(R.id.tvRvAdresse);
            tvRvStationBikeAvailable = (TextView) itemView.findViewById(R.id.tvRvStationBikeAvailable);
            tvRvStationBorneAvailable = (TextView) itemView.findViewById(R.id.tvRvStationBorneAvailable);
        }
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
