package com.unipi.kottarido.unipimeter.unipimeter;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SpeedHistoryActivity extends AppCompatActivity {

    private DatabaseHelper myDb;

    private RecyclerView SpeedHistoryView;
    private RecyclerView.Adapter SpeedHistoryAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private List<SpeedHistory> mySpeedHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_history);

        myDb = new DatabaseHelper(this);
        readSpeedHistory();
       // mySpeedHistory.add(new SpeedHistory("2018-2-2 12:30:22", 50, 37.98042804, 23.66001724));

        setContentView(R.layout.activity_speed_history);

        SpeedHistoryView = findViewById(R.id.SpeedHistoryView);

        myLayoutManager = new LinearLayoutManager(this);
        SpeedHistoryView.setLayoutManager(myLayoutManager);

        SpeedHistoryAdapter = new SpeedHistoryAdapter(mySpeedHistory);
        SpeedHistoryView.setAdapter(SpeedHistoryAdapter);


        //ftiaxnw on swipe left event
        //gia na diagrafi item apo to recycle view
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                SpeedHistory speedHistory = mySpeedHistory.get(viewHolder.getAdapterPosition());
                int result = myDb.deleteFromSpeedHistory(speedHistory.getId());
                if(result!= 0) {
                    mySpeedHistory.remove(viewHolder.getAdapterPosition());

                    //enimeronei to recycle view
                    SpeedHistoryAdapter = new SpeedHistoryAdapter(mySpeedHistory);
                    SpeedHistoryView.setAdapter(SpeedHistoryAdapter);

                    Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Database connection problem!", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(SpeedHistoryView);
    }

    //diavazei ton pinaka SpeedHistory apo tin db
    private void readSpeedHistory(){
        mySpeedHistory = new ArrayList<>();
        Cursor result = myDb.getAllFromSpeedHistory();

        //an i lista einai adia
        if(result.getCount() == 0){
            Toast.makeText(this, "There is no speed alarms saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        while (result.moveToNext()){
            mySpeedHistory.add(new SpeedHistory(result.getInt(result.getColumnIndex(SpeedHistory.COL_ID)),
                    result.getString(result.getColumnIndex(SpeedHistory.COL_TIMESTAMP)),
                    result.getInt(result.getColumnIndex(SpeedHistory.COL_SPEED)), result.getDouble(result.getColumnIndex(SpeedHistory.COL_LATITUDE)),
                    result.getDouble(result.getColumnIndex(SpeedHistory.COL_LONGITUDE))));
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
                myDb.deleteFromSpeedHistory(-1);
                //adiazei tis listes
                mySpeedHistory.clear();

                //enimeronei to recycle view
                SpeedHistoryAdapter = new SpeedHistoryAdapter(mySpeedHistory);
                SpeedHistoryView.setAdapter(SpeedHistoryAdapter);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
