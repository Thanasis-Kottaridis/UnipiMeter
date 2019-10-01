package com.unipi.kottarido.unipimeter.unipimeter;

import java.sql.Timestamp;

public class LocationHistory {

    private int Id;
    private String timestamp;
    private int POIid;

    //dilono to name kai ta col tou LocationHistory table
    public static final String TABLE_NAME = "LocationHistory";
    public static final String COL_ID = "ID";
    public static final String COL_TIMESTAMP = "TIMESTAMP";
    public static final String COL_POI_ID = "POI_ID";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COL_POI_ID + " INTEGER,"
                    + "FOREIGN KEY ('"+ COL_POI_ID +"')REFERENCES `"+POI.TABLE_NAME+"` (`"+POI.COL_ID+"`)"
                    + ")";

    public LocationHistory(int Id ,String timestamp, int POIid) {
        this.Id = Id;
        this.timestamp = timestamp;
        this.POIid = POIid;
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

    public int getPOIid() {
        return POIid;
    }

    public void setPOIid(int POIid) {
        this.POIid = POIid;
    }
}
