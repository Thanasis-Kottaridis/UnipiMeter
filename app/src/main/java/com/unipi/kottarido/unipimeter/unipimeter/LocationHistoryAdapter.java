package com.unipi.kottarido.unipimeter.unipimeter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.LocationHistoryHolder> {

    private List<LocationHistory> myLocationHistory;
    private List<POI> myPOIs;

    public class LocationHistoryHolder extends RecyclerView.ViewHolder{

        private TextView LocationName;
        private TextView LocationTimestamp;
        private TextView LocationLatitude;
        private TextView LocationLongitude;

        public LocationHistoryHolder (@NonNull View itemView) {
            super(itemView);

            LocationName = itemView.findViewById(R.id.LocationName);
            LocationTimestamp = itemView.findViewById(R.id.LocationTimestamp);
            LocationLatitude = itemView.findViewById(R.id.LocationLatitude);
            LocationLongitude = itemView.findViewById(R.id.LocationLongitude);
        }
    }

    @NonNull
    @Override
    public LocationHistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //ftiaxnoume to view pou dexete i LocationHistory Holder
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_history_item,viewGroup,false );
        return new LocationHistoryHolder(itemView);
    }

    //se auti ti methodo arxikopiounte ta instances pou exoun dilothei sto LocationHistory Holder
    // gia kathe antikimeno tis listas myLocationHistory
    @Override
    public void onBindViewHolder(@NonNull LocationHistoryHolder holder, int i) {
        LocationHistory locationHistory = myLocationHistory.get(i);
        POI poi = myPOIs.get(i);
        holder.LocationName.setText("Location: "+poi.getName());
        holder.LocationTimestamp.setText("Datetime: "+ locationHistory.getTimestamp());
        holder.LocationLatitude.setText("Latitude: "+String.valueOf(poi.getLatitude()));
        holder.LocationLongitude.setText("Longitude: "+String.valueOf(poi.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return myLocationHistory.size();
    }

    public LocationHistoryAdapter(List<LocationHistory> myLocationHistory , List<POI> myPOIs){
        this.myLocationHistory = myLocationHistory;
        this.myPOIs = myPOIs;
    }

    public void test(){}
}
