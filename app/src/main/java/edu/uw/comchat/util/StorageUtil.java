package edu.uw.comchat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageUtil {
    private final String ZIP_TYPE = "ZIP_TYPE";
    private final String ZIP = "ZIP";
    private final String LATITUDE = "LATITUDE";
    private final String LONGITUDE = "LONGITUDE";
    private final String STORAGE = "edu.uw.comchat.STORAGE";
    private final Context mContext;
    private SharedPreferences mPreferences;
    
    public StorageUtil(Context context) {
        mContext = context;
    }

    public void storeLocationZip(int zip) {
        getPreferences();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ZIP, String.valueOf(zip));
        editor.putBoolean(ZIP_TYPE, true);
        editor.apply();
    }
    
    public void storeLocationLatLong(double latitude, double longitude) {
        getPreferences();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LATITUDE, String.valueOf(latitude));
        editor.putString(LONGITUDE, String.valueOf(longitude));
        editor.putBoolean(ZIP_TYPE, false);
        editor.apply();
    }
    
    public String[] getLocation() {
        getPreferences();
        String[] location;

        if (mPreferences.getBoolean(ZIP_TYPE, true))
            location = new String[] {mPreferences.getString(ZIP, "98402") };
        else
            location = new String[] {
                    mPreferences.getString(LATITUDE, "0"),
                    mPreferences.getString(LONGITUDE, "0") };

        return location;
    }
    
    private void getPreferences() {
        mPreferences = mContext.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }
}
