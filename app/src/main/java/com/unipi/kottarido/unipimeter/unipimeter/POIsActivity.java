package com.unipi.kottarido.unipimeter.unipimeter;

import android.content.Intent;
import android.database.Cursor;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class POIsActivity extends AppCompatActivity {

    public static final int ADD_POI_REQUEST = 1;
    public static final int EDIT_POI_REQUEST = 2;

    private DatabaseHelper myDb;

    private RecyclerView POIsView;
    private RecyclerView.Adapter POIsAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private List<POI> myPOIs;
    private FloatingActionButton AddPOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pois);

        myDb = new DatabaseHelper(this);

        //fortonei ta pois apo tin db
        readPOIs();


//        //for (int i = 0; i < 10; i++)
//        myPOIs.add(new POI("My Home", "Home", "my home", 37.98042804, 23.66001724));

        setContentView(R.layout.activity_pois);

        POIsView = findViewById(R.id.POIsView);

        myLayoutManager = new LinearLayoutManager(this);
        POIsView.setLayoutManager(myLayoutManager);

        POIsAdapter = new POIsAdapter(myPOIs);
        POIsView.setAdapter(POIsAdapter);

        //arxikopio to FAB kai ftiaxnw to onClick Event tou
        AddPOI = findViewById(R.id.AddPOI);
        AddPOI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddPoiActivity.class);
                startActivityForResult(intent, ADD_POI_REQUEST);
            }
        });

        POIsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && AddPOI.getVisibility() == View.VISIBLE)
                    AddPOI.hide();
                else if(dy < 0 && AddPOI.getVisibility() != View.VISIBLE)
                    AddPOI.show();
            }
        });



        //On swipe left event

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i){
                POI poi = myPOIs.get(viewHolder.getAdapterPosition());
                int result = myDb.deleteFromPOIs(poi.getId());
                if (result != 0 ){
                    myPOIs.remove(viewHolder.getAdapterPosition());

                    ((POIsAdapter) POIsAdapter).setMyPOIs(myPOIs);
                    POIsView.setAdapter(POIsAdapter);

                    Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Database connection problem!", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(POIsView);

        //on Recycle view item click event einai to interface pou ftia3ame ston adapter
        //to xirizomaste opos xirizomaste kai ta apla onclick event

        ((POIsAdapter) POIsAdapter).setOnItemClickListener(new POIsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(POI poi) {

                Intent intent = new Intent(getApplicationContext(),AddPoiActivity.class);

                intent.putExtra(AddPoiActivity.EXTRA_POI_ID, poi.getId());
                intent.putExtra(AddPoiActivity.EXTRA_POI_NAME, poi.getName());
                intent.putExtra(AddPoiActivity.EXTRA_POI_CATEGORY, poi.getCategory());
                intent.putExtra(AddPoiActivity.EXTRA_POI_DESCRIPTION, poi.getDescription());
                intent.putExtra(AddPoiActivity.EXTRA_POI_LATITUDE, poi.getLatitude());
                intent.putExtra(AddPoiActivity.EXTRA_POI_LONGITUDE, poi.getLongitude());

                startActivityForResult(intent, EDIT_POI_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_POI_REQUEST && resultCode == RESULT_OK){

            readPOIs();

            //prosthetw to POI sto Recycle View
            ((POIsAdapter) POIsAdapter).setMyPOIs(myPOIs);
            POIsView.setAdapter(POIsAdapter);

        }
        else if(requestCode == EDIT_POI_REQUEST && resultCode == RESULT_OK){
            Toast.makeText(this, "Point Of Interest Updated", Toast.LENGTH_LONG).show();

            readPOIs();

            //prosthetw to POI sto Recycle View
            ((POIsAdapter) POIsAdapter).setMyPOIs(myPOIs);
            POIsView.setAdapter(POIsAdapter);
        }
        else{
            Toast.makeText(this,"POI not Saved!", Toast.LENGTH_LONG).show();
        }
    }

    //diavazei ton pinaka POI apo tin db
    private void readPOIs(){

        myPOIs = new ArrayList<>();
        Cursor result = myDb.getAllFromPOI();
        //an i lista einai adia
        if(result.getCount() == 0){
            Toast.makeText(this, "There is no POIs saved in the system", Toast.LENGTH_LONG).show();
            return ;
        }

        while (result.moveToNext()){
            myPOIs.add(new POI(result.getInt(result.getColumnIndex(POI.COL_ID)), result.getString(result.getColumnIndex(POI.COL_NAME)) ,
                    result.getString(result.getColumnIndex(POI.COL_CATEGORY)), result.getString(result.getColumnIndex(POI.COL_DESCRIPTION)),
                    result.getDouble(result.getColumnIndex(POI.COL_LATITUDE)), result.getDouble(result.getColumnIndex(POI.COL_LONGITUDE))));
        }
    }

    //ftiaxnei to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all_manu, menu);
        return true;
    }

    //ti tha ginei sto onclick kathe katigorias tou menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ClearHistory:
                //to kalw etsi gia na diagrapsi oles tis grames tou pinaka
                myDb.deleteFromPOIs(-1);
                //adiazei tis listes
                myPOIs.clear();

                //enimeronei to recycle view
                ((POIsAdapter) POIsAdapter).setMyPOIs(myPOIs);
                POIsView.setAdapter(POIsAdapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //event Sto patima tou default back button tou android
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }


}
