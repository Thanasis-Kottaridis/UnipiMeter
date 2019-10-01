package com.unipi.kottarido.unipimeter.unipimeter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//
public class POIsAdapter extends RecyclerView.Adapter<POIsAdapter.POIsHolder> {

    private List<POI> myPOIs;

    private OnItemClickListener listener;

    @NonNull
    @Override
    public POIsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //ftiaxnoume to view pou prepei na dextei o POIs Holder
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.poi_item, viewGroup, false);
        return new POIsHolder(itemView);
    }

    //mesa se auti ti methodo pernoun times antikimena pou exoume dilosei sto POIsHolder
    @Override
    public void onBindViewHolder(@NonNull POIsHolder holder, int i) {
        POI poi = myPOIs.get(i);
        holder.POI_Name.setText(poi.getName());
        holder.POI_Category.setText("Category: "+poi.getCategory());
    }

    @Override
    public int getItemCount() {
        return myPOIs.size();
    }


    class POIsHolder extends RecyclerView.ViewHolder{
        private TextView POI_Name;
        private TextView POI_Category;


        public POIsHolder(@NonNull View itemView) {
            super(itemView);
            POI_Name = itemView.findViewById(R.id.POI_Name);
            POI_Category = itemView.findViewById(R.id.POI_Category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(myPOIs.get(pos));
                    }


                }
            });
        }
    }

    public POIsAdapter(List<POI> myPOIs) {
        this.myPOIs = myPOIs;
    }

    //ga na kanoume to update sto kathe poi xriazomaste event sto onclick tou Recycle view item
    // giafto ftiaxnoume ena functional interface onItemClickListener

    public interface OnItemClickListener{
        void onItemClick(POI poi);
    }

    // episis dimiourgoume kai tin methodo setOnItemClickListener
    // i opoia dexete ena instance pou kanei implement to onItemClickListener
    // gia na ipoxreosoume opoion to kalei na kanei implement to interface mas

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener =listener;
    }

    public void setMyPOIs(List<POI> myPOIs) {
        this.myPOIs = myPOIs;
    }
}
