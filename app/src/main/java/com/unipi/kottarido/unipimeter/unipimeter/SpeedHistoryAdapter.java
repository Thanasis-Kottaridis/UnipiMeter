package com.unipi.kottarido.unipimeter.unipimeter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SpeedHistoryAdapter extends RecyclerView.Adapter<SpeedHistoryAdapter.SpeedHistoryHolder> {

    private List<SpeedHistory> mySpeedHistory;

    // ftiaxnw mia inner class SpeedHistoryHolder
    // meso tis mporw na xiristw kathe item/view
    // pou iparxei mesa sto speed_history_view
    public class SpeedHistoryHolder extends RecyclerView.ViewHolder{
        private TextView AlarmDateTime;
        private TextView AlarmSpeed;
        private TextView AlarmLatitude;
        private TextView AlarmLongitude;


        public SpeedHistoryHolder(@NonNull View itemView) {
            super(itemView);

            AlarmDateTime = itemView.findViewById(R.id.AlarmTimestamp);
            AlarmSpeed = itemView.findViewById(R.id.AlarmSpeed);
            AlarmLatitude = itemView.findViewById(R.id.AlarmLatitude);
            AlarmLongitude = itemView.findViewById(R.id.AlarmLongitude);

        }
    }

    public SpeedHistoryAdapter(List<SpeedHistory> mySpeedHistory){
        this.mySpeedHistory = mySpeedHistory;
    }

    @NonNull
    @Override
    public SpeedHistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.speed_history_item,viewGroup,false );
        return new SpeedHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeedHistoryHolder holder, int i) {
        SpeedHistory speedHistory = mySpeedHistory.get(i);
        holder.AlarmDateTime.setText("Alarm Datetime: "+speedHistory.getTimestamp().toString());
        holder.AlarmSpeed.setText("Speed: "+String.valueOf(speedHistory.getSpeed())+" km/h");
        holder.AlarmLatitude.setText("Latitude: "+String.valueOf(speedHistory.getLatitude()));
        holder.AlarmLongitude.setText("Longitude: "+String.valueOf(speedHistory.getLongitude()));

    }

    @Override
    public int getItemCount() {
        return mySpeedHistory.size();
    }

}
