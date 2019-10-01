package com.unipi.kottarido.unipimeter.unipimeter;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper myDb;

    private TextView SpeedLimitViolations;
    private TextView TotalPOIViews;
    private TextView FavoritePOICategory;
    private TextView FavoritePOICategoryViews;
    private TextView POIName;
    private TextView POILatitude;
    private TextView POILongitude;
    private TextView FavPOIViews;
    private Spinner DateRange;

    //einai poses meres apexei pao tin simerini kathe diastima me tin sira pou exoun oristi sto stings.xml
    private int[] dates = new int[]{0,1,7,31,366,-1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        myDb = new DatabaseHelper(this);

        SpeedLimitViolations = findViewById(R.id.SpeedLimitViolations_Statistics);
        TotalPOIViews = findViewById(R.id.TotalPOIViews_Statistics);
        FavoritePOICategory = findViewById(R.id.FavoritePOICategory_Statistics);
        FavoritePOICategoryViews = findViewById(R.id.FavoritePOICategoryViews_Statistics);
        POIName = findViewById(R.id.POIName_Statistics);
        POILatitude = findViewById(R.id.POILatitude_Statistics);
        POILongitude = findViewById(R.id.POILongitude_Statistics);
        FavPOIViews = findViewById(R.id.FavoritePOIViews_Statistics);
        DateRange = findViewById(R.id.DateRangeSpinner_Statistics);

        setSpeedLimitViolations(dates[0]);
        setTotalPOIViews(dates[0]);
        setFavoritePOICategory(dates[0]);
        setFavoritePOI(dates[0]);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.statistics_date_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DateRange.setAdapter(adapter);


        DateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpeedLimitViolations(dates[position]);
                setTotalPOIViews(dates[position]);
                setFavoritePOICategory(dates[position]);
                setFavoritePOI(dates[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //epistrefei kai emfanizei ton sinoliko arithmo paraviasis tou oriou mesta se ena xroniko diastima
    public void setSpeedLimitViolations(int interval){
        Cursor result = myDb.getSpeedLimitViolations(interval);


        //an den iparxoun apotelesmata
        if (result.getCount() == 0){
            Toast.makeText(this, "There is no Data saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        result.moveToFirst();
        SpeedLimitViolations.setText(String.valueOf(result.getInt(0)));
    }
    //epistreuei kai emfanizei poses fores vrethike o xristis konta se ena poi mesa se ena xroniko diastima
    public void setTotalPOIViews(int interval){
        Cursor result = myDb.getTotalPOIsViews(interval);


        //an den iparxoun apotelesmata
        if (result.getCount() == 0){
            Toast.makeText(this, "There is no Data saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        result.moveToFirst();
        TotalPOIViews.setText(String.valueOf(result.getInt(0)));
    }

    //emfanizei tin agapimeni POI  katigoria kai ta views tis
    public void setFavoritePOICategory(int interval){
        Cursor result = myDb.getFavoritePOICategory(interval);


        //an den iparxoun apotelesmata
        if (result.getCount() == 0){
            Toast.makeText(this, "There is no Data saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        result.moveToFirst();
        FavoritePOICategory.setText("Category: "+result.getString(0));
        FavoritePOICategoryViews.setText("Views: "+String.valueOf(result.getInt(1)));
    }

    public void  setFavoritePOI(int interval){
        Cursor result = myDb.getFavoritePOI(interval);


        //an den iparxoun apotelesmata
        if (result.getCount() == 0){
            Toast.makeText(this, "There is no Data saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        result.moveToFirst();
        POIName.setText("Name: "+result.getString(0));
        POILatitude.setText("Latitude: "+String.valueOf(result.getDouble(1)));
        POILongitude.setText("Longitude: "+String.valueOf(result.getDouble(2)));
        FavPOIViews.setText("Views: "+result.getString(3));
    }
}
