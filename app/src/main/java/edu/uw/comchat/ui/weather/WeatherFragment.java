package edu.uw.comchat.ui.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentWeatherBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.util.StorageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fragment that shows the weather in a tabular layout for multiple
 * types of weather reports.
 *
 * @author Jerry Springer (UI), Hung Vu (connect to webservice and populate data).
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherFragment extends Fragment {

  private static final String ZIPCODE_REGEX = "^[0-9]{5}(?:-[0-9]{4})?$";

  // Connect to webservice - Hung Vu.
  private WeatherViewModel mWeatherModel;
  private FragmentWeatherBinding mWeatherBinding;
  private StorageUtil mStorageUtil;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

    // Set storage util
    mStorageUtil = new StorageUtil(getContext());

    // Set jwt dynamically.
    UserInfoViewModel userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    String jwt = userInfoViewModel.getJwt();
    mWeatherModel.getToken().setValue(jwt);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    mWeatherBinding = FragmentWeatherBinding.inflate(inflater);
    return mWeatherBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    // Establish weather connection
    mWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    mWeatherModel.connect(mStorageUtil.getLocation());

    // Setup recycler view layout managers
    mWeatherBinding.listWeatherHour.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mWeatherBinding.listWeatherDaily.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_weather, menu);

    ((SearchView) menu.findItem(R.id.weather_search)
            .getActionView())
            .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String s) {
                Pattern pattern = Pattern.compile(ZIPCODE_REGEX);
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) {
                  mStorageUtil.storeLocationZip(s);
                  mWeatherModel.connect(new String[] {s});
                } else {
                  new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                          .setMessage("Using last known or default location.")
                          .setTitle("Location not found")
                          .setPositiveButton(R.string.connection_search_ok, (dialog, which) -> {
                          })
                          .show();
                }

                return false;
              }

              @Override
              public boolean onQueryTextChange(String s) {
                return false;
              }
            });

  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_location) {
      Navigation.findNavController(getView()).navigate(
              WeatherFragmentDirections.actionNavigationWeatherToLocationFragment()
      );
    }

    return super.onOptionsItemSelected(item);
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
          populateWeatherPage(response);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }

  private void populateWeatherPage(JSONObject response) throws JSONException {
    // Set location
    String location = response.getString("City")
            + ", " + response.getString("State")
            + ", " + response.getString("Country");
    mWeatherBinding.textWeatherLocation.setText(location);

    // Get and set current weather
    JSONObject current = response.getJSONObject("current");
    String currentTemp = current.getString("temp") + "\u00B0";
    mWeatherBinding.textWeatherCurrentTemp.setText(currentTemp);
    mWeatherBinding.textWeatherCurrentDescription.setText(
            current.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description"));

    // Get sunrise and sunset time.
    long sunriseTime = current.getLong("sunrise");
    long sunsetTime = current.getLong("sunset");
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    final String formattedSunriseTime = Instant.ofEpochSecond(sunriseTime)
            .atZone(TimeZone.getDefault().toZoneId())
            .format(formatter);
    final String formattedSunsetTime = Instant.ofEpochSecond(sunsetTime)
            .atZone(TimeZone.getDefault().toZoneId())
            .format(formatter);

    // Setup hourly weather reports
    ArrayList<WeatherReport> hourlyReports = new ArrayList<>();
    JSONArray hourlyArray = response.getJSONArray("hourly");
    try {
      for (int i = 0; i < hourlyArray.length(); i++) {
        JSONObject json = hourlyArray.getJSONObject(i);
        JSONObject time = json.getJSONObject("dt");
        hourlyReports.add(new WeatherReport(
                time.getString("hours") + " " + time.getString("ampms"),
                json.getString("temp") + "\u00B0",
                formattedSunriseTime,
                formattedSunsetTime,
                json.getJSONArray("weather")
                        .getJSONObject(0)
                        .getInt("id")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Setup daily weather reports
    ArrayList<WeatherReport> dailyReports = new ArrayList<>();
    JSONArray dailyArray = response.getJSONArray("daily");
    try {
      for (int i = 0; i < dailyArray.length(); i++) {
        JSONObject json = dailyArray.getJSONObject(i);
        JSONObject time = json.getJSONObject("dt");
        dailyReports.add(new WeatherReport(
                time.getString("months") + "/" + time.getString("dates"),
                json.getJSONObject("temp").getString("day") + "\u00B0",
                json.getJSONArray("weather")
                        .getJSONObject(0)
                        .getInt("id")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Set up recycler views
    mWeatherBinding.listWeatherHour.setAdapter(new WeatherHourRecyclerViewAdapter(hourlyReports));
    mWeatherBinding.listWeatherDaily.setAdapter(new WeatherDayRecyclerViewAdapter(dailyReports));
  }
}