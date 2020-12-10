package edu.uw.comchat.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentHomeBinding;
import edu.uw.comchat.model.NotificationViewModel;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.ui.connection.ConnectionListViewModel;
import edu.uw.comchat.ui.weather.WeatherViewModel;
import edu.uw.comchat.util.StorageUtil;

import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





/**
 * A fragment for the users home view.
 *
 * @author Jerry Springer, Hung Vu
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class HomeFragment extends Fragment {
  private UserInfoViewModel mUserModel;
  private WeatherViewModel mWeatherModel;
  private FragmentHomeBinding mBinding;

  private NotificationViewModel mNotificationModel;
  // Store incoming connection request.
  private ConnectionListViewModel mConnectionModel;
  // TODO adjust home fragment, it's just in a bare bone state now.

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    mUserModel = provider.get(UserInfoViewModel.class);
    mWeatherModel = provider.get(WeatherViewModel.class);
    mWeatherModel.getToken().setValue(mUserModel.getJwt());
    mNotificationModel = provider.get(NotificationViewModel.class);
    mConnectionModel = provider.get(ConnectionListViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    mBinding = FragmentHomeBinding.inflate(inflater);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.textHomeUserEmail.setText(mUserModel.getEmail());
    mWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    StorageUtil util = new StorageUtil(getContext());
    mWeatherModel.connect(util.getLocation());

    mNotificationModel.addChatResponseObserver(getViewLifecycleOwner(), notificationMap -> {
      try {
        mBinding.textHomeNotificationMessage.setText("Message: " + mNotificationModel.getLatestNotificationMessage());
        mBinding.textHomeNotificationSender.setText("From: " + mNotificationModel.getLatestNotificationSender());
        mBinding.textHomeNotificationDate.setText("Date: " + mNotificationModel.getLatestNotificationDate());
        mBinding.textHomeNotificationTime.setText("Time: " + mNotificationModel.getLatestNotificationTime());
      } catch (NullPointerException e){
        Log.i("Home Fragment", "NPE for at notification due to no message has been received, this error can be ignored.");
      }
    });

    TimerTask getGroupTask = new TimerTask() {
      @Override
      public void run() {
        mConnectionModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());
      }
    };
    Timer timer = new Timer("Update incoming request notification per 0.5 sec");
    timer.scheduleAtFixedRate(getGroupTask, 500L, 500L);
    mConnectionModel.addIncomingListObserver(getViewLifecycleOwner(), listConnection -> {
      mNotificationModel.updateConnectionNotificationData(listConnection);
    });

    mNotificationModel.addConnectionResponseObserver(getViewLifecycleOwner(), incomingRequest -> {
      try {
        // TODO Update incoming request notification on homepage.
        //  The view model is refreshed per 0.5s so the error will be repeatedly printed till there is a connection.
        //  If it is removed then the error return again.
        //  What I think you can do is deleting the message and repeatedly set text of object to empty for example ("").
//        mBinding.textView.setText(mNotificationModel.getLatestConnectionRequest().get(0));
      } catch (ArrayIndexOutOfBoundsException e){
        Log.i("Out of bound Home, Notification model", "The error happens when there is no incoming request, this can be ignored.");
      }
    });



  }

  /**
   * Behavior when receive a response.
   *
   * @param response web service response
   */
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
          currentWeatherInfo(response);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }

  /**
   * This helper method will extract weather information from the response.
   *
   * @param response web service response
   * @throws JSONException an exception handled by observeResponse
   */
  private void currentWeatherInfo(JSONObject response) throws JSONException {
    JSONObject currentWeather = response.getJSONObject("current");
    JSONArray weatherDescription = currentWeather.getJSONArray("weather");
    int weatherDescriptionId = weatherDescription.getJSONObject(0).getInt("id");
    long sunriseTime = currentWeather.getLong("sunrise");
    long sunsetTime = currentWeather.getLong("sunset");

    // Get sunrise and sunset time.
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    final String formattedSunriseTime = Instant.ofEpochSecond(sunriseTime)
            .atZone(TimeZone.getDefault().toZoneId())
            .format(formatter);
    final String formattedSunsetTime = Instant.ofEpochSecond(sunsetTime)
            .atZone(TimeZone.getDefault().toZoneId())
            .format(formatter);

    String currentTemp = currentWeather.getString("temp") + " F";

    setWeatherInfo(weatherDescriptionId, formattedSunriseTime,
            formattedSunsetTime, currentTemp);

  }

  /**
   * This helper method helps modify UI to display weather information.
   *
   * @param id          weather description id
   * @param sunriseTime sunrise time
   * @param sunsetTime  sunset time
   * @param currentTemp current temperature
   */
  private void setWeatherInfo(int id, String sunriseTime, String sunsetTime, String currentTemp) {
    if (id >= 200 && id <= 232) { // ID for thunderstorm
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_thunder_storm);
    } else if (id >= 300 && id <= 321) { // ID for drizzle
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_drizzle);
    } else if (id >= 500 && id <= 531) { // ID for rain
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_rain);
    } else if (id >= 600 && id <= 622) { // ID for snow
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_snow);
    } else if (id >= 700 && id <= 781) { // ID for atmosphere
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_fog);
    } else if (id == 800) { // ID for clear, icon varies based on time in a day
      LocalTime currentTime = LocalTime.now();
      if (currentTime.isAfter(LocalTime.parse(sunriseTime)) && currentTime.isBefore(LocalTime.parse(sunsetTime))) {
        mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_clear_sky_day);
      } else {
        mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_clear_sky_night);
      }
    } else if (id >= 801 && id <= 804) { // ID for cloud
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_cloudy);
    }
    mBinding.textHomeWeatherTemp.setText(currentTemp);

  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_home, menu);
  }


}