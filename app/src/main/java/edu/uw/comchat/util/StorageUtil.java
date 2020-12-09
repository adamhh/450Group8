package edu.uw.comchat.util;

import android.content.Context;
import android.content.SharedPreferences;

import edu.uw.comchat.R;

public class StorageUtil {
    private final Context mContext;
    private SharedPreferences mPreferences;
    
    public StorageUtil(Context context) {
        mContext = context;
    }

    public void storeLocationZip(int zip) {
        getPreferences();
        mPreferences.edit()
                .putString(getString(R.string.keys_zip), String.valueOf(zip))
                .putBoolean(getString(R.string.keys_zip), true)
                .apply();
    }
    
    public void storeLocationLatLong(double latitude, double longitude) {
        getPreferences();
        mPreferences.edit()
                .putString(getString(R.string.keys_latitude), String.valueOf(latitude))
                .putString(getString(R.string.keys_longitude), String.valueOf(longitude))
                .putBoolean(getString(R.string.keys_location_type), false)
                .apply();
    }
    
    public String[] getLocation() {
        getPreferences();
        String[] location;

        if (mPreferences.getBoolean(getString(R.string.keys_location_type), true))
            location = new String[] {mPreferences.getString(
                    getString(R.string.keys_zip), "98402") };
        else
            location = new String[] {
                    mPreferences.getString(getString(R.string.keys_latitude), "0"),
                    mPreferences.getString(getString(R.string.keys_longitude), "0") };

        return location;
    }

    public void storeCredentials(String jwt) {
        getPreferences();
        mPreferences.edit()
                .putString(getString(R.string.keys_jwt), jwt)
                .apply();
    }

    public void removeCredentials() {
        getPreferences();
        mPreferences.edit()
                .remove(getString(R.string.keys_jwt))
                .apply();
    }

    public String loadCredentials() {
        getPreferences();
        return mPreferences.getString(getString(R.string.keys_jwt), "");
    }

    public void storeTheme(int id) {
        getPreferences();
        mPreferences.edit()
                .putString(getString(R.string.keys_theme), getString(id))
                .apply();
    }

    public int loadTheme() {
        getPreferences();
        String theme = mPreferences.getString(
                getString(R.string.keys_theme),
                getString(R.string.theme_default));

        if (theme.equals(getString(R.string.theme_red)))
            return R.style.Theme_ComChatRed;
        else if (theme.equals(getString(R.string.theme_grey)))
            return R.style.Theme_ComChatBlueGrey;
        else
            return R.style.Theme_ComChat;
    }

    public void toggleDarkTheme() {
        getPreferences();
        mPreferences.edit()
                .putBoolean(getString(R.string.keys_theme_dark), !loadDarkTheme())
                .apply();
    }

    public boolean loadDarkTheme() {
        getPreferences();
        return mPreferences.getBoolean(getString(R.string.keys_theme_dark), false);
    }

    private String getString(int id) {
        return mContext.getString(id);
    }
    
    private void getPreferences() {
        mPreferences = mContext.getSharedPreferences(
                getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);
    }
}
