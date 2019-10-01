package com.unipi.kottarido.unipimeter.unipimeter;

public class POI {
    private int Id;
    private String Name;
    private String Category;
    private String Description;
    private double Latitude;
    private double Longitude;
    private boolean near;

    //dilwnw to name kai ta col tou POI table
    public static final String TABLE_NAME = "POIs";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";

    //dilono to create table query gia ton POI table

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // to kanw klidi kai autoincrement edo!
                    + COL_NAME + " TEXT,"
                    + COL_CATEGORY + " TEXT,"
                    + COL_DESCRIPTION +" TEXT,"
                    + COL_LATITUDE + " DOUBLE,"
                    +COL_LONGITUDE+ " DOUBLE "
                    + ")";

    public POI(int id,String name, String category, String description, double latitude, double longitude) {

        Id = id;
        Name = name;
        Category = category;
        Description = description;
        Latitude = latitude;
        Longitude = longitude;
        near = false;
    }

    public POI(int id, String name, double latitude, double longitude) {
        Id = id;
        Name = name;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public boolean isNear() {
        return near;
    }

    public void setNear(boolean near) {
        this.near = near;
    }


}
