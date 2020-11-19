package edu.uw.comchat.ui.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentWeatherCurrentBinding;
import edu.uw.comchat.databinding.FragmentWeatherDailyBinding;
import edu.uw.comchat.databinding.FragmentWeatherDailyCardBinding;
import edu.uw.comchat.databinding.FragmentWeatherFiveDayBinding;
import edu.uw.comchat.databinding.FragmentWeatherFiveDayCardBinding;


/**
 * A fragment that represents a single card for a weather report.
 *
 * @author Jerry Springer (UI), Hung Vu (connect to webservice and populate data).
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherCardFragment extends Fragment {

  public static final String ARG_POSITION = "position";

  // Connect to webservice.
  private WeatherViewModel mWeatherModel;
  private Bundle mArgs;

  public WeatherCardFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view;
    switch (getArguments().getInt(ARG_POSITION)) {
      // Not using layout ids as case as it is not recommended
      // Layout ids are not final on runtime could cause error?
      case 1:
        view = inflater.inflate(R.layout.fragment_weather_current, container, false);
        break;

      case 2:
        view = inflater.inflate(R.layout.fragment_weather_daily, container, false);
        break;

      case 3:
        view = inflater.inflate(R.layout.fragment_weather_five_day, container, false);
        break;

      default:
        // TODO Throw error
        view = null;
        break;
    }

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    mArgs = getArguments();
    /*
    ((TextView) view.findViewById(R.id.text_weather_default))
            .setText(Integer.toString(args.getInt(ARG_OBJECT)));

     */
    mWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    mWeatherModel.connect("98402");
  }

  private void observeResponse(JSONObject response) {
    if (response.length() > 0) {
      if (response.has("code")) {
        try {
          Log.i("JSON error", response.getJSONObject("data").getString("message"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
        try {
          switch (mArgs.getInt(ARG_POSITION)) {
            case 1:
              updateWeatherCurrent(response);
              break;

            case 2:
              updateWeatherDaily(response);
              break;

            case 3:
              updateWeatherTenDay(response);
              break;
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }


  private void updateWeatherCurrent(JSONObject response) throws JSONException {
    // TODO UGLY code, may need to somehow refactor this.
    FragmentWeatherCurrentBinding weatherCurrentBinding = FragmentWeatherCurrentBinding.bind(getView());
    JSONObject currentWeather = response.getJSONObject("current");
    weatherCurrentBinding.textWeatherCurrentTemp.setText("Temp: " + currentWeather.getString("temp"));
    weatherCurrentBinding.textWeatherCurrentFeelsLike.setText("Feels Like: " + currentWeather.getString("feels_like"));
    weatherCurrentBinding.textWeatherCurrentUvi.setText("UV Index: " + currentWeather.getString("uvi"));
    weatherCurrentBinding.textWeatherCurrentWindSpeed.setText("Wind Speed: " + currentWeather.getString("wind_speed"));
    weatherCurrentBinding.textWeatherCurrentDescription.setText("Description: " + currentWeather.getJSONArray("weather")
            .getJSONObject(0)
            .getString("description"));
  }

  // TODO This method vs. updateWeatherTenDay are similar, can refactor.
  // Traverse json array.
  private void updateWeatherDaily(JSONObject response) throws JSONException {
    JSONArray hourly = response.getJSONArray("hourly");
    for (int i = 0; i < hourly.length() / 6; i++) {
      JSONObject hourReport = hourly.getJSONObject(i * 4);
      updateWeatherBasedOnHour(i + 1, hourReport);
    }
  }

  // Update ui information.
  private void updateWeatherBasedOnHour(int hour, JSONObject response) throws JSONException {
    FragmentWeatherDailyBinding weatherDailyCardBinding = FragmentWeatherDailyBinding.bind(getView());
    switch (hour) {
      case 1:
        FragmentWeatherDailyCardBinding hour1 = weatherDailyCardBinding.weatherCurrentHour1;
        updateHourCard(hour1, response);
        break;
      case 2:
        FragmentWeatherDailyCardBinding hour2 = weatherDailyCardBinding.weatherCurrentHour2;
        updateHourCard(hour2, response);
        break;
      case 3:
        FragmentWeatherDailyCardBinding hour3 = weatherDailyCardBinding.weatherCurrentHour3;
        updateHourCard(hour3, response);
        break;
      case 4:
        FragmentWeatherDailyCardBinding hour4 = weatherDailyCardBinding.weatherCurrentHour4;
        updateHourCard(hour4, response);
        break;
      case 5:
        FragmentWeatherDailyCardBinding hour5 = weatherDailyCardBinding.weatherCurrentHour5;
        updateHourCard(hour5, response);
        break;
      case 6:
        FragmentWeatherDailyCardBinding hour6 = weatherDailyCardBinding.weatherCurrentHour6;
        updateHourCard(hour6, response);
        break;
    }
  }

  // Helper method to update ui.
  private void updateHourCard(FragmentWeatherDailyCardBinding whichHour, JSONObject response) throws JSONException {
    JSONObject date = response.getJSONObject("dt");
    whichHour.textWeatherDailyTime.setText("Time: " + date.getString("hours") + ":" + date.getString("minutes") +
            date.getString("ampms"));
    whichHour.textWeatherDailyTemp.setText("Temp: " + response.getString("temp"));
    whichHour.textWeatherDailyHumidity.setText("Humidity: " + response.getString("humidity"));

  }

  // Traverse JSON array.
  private void updateWeatherTenDay(JSONObject response) throws JSONException {
    JSONArray forecast = response.getJSONArray("daily");
    for (int i = 0; i < forecast.length(); i++) {
      JSONObject day = forecast.getJSONObject(i);
      updateWeatherBasedOnDay(i, day);
    }
  }

  // TODO UGLY code, need to somehow refactor this. May want to use recycler view instead.
  // Update ui information.
  // TODO Current JSON response only allows the choice of 5 days forecast.
  private void updateWeatherBasedOnDay(int day, JSONObject response) throws JSONException {
    FragmentWeatherFiveDayBinding weatherTenDayBinding = FragmentWeatherFiveDayBinding.bind(getView());
    switch (day) {
      case 1:
//        JSONObject date = new JSONObject(response.getString("dt"));
        FragmentWeatherFiveDayCardBinding forecastDay1 = weatherTenDayBinding.weatherFiveDay1;
        updateTenDayCard(forecastDay1, response);
        break;
      case 2:
        FragmentWeatherFiveDayCardBinding forecastDay2 = weatherTenDayBinding.weatherFiveDay2;
        updateTenDayCard(forecastDay2, response);
        break;
      case 3:
        FragmentWeatherFiveDayCardBinding forecastDay3 = weatherTenDayBinding.weatherFiveDay3;
        updateTenDayCard(forecastDay3, response);
        break;
      case 4:
        FragmentWeatherFiveDayCardBinding forecastDay4 = weatherTenDayBinding.weatherFiveDay4;
        updateTenDayCard(forecastDay4, response);
        break;
      case 5:
        FragmentWeatherFiveDayCardBinding forecastDay5 = weatherTenDayBinding.weatherFiveDay5;
        updateTenDayCard(forecastDay5, response);
        break;
    }

  }

  // Helper method to update ui.
  private void updateTenDayCard(FragmentWeatherFiveDayCardBinding whichDay, JSONObject response) throws JSONException {
    JSONObject date = response.getJSONObject("dt");

    whichDay.textWeatherFiveDayDate.setText("Date: " +
            date.getString("months") + "\\" + date.getString("dates") + "\\" + date.getString("years"));

    JSONObject temp = response.getJSONObject("temp");
    whichDay.textWeatherFiveDayTemp.setText("Temp: " + temp.getString("day"));

    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
    whichDay.textWeatherFiveDayDescription.setText("Description: " + weather.getString("description"));
  }
}