package edu.uw.comchat.ui.weather;

public class WeatherReport {
    private String mTime;
    private String mTemp;

    public WeatherReport(String time, String temp) {
        mTime = time;
        mTemp = temp;
    }

    public String getTime() {
        return mTime;
    }

    public String getTemp() {
        return mTemp;
    }
}