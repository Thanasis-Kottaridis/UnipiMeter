package com.unipi.kottarido.unipimeter.unipimeter;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.nitri.gauge.Gauge;

import static com.unipi.kottarido.unipimeter.unipimeter.SettingsActivity.KEY_PREFERENCES_SPEED_LIMIT;

public class MainActivity extends AppCompatActivity implements LocationListener, DialogClass.DialogListener {

    //REQUEST CODES
    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int GO_TO_POIs_REQUEST_CODE_= 2 ;
    public static final int SETTINGS_REQUEST_CODE = 3;

    //data helper
    private DatabaseHelper myDb;

    private LocationManager locationManager;
    private Gauge gauge;
    private TextView SpeedLimitTextView;
    private List<POI> myPOIs;
    private int SpeedLimit = 50;
    private int DistanceRange = 100;
    private boolean VoiceAlert;
    private boolean SpeedLimitFlag = true;

    //Text To Speech Variables;
    private TextToSpeech tts;
    private TextToSpeech.OnInitListener initListener =
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status==TextToSpeech.SUCCESS)
                        tts.setLanguage(Locale.ENGLISH);
                }
            };


    @Override
    public void onLocationChanged(Location location) {
        float Speed = (location.getSpeed()*3600)/1000;
        gauge.setValue(Speed);

        //elenxei an o xristis exei paraviasi to speed limit
        if (SpeedLimit < Speed && SpeedLimitFlag){
           boolean result = myDb.InsertSpeedHistory(Speed, location.getLatitude(), location.getLongitude());
           if(result) {
               ShowMessage("You running faster than the limit!");
               //an to voice alert einai eneropiimeno apo tis rithmisis
               //i euarmogi idopoiei me fonitiko minima
               if(VoiceAlert)
                   tts.speak("You running faster than the limit!",TextToSpeech.QUEUE_ADD,null,null);
           }
           else
               Toast.makeText(this, "Database connection problem", Toast.LENGTH_LONG).show();
            SpeedLimitFlag = false;
        }
        else if (SpeedLimit > ((location.getSpeed()*3600)/1000) && !SpeedLimitFlag){
            SpeedLimitFlag = true;
        }

        //elenxos an o xristis vriskete mesa se mia orismeni aktina apo kapio agapimeno tou POI
        for(POI poi :myPOIs){
            Location loc = new Location("destination");
            loc.setLatitude(poi.getLatitude());
            loc.setLongitude(poi.getLongitude());
            if(location.distanceTo(loc) < DistanceRange && !poi.isNear()){
                //eisai konta se POI
                boolean result = myDb.InsertLocationHistory(poi.getId());
                if (result) {
                    ShowMessage("You are near to The POI: " + poi.getName());
                    poi.setNear(true);

                    if(VoiceAlert)
                        tts.speak("You are near to The Point of interest: "+ poi.getName() ,TextToSpeech.QUEUE_ADD,null,null);
                }
                else
                    Toast.makeText(this, "Database connection problem", Toast.LENGTH_LONG).show();

            }
            else if(location.distanceTo(loc) > DistanceRange)
                poi.setNear(false);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        //arxikopoiei to text to speech instance
        tts = new TextToSpeech(this,initListener);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        gauge = findViewById(R.id.gauge);
        SpeedLimitTextView = findViewById(R.id.speedLimitTextView_Main);
        SpeedLimitTextView.setText("SPEED LIMIT : "+SpeedLimit+" KM/H");

        readPOIs();

        readSettings();

        //elenxos gia permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            EnableGPS();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        //on click event gia to SpeedLimit TextView
        SpeedLimitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });
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

    private void readSettings(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SpeedLimit = Integer.parseInt(preferences.getString(SettingsActivity.KEY_PREFERENCES_SPEED_LIMIT,"50"));
        DistanceRange = Integer.parseInt(preferences.getString(SettingsActivity.KEY_PREFERENCES_DISTANCE_RANGE,"100"));
        VoiceAlert = preferences.getBoolean(SettingsActivity.KEY_PREFERENCES_VOICE_ALERT,true);

        SpeedLimitTextView.setText("SPEED LIMIT : "+SpeedLimit+" KM/H");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //ti tha ginei sto onclick kathe katigorias tou menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Settings:
                startActivityForResult(new Intent(this,SettingsActivity.class),SETTINGS_REQUEST_CODE);
                return true;
            case R.id.POIs:
                startActivityForResult(new Intent(this,POIsActivity.class), GO_TO_POIs_REQUEST_CODE_);
                return true;
            case R.id.SpeedHistory:
                startActivity(new Intent(this,SpeedHistoryActivity.class));
                return true;
            case R.id.LocationHistory:
                startActivity(new Intent(this,LocationHistoryActivity.class));
                //Toast.makeText(this, "Location History", Toast.LENGTH_LONG).show();
                return true;
            case R.id.Statistics:
                startActivity(new Intent(this,StatisticsActivity.class));
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GO_TO_POIs_REQUEST_CODE_&& resultCode == RESULT_OK){
           //enimeronei tin lista apo tin vasi
            readPOIs();
        }
        else if(requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK){
            readSettings();
        }
    }

    private void EnableGPS() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch (SecurityException e){
            Toast.makeText(this,  getString(R.string.noAccessFineLocationPermission),  Toast.LENGTH_LONG).show();
        }
    }

    //TO String[] permission einai ena array pou periexei ola ta permission pou exoume zitisei(sti periptosi mas 1)
    // to int[] grantResults periexei tis apantisis tou xristi sxetika me tin adia ton permition pou zitisame
    // kai einai panta oses kai ta permissions pou zitisame
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                EnableGPS();
            }
            else {
                Toast.makeText(this, getString(R.string.noAccessFineLocationPermission), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void ShowMessage(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Alert!");
        builder.setMessage(s);
        builder.show();
    }

    public void OpenDialog(){
        DialogClass dialog = new DialogClass();
        dialog.show(getSupportFragmentManager(), "Set Speed Limit Dialog");
    }

    @Override
    public void applyText(String[] Answers) {
        SpeedLimit = Integer.parseInt(Answers[0]);
        SpeedLimitTextView.setText("SPEED LIMIT : "+SpeedLimit+" KM/H");
    }

}
