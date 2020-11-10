package edu.uw.comchat.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.uw.comchat.R;

/**
 * A fragment that represents a single card for a weather report.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherCardFragment extends Fragment {

  public static final String ARG_POSITION = "position";

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
    Bundle args = getArguments();
    /*
    ((TextView) view.findViewById(R.id.text_weather_default))
            .setText(Integer.toString(args.getInt(ARG_OBJECT)));

     */

    switch (args.getInt(ARG_POSITION)) {
      case 1:
        updateWeatherCurrent();
        break;

      case 2:
        updateWeatherDaily();
        break;

      case 3:
        updateWeatherTenDay();
        break;
    }
  }

  private void updateWeatherCurrent() {
  }

  private void updateWeatherDaily() {
  }

  private void updateWeatherTenDay() {
  }

  // Checkstyle: Done - Hung Vu
}