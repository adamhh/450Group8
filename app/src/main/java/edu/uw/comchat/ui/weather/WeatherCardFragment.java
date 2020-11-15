package edu.uw.comchat.ui.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentWeatherCurrentBinding;
import edu.uw.comchat.databinding.FragmentWeatherTenDayBinding;
import edu.uw.comchat.databinding.FragmentWeatherTenDayCardBinding;


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
        view = inflater.inflate(R.layout.fragment_weather_ten_day, container, false);
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

        } catch (JSONException e){
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
    weatherCurrentBinding.textWeatherCurrentTemp.setText("Temp: " + currentWeather.getString("temp") + "F");
//    weatherCurrentBinding.textWeatherCurrentWindChill.setText("Wind Deg: " + currentWeather.getString("wind_deg") + "F");
//    weatherCurrentBinding.textWeatherCurrentWindDirection.setText("Wind Direction: " + currentWeather.getString("direction"));
//    weatherCurrentBinding.textWeatherCurrentWindSpeed.setText("Wind Speed: " + currentWeather.getString("speed") + "MPH");
//    weatherCurrentBinding.textWeatherCurrentDescription.setText("Description: " + currentWeather.getString("text"));
  }

  private void updateWeatherDaily(JSONObject response)throws JSONException{
    // No daily data.
  }

  private void updateWeatherTenDay(JSONObject response)throws JSONException {
    JSONArray forecast = response.getJSONArray("daily");
    for (int i = 0; i < forecast.length(); i++) {
      JSONObject day = forecast.getJSONObject(i);
      updateWeatherBasedOnDay(i, day);
    }
  }

  // TODO Very UGLY code, need to somehow refactor this. May want to use recycler view instead.
  private void updateWeatherBasedOnDay(int day, JSONObject response) throws JSONException {
    FragmentWeatherTenDayBinding weatherTenDayBinding = FragmentWeatherTenDayBinding.bind(getView());

    switch(day){
      case 1:
//        JSONObject date = new JSONObject(response.getString("dt"));
        FragmentWeatherTenDayCardBinding forecastDay1 = weatherTenDayBinding.weatherTenDay1;
        updateTenDayCard(forecastDay1, response);
        break;
      case 2:
        FragmentWeatherTenDayCardBinding forecastDay2 = weatherTenDayBinding.weatherTenDay2;
        updateTenDayCard(forecastDay2, response);
        break;
      case 3:
        FragmentWeatherTenDayCardBinding forecastDay3 = weatherTenDayBinding.weatherTenDay3;
        updateTenDayCard(forecastDay3, response);
        break;
      case 4:
        FragmentWeatherTenDayCardBinding forecastDay4 = weatherTenDayBinding.weatherTenDay4;
        updateTenDayCard(forecastDay4, response);
        break;
      case 5:
        FragmentWeatherTenDayCardBinding forecastDay5 = weatherTenDayBinding.weatherTenDay5;
        updateTenDayCard(forecastDay5, response);
        break;
//      case 6:
//        weatherTenDayBinding.weatherTenDay6.textWeatherTenDayDate.setText("Day: " + response.getString("day"));
//        weatherTenDayBinding.weatherTenDay6.textWeatherTenDayTemp.setText("Low: " + response.getString("low") + "F " +
//                "High: " + response.get("high") + "F");
//        weatherTenDayBinding.weatherTenDay6.textWeatherTenDayDescription.setText("Description: "+ response.getString("text"));
//        break;
//      case 7:
//        weatherTenDayBinding.weatherTenDay7.textWeatherTenDayDate.setText("Day: " + response.getString("day"));
//        weatherTenDayBinding.weatherTenDay7.textWeatherTenDayTemp.setText("Low: " + response.getString("low") + "F " +
//                "High: " + response.get("high") + "F");
//        weatherTenDayBinding.weatherTenDay7.textWeatherTenDayDescription.setText("Description: "+ response.getString("text"));
//        break;
//      case 8:
//        weatherTenDayBinding.weatherTenDay8.textWeatherTenDayDate.setText("Day: " + response.getString("day"));
//        weatherTenDayBinding.weatherTenDay8.textWeatherTenDayTemp.setText("Low: " + response.getString("low") + "F " +
//                "High: " + response.get("high") + "F");
//        weatherTenDayBinding.weatherTenDay8.textWeatherTenDayDescription.setText("Description: "+ response.getString("text"));
//        break;
//      case 9:
//        weatherTenDayBinding.weatherTenDay9.textWeatherTenDayDate.setText("Day: " + response.getString("day"));
//        weatherTenDayBinding.weatherTenDay9.textWeatherTenDayTemp.setText("Low: " + response.getString("low") + "F " +
//                "High: " + response.get("high") + "F");
//        weatherTenDayBinding.weatherTenDay9.textWeatherTenDayDescription.setText("Description: "+ response.getString("text"));
//        break;
//      case 10:
//        weatherTenDayBinding.weatherTenDay10.textWeatherTenDayDate.setText("Day: " + response.getString("day"));
//        weatherTenDayBinding.weatherTenDay10.textWeatherTenDayTemp.setText("Low: " + response.getString("low") + "F " +
//                "High: " + response.get("high") + "F");
//        weatherTenDayBinding.weatherTenDay10.textWeatherTenDayDescription.setText("Description: "+ response.getString("text"));
//        break;
    }

  }
  private void updateTenDayCard (FragmentWeatherTenDayCardBinding whichDay, JSONObject response) throws JSONException {
    JSONObject date = response.getJSONObject("dt");
    Log.i("date", date.toString());
    whichDay.textWeatherTenDayDate.setText("Date: " +
            date.getString("months") + "\\" + date.getString("dates") + "\\" + date.getString("years"));

    JSONObject temp = response.getJSONObject("temp");
    whichDay.textWeatherTenDayTemp.setText("Temp: " + temp.getString("day"));

    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
    whichDay.textWeatherTenDayDescription.setText("Description: "+ weather.getString("description"));
  }
}