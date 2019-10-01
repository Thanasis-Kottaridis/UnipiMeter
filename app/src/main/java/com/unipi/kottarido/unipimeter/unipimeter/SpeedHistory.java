package com.unipi.kottarido.unipimeter.unipimeter;

import java.sql.Timestamp;

public class SpeedHistory {
    private int Id;
    private String timestamp;
    private float Speed;
    private double Latitude;
    private double Longitude;

    //dilono to name kai ta col tou LocationHistory table
    public static final String TABLE_NAME = "SpeedHistory";
    public static final String COL_ID = "ID";
    public static final String COL_TIMESTAMP = "TIMESTAMP";
    public static final String COL_SPEED ="SPEED";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COL_SPEED+" FLOAT,"
                    + COL_LATITUDE + " DOUBLE,"
                    + COL_LONGITUDE+ " DOUBLE"
                    + ")";


    public SpeedHistory(int id, String timestamp, float speed, double latitude, double longitude) {
        Id = id;
        this.timestamp = timestamp;
        Speed = speed;

        Latitude = latitude;
        Longitude = longitude;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }


    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
