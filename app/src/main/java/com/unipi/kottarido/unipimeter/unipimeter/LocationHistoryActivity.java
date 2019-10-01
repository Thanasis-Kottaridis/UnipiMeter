package com.unipi.kottarido.unipimeter.unipimeter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LocationHistoryActivity extends AppCompatActivity {

    private DatabaseHelper myDb;

    private RecyclerView LocationHistoryView;
    private RecyclerView.Adapter LocationHistoryAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    private List<LocationHistory> myLocationHistory;
    private List<POI> myPOIs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        myDb = new DatabaseHelper(this);
        readLocationHistory();

        setContentView(R.layout.activity_location_history);

        LocationHistoryView = findViewById(R.id.LocationHistoryView);

        myLayoutManager = new LinearLayoutManager(this);
        LocationHistoryView.setLayoutManager(myLayoutManager);

        LocationHistoryAdapter = new LocationHistoryAdapter(myLocationHistory,myPOIs);
        LocationHistoryView.setAdapter(LocationHistoryAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                LocationHistory locationHistory = myLocationHistory.get(viewHolder.getAdapterPosition());
                int result = myDb.deleteFromLocationHistory(locationHistory.getId());

                if (result != 0) {
                    myLocationHistory.remove(viewHolder.getAdapterPosition());

                    LocationHistoryAdapter = new LocationHistoryAdapter(myLocationHistory, myPOIs);
                    LocationHistoryView.setAdapter(LocationHistoryAdapter);

                    Toast.makeText(getApplicationContext(), "item deleted", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Database connection problem!", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(LocationHistoryView);

    }

    private void readLocationHistory(){
        myLocationHistory = new ArrayList<>();
        myPOIs = new ArrayList<>();

        Cursor result = myDb.getAllFromLocationHistoryJoinPOIs();

        //an den iparxoun apotelesmata
        if (result.getCount() == 0){
            Toast.makeText(this, "There is no Data saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        while (result.moveToNext()){
            myLocationHistory.add(new LocationHistory(result.getInt(result.getColumnIndex(LocationHistory.COL_ID)),
                    result.getString(result.getColumnIndex(LocationHistory.COL_TIMESTAMP)),
                    result.getInt(result.getColumnIndex(LocationHistory.COL_POI_ID))));
            myPOIs.add( new POI(result.getInt(result.getColumnIndex(LocationHistory.COL_POI_ID)),
                    result.getString(result.getColumnIndex(POI.COL_NAME)),
                    result.getDouble(result.getColumnIndex(POI.COL_LATITUDE)),
                    result.getDouble(result.getColumnIndex(POI.COL_LONGITUDE))));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all_manu, menu);
        return true;
    }
    //ti tha ginei sto onclick kathe katigorias tou menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ClearHistory:
                //to kalw etsi gia na diagrapsi oles tis grames tou pinaka
                myDb.deleteFromLocationHistory(-1);
                //adiazei tis listes
                myLocationHistory.clear();
                myPOIs.clear();

                //enimeronei to recycle view
                LocationHistoryAdapter = new LocationHistoryAdapter(myLocationHistory,myPOIs);
                LocationHistoryView.setAdapter(LocationHistoryAdapter);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
