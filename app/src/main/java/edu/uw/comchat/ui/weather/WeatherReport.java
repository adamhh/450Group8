package edu.uw.comchat.ui.weather;

public class WeatherReport {
    private final String mTime;
    private final String mTemp;
    private final String mSunrise;
    private final String mSunset;
    private final int mId;

    public WeatherReport(String time, String temp, String sunrise, String sunset, int id) {
        mTime = time;
        mTemp = temp;
        mSunrise = sunrise;
        mSunset = sunset;
        mId = id;
    }

    public WeatherReport(String time, String temp, int id) {
        this(time, temp, "", "", id);
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

    public int getId() {
        return mId;
    }
}