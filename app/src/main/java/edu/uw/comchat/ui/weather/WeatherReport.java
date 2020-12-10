package edu.uw.comchat.ui.weather;

public class WeatherReport {
    private final String mTime;
    private final String mTemp;
    private final String mSunrise;
    private final String mSunset;
    private final String mDescription;
    private final int mId;

    public WeatherReport(String time, String temp, String sunrise, String sunset, String description, int id) {
        mTime = time;
        mTemp = temp;
        mSunrise = sunrise;
        mSunset = sunset;
        mDescription = description;
        mId = id;
    }

    public WeatherReport(String time, String temp, String sunrise, String sunset, int id) {
        this(time, temp, sunrise, sunset, "", id);
    }

    public WeatherReport(String time, String temp, String description, int id) {
        this(time, temp, "", "", description, id);
    }

    public WeatherReport(String time, String temp, int id) {
        this(time, temp, "", "", "", id);
    }

    public String getTime() {
        return mTime;
    }

    public String getTemp() {
        return mTemp;
    }

    public String getSunrise() {
        return mSunrise;
    }

    public String getSunset() {
        return mSunset;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getId() {
        return mId;
    }
}