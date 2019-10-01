package com.unipi.kottarido.unipimeter.unipimeter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class AddPoiActivity extends AppCompatActivity {

    public static final String EXTRA_POI_ID = "com.unipi.kottarido.unipimeter.unipimeter.EXTRA_POI_ID";
    public static final String EXTRA_POI_NAME = "com.unipi.kottarido.unipimeter.unipimeter.EXTRA_POI_NAME";
    public static final String EXTRA_POI_CATEGORY = "com.unipi.kottarido.unipimeter.unipimeter.EXTRA_POI_CATEGORY";
    public static final String EXTRA_POI_LATITUDE = "com.unipi.kottarido.unipimeter.unipimeter.EXTRA_POI_LATITUDE";
    public static final String EXTRA_POI_LONGITUDE = "com.unipi.kottarido.unipimeter.unipimeter.EXTRA_POI_LONGITUDE";
    public static final String EXTRA_POI_DESCRIPTION = "com.unipi.kottarido.unipimeter.unipimeter.EXTRA_POI_DESCRIPTION";

    private DatabaseHelper myDb;

    Intent intent;

    private TextView POI_Name;
    private Spinner POI_Category;
    private TextView POI_Latitude;
    private TextView POI_Longitude;
    private TextView POI_Description;

    private boolean Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poi);

        POI_Name = findViewById(R.id.POI_Name);
        POI_Category = findViewById(R.id.POI_Category);
        POI_Latitude = findViewById(R.id.POI_Latitude);
        POI_Longitude = findViewById(R.id.POI_Longitude);
        POI_Description = findViewById(R.id.POI_Description);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.POIsCategories,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        POI_Category.setAdapter(adapter);

        //gia na emfanistei to close sto menu
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //elenxoume an auto to activity klithike agia add i gia update a POI
        //pernoume to intent pou klithike to activity
        intent = getIntent();

        //an sto intent iparxei apothikeumenei i timi tou ID simeni oti klithike gia update
        if (intent.hasExtra(EXTRA_POI_ID)) {
            //kanei tin metavilti Edit = ture gia na 3eroume oti tha ginei edit stin egrafi
            Edit=true;

            POI_Name.setText(intent.getStringExtra(EXTRA_POI_NAME));
            Resources res = getResources();
            List<String> categorys = Arrays.asList(res.getStringArray(R.array.POIsCategories));
            for(String s :categorys){
                if(s.equals(intent.getStringExtra(EXTRA_POI_CATEGORY))){
                    POI_Category.setSelection(categorys.indexOf(s));
                }
            }
            POI_Description.setText(intent.getStringExtra(EXTRA_POI_DESCRIPTION));
            POI_Latitude.setText(String.valueOf(intent.getDoubleExtra(EXTRA_POI_LATITUDE, 0.0)));
            POI_Longitude.setText(String.valueOf(intent.getDoubleExtra(EXTRA_POI_LONGITUDE, 0.0)));

            //alazei to title tou activity
            getSupportActionBar().setTitle("Edit Point Of Interest");
        } else {
            //kanei tin Edit false gia na di3ei oti tha finei insert egrafis
            //alazei to title tou activity
            getSupportActionBar().setTitle("Add Point Of Interest");
        }
    }

    private void SavePOI() {
        String poi_name = POI_Name.getText().toString();
        String poi_category = POI_Category.getSelectedItem().toString();
        String poi_latitude = POI_Latitude.getText().toString();
        String poi_longitude = POI_Longitude.getText().toString();
        String poi_description = POI_Description.getText().toString();

        if (poi_name.isEmpty() || poi_category.isEmpty() || poi_latitude.isEmpty() || poi_longitude.isEmpty()) {
            Toast.makeText(this, "Invalid arguments", Toast.LENGTH_LONG).show();
            return;
        }

        //Apothikeuw to POI stin DataBase
        myDb = new DatabaseHelper(this);
        //an prokete na ginei edit iparxousas egrafis
        if(Edit){
            //elenxei an ala3e kapia timi sta paidia
            if (poi_name.equals(intent.getStringExtra(EXTRA_POI_NAME)) && poi_category.equals(intent.getStringExtra(EXTRA_POI_CATEGORY))
                    && poi_latitude.equals(String.valueOf(intent.getDoubleExtra(EXTRA_POI_LATITUDE, 0.0)))
                    && poi_longitude.equals(String.valueOf(intent.getDoubleExtra(EXTRA_POI_LONGITUDE, 0.0)))
                    && poi_description.equals(intent.getStringExtra(EXTRA_POI_DESCRIPTION))){

                Toast.makeText(this, "You have to update at least one field", Toast.LENGTH_LONG).show();
                return;

            }
            else {
                boolean result = myDb.UpdatePOIs(intent.getIntExtra(EXTRA_POI_ID, -1),
                        poi_name, poi_category, poi_description, Double.valueOf(poi_latitude), Double.valueOf(poi_longitude));
                if(result){
                    setResult(RESULT_OK);
                    finish();
                }

            }
        }
        //an prokite na ginei insert egrafis
        else {
            boolean result = myDb.InsertPOI(poi_name, poi_category, poi_description, Double.valueOf(poi_latitude), Double.valueOf(poi_longitude));

            if (result == true) {

                //stelnei sto POIsActiviti oti ta dedomena einai egkira kai stelnei kai to intent me to putExtra
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Database connection problem", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_poi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.SavePOI:
                SavePOI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
