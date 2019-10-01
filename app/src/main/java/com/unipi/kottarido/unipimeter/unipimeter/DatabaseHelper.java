package com.unipi.kottarido.unipimeter.unipimeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.LocationListener;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //i version tis database
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "UnipiMeter.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
//        SQLiteDatabase db = this .getWritableDatabase();
//        onUpgrade(db, 1, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(POI.CREATE_TABLE);
       db.execSQL(LocationHistory.CREATE_TABLE);
       db.execSQL(SpeedHistory.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+POI.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+LocationHistory.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+SpeedHistory.TABLE_NAME);
        onCreate(db);
    }

    //eisagogi stixiou ston pinaka POI tis database
    public boolean InsertPOI(String POI_name, String POI_Category, String POI_Description, Double POI_Latitude, Double POI_Longitude){
        SQLiteDatabase db = this .getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POI.COL_NAME, POI_name);
        contentValues.put(POI.COL_CATEGORY, POI_Category);
        contentValues.put(POI.COL_DESCRIPTION, POI_Description);
        contentValues.put(POI.COL_LATITUDE, POI_Latitude);
        contentValues.put(POI.COL_LONGITUDE, POI_Longitude);

        Long result = db.insert(POI.TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //eisagogi stixiou ston pinaka speedHistory tis db
    public boolean InsertSpeedHistory( float speed, double latitude , double longitude){
        SQLiteDatabase db = this .getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SpeedHistory.COL_SPEED, speed);
        contentValues.put(SpeedHistory.COL_LATITUDE,latitude );
        contentValues.put(SpeedHistory.COL_LONGITUDE, longitude);

        long result = db.insert(SpeedHistory.TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //eisagogi stixiou ston pinaka location history tis db
    public boolean InsertLocationHistory(int poi_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(LocationHistory.COL_TIMESTAMP, timestamp);
        contentValues.put(LocationHistory.COL_POI_ID, poi_id);

        long result = db.insert(LocationHistory.TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllFromPOI(){
        SQLiteDatabase  db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT  * FROM  "+POI.TABLE_NAME, null );

        return result;
    }


    public Cursor getAllFromSpeedHistory(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+ SpeedHistory.TABLE_NAME, null);
        return result;
    }

    public Cursor getAllFromLocationHistoryJoinPOIs(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select LocationHistory.ID,LocationHistory.TIMESTAMP," +
                "LocationHistory.POI_ID,POIs.NAME,POIs.LATITUDE,POIs.LONGITUDE " +
                " from LocationHistory join POIs " +
                " where LocationHistory.POI_ID = POIs.ID " +
                " order by TIMESTAMP DESC", null);
        return result;
    }

    public Integer deleteFromPOIs(int id){
        SQLiteDatabase db = getWritableDatabase();
        if(id != -1 )
            return db.delete(POI.TABLE_NAME, POI.COL_ID+" = ?",new String[]{String.valueOf(id)} );
        else {
            db.execSQL("Delete from "+POI.TABLE_NAME);
            return -1;
        }
    }

    public Integer deleteFromSpeedHistory(int id){
        SQLiteDatabase db = getWritableDatabase();
        if(id != -1 )
            return db.delete(SpeedHistory.TABLE_NAME, LocationHistory.COL_ID+" = ?",new String[]{String.valueOf(id)} );
        else {
            db.execSQL("Delete from "+SpeedHistory.TABLE_NAME);
            return -1;
        }
    }

    public Integer deleteFromLocationHistory(int id){
        SQLiteDatabase db = getWritableDatabase();
        if (id != -1)
            return db.delete(LocationHistory.TABLE_NAME, LocationHistory.COL_ID+" = ?",new  String[]{String.valueOf(id)} );
        else {
            //diagrafei oles tis grames tou pinaka kai epistreuei to plithos tous
            db.execSQL("Delete from " + LocationHistory.TABLE_NAME);
            return -1;
        }
    }

    public boolean UpdatePOIs( int id , String POI_name, String POI_Category, String POI_Description, Double POI_Latitude, Double POI_Longitude){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POI.COL_NAME, POI_name);
        contentValues.put(POI.COL_CATEGORY, POI_Category);
        contentValues.put(POI.COL_DESCRIPTION, POI_Description);
        contentValues.put(POI.COL_LATITUDE, POI_Latitude);
        contentValues.put(POI.COL_LONGITUDE, POI_Longitude);
        db.update(POI.TABLE_NAME, contentValues, POI.COL_ID +" = ?",new String[]{String.valueOf(id)} );
        return true;
    }

    public Cursor getSpeedLimitViolations(int interval){
        SQLiteDatabase db = getWritableDatabase();
        if(interval !=-1) {

            Cursor result = db.rawQuery("Select count(" + SpeedHistory.COL_ID + ") from " + SpeedHistory.TABLE_NAME +
                    " where " + SpeedHistory.COL_TIMESTAMP + " >= date('now','-" + interval + " days')", null);
            return result;
        }
        else {
            Cursor result = db.rawQuery("Select count(" + SpeedHistory.COL_ID + ") from " + SpeedHistory.TABLE_NAME , null);
            return result;
        }
    }

    public Cursor getTotalPOIsViews(int interval){
        SQLiteDatabase db = getWritableDatabase();
        if(interval != -1) {
            Cursor result = db.rawQuery("Select count(" + LocationHistory.COL_ID + ") from " + LocationHistory.TABLE_NAME +
                    " where " + SpeedHistory.COL_TIMESTAMP + " >= date('now','-" + interval + " days')", null);
            return result;
        }
        else {
            Cursor result = db.rawQuery("Select count(" + LocationHistory.COL_ID + ") from " + LocationHistory.TABLE_NAME , null);
            return result;
        }
    }

    public Cursor getFavoritePOICategory(int interval){
        SQLiteDatabase db = getWritableDatabase();
        if(interval != -1) {
            Cursor result = db.rawQuery("Select " + POI.COL_CATEGORY + " ,count(" + POI.COL_CATEGORY + ") from " + LocationHistory.TABLE_NAME + " join " + POI.TABLE_NAME +
                    " where " + LocationHistory.TABLE_NAME + ".POI_ID = " + POI.TABLE_NAME + ".ID and TIMESTAMP  >= date('now','-" + interval + " days')" +
                    " group by " + POI.COL_CATEGORY + " order by count(" + POI.COL_CATEGORY + ") desc", null);
            return result;
        }
        else {
            Cursor result = db.rawQuery("Select " + POI.COL_CATEGORY + " ,count(" + POI.COL_CATEGORY + ") from " + LocationHistory.TABLE_NAME + " join " + POI.TABLE_NAME +
                    " where " + LocationHistory.TABLE_NAME + ".POI_ID = " + POI.TABLE_NAME + ".ID " +
                    " group by " + POI.COL_CATEGORY + " order by count(" + POI.COL_CATEGORY + ") desc", null);
            return result;
        }
    }

    public Cursor getFavoritePOI(int interval){
        SQLiteDatabase db = getWritableDatabase();
        if(interval != -1) {
            Cursor result = db.rawQuery("Select " + POI.COL_NAME + "," + POI.COL_LATITUDE + ", " + POI.COL_LONGITUDE + ", count(" + LocationHistory.COL_POI_ID + ")" +
                    " from " + LocationHistory.TABLE_NAME + " join " + POI.TABLE_NAME +
                    " where " + LocationHistory.TABLE_NAME + ".POI_ID = " + POI.TABLE_NAME + ".ID and TIMESTAMP  >= date('now','-" + interval + " days')" +
                    " group by POI_ID" +
                    " order by count(POI_ID) desc", null);
            return result;
        }
        else {
            Cursor result = db.rawQuery("Select " + POI.COL_NAME + "," + POI.COL_LATITUDE + ", " + POI.COL_LONGITUDE + ", count(" + LocationHistory.COL_POI_ID + ")" +
                    " from " + LocationHistory.TABLE_NAME + " join " + POI.TABLE_NAME +
                    " where " + LocationHistory.TABLE_NAME + ".POI_ID = " + POI.TABLE_NAME + ".ID " +
                    " group by POI_ID" +
                    " order by count(POI_ID) desc", null);
            return result;
        }
    }

}
