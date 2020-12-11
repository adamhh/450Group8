package edu.uw.comchat.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentHomeBinding;
import edu.uw.comchat.databinding.FragmentHomeWeatherCardBinding;
import edu.uw.comchat.model.NotificationViewModel;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.ui.weather.WeatherReport;
import edu.uw.comchat.ui.connection.ConnectionListViewModel;
import edu.uw.comchat.ui.weather.WeatherViewModel;
import edu.uw.comchat.util.StorageUtil;

import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
 * @version 9 December 2020
 */
// Ignore checkstyle member name error.
public class HomeFragment extends Fragment {
  private UserInfoViewModel mUserModel;
  private WeatherViewModel mWeatherModel;
  private FragmentHomeBinding mBinding;

  private NotificationViewModel mNotificationModel;
  // Store incoming connection request.
  private ConnectionListViewModel mConnectionModel;

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
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    mBinding = FragmentHomeBinding.inflate(inflater);
    StorageUtil mStorageUtil = new StorageUtil(getContext());
    int theme = mStorageUtil.loadTheme();
    if (theme == R.style.Theme_ComChatRed){
      mBinding.dividerProfile.setBackgroundColor(getResources().getColor(R.color.redAccentColorLight,
                                                 getActivity().getTheme()));
      mBinding.dividerProfileBottom.setBackgroundColor(getResources().getColor(R.color.redAccentColorLight,
              getActivity().getTheme()));
      mBinding.dividerProfileTop.setBackgroundColor(getResources().getColor(R.color.redAccentColorLight,
              getActivity().getTheme()));

    } else {
      mBinding.dividerProfile.setBackgroundColor(getResources().getColor(R.color.greyAccentColorLight,
                                                 getActivity().getTheme()));
      mBinding.dividerProfileTop.setBackgroundColor(getResources().getColor(R.color.greyAccentColorLight,
              getActivity().getTheme()));
      mBinding.dividerProfileBottom.setBackgroundColor(getResources().getColor(R.color.greyAccentColorLight,
              getActivity().getTheme()));
    }
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Sets user email
    String hello = getString(R.string.text_home_hello) + " " + mUserModel.getEmail();
    mBinding.textHomeUserEmail.setText(hello);

    // Set up weather connection
    mWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    StorageUtil util = new StorageUtil(getContext());
    mWeatherModel.connect(util.getLocation());

    // Sets layout for recycler view
    SnapHelper helper = new LinearSnapHelper();
    helper.attachToRecyclerView(mBinding.listHomeWeather);
    mBinding.listHomeWeather.setLayoutManager(new LinearLayoutManager(
            getContext(), LinearLayoutManager.HORIZONTAL, false));

    // Set scroll behaviour for notification message
    mBinding.textHomeWelcomeMessage.setMovementMethod(new ScrollingMovementMethod());

    // Notification response observer
    mNotificationModel.addChatResponseObserver(getViewLifecycleOwner(), notificationMap -> {
      try {
        // Show notification and hide display message
        mBinding.textHomeNotificationDisplay.setVisibility(View.INVISIBLE);
        mBinding.layoutInnerHomeNotification.setVisibility(View.VISIBLE);

        mBinding.textHomeNotificationMessage.setText(mNotificationModel.getLatestNotificationMessage());
        mBinding.textHomeNotificationSender.setText(mNotificationModel.getLatestNotificationSender());
        mBinding.textHomeNotificationDate.setText(mNotificationModel.getLatestNotificationDate());
        mBinding.textHomeNotificationTime.setText(mNotificationModel.getLatestNotificationTime());
      } catch (NullPointerException e){
        Log.i("Home Fragment", "NPE for at notification due to no message has been received, this error can be ignored.");

        // Hide empty notification and show display
        mBinding.layoutInnerHomeNotification.setVisibility(View.INVISIBLE);
        mBinding.textHomeNotificationDisplay.setVisibility(View.VISIBLE);
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
    mConnectionModel.addIncomingListObserver(getViewLifecycleOwner(), listConnection ->
            mNotificationModel.updateConnectionNotificationData(listConnection));

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
          populateWeatherInfo(response);
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
  private void populateWeatherInfo(JSONObject response) throws JSONException {
    JSONObject currentWeather = response.getJSONObject("current");
    JSONObject weatherObject = currentWeather.getJSONArray("weather").getJSONObject(0);

    // Get sunrise and sunset time.
    long sunriseTime = currentWeather.getLong("sunrise");
    long sunsetTime = currentWeather.getLong("sunset");
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    final String formattedSunriseTime = Instant.ofEpochSecond(sunriseTime)
            .atZone(TimeZone.getDefault().toZoneId())
            .format(formatter);
    final String formattedSunsetTime = Instant.ofEpochSecond(sunsetTime)
            .atZone(TimeZone.getDefault().toZoneId())
            .format(formatter);

    // Set location
    String location = response.getString("City")
            + ", " + response.getString("State")
            + ", " + response.getString("Country");
    mBinding.textHomeWeatherLocation.setText(location);

    // Set temp
    String currentTemp = currentWeather.getString("temp") + "\u00B0";
    mBinding.textHomeWeatherCurrent.setText(currentTemp);

    // Get description
    mBinding.textHomeWeatherDescription.setText(weatherObject.getString("description"));

    // Get image icon using weather id
    int id = weatherObject.getInt("id");
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
      if (currentTime.isAfter(LocalTime.parse(formattedSunriseTime)) && currentTime.isBefore(LocalTime.parse(formattedSunsetTime))) {
        mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_clear_sky_day);
      } else {
        mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_clear_sky_night);
      }
    } else if (id >= 801 && id <= 804) { // ID for cloud
      mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_cloudy);
    }

    // Set recycler view
    ArrayList<WeatherReport> dailyReports = new ArrayList<>();
    JSONArray dailyArray = response.getJSONArray("daily");
    try {
      for (int i = 0; i < dailyArray.length(); i++) {
        JSONObject json = dailyArray.getJSONObject(i);
        JSONObject time = json.getJSONObject("dt");
        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
        dailyReports.add(new WeatherReport(
                time.getString("months") + "/" + time.getString("dates"),
                json.getJSONObject("temp").getString("day") + "\u00B0",
                weather.getString("description"),
                weather.getInt("id")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    mBinding.listHomeWeather.setAdapter(new HomeWeatherRecyclerViewAdapter(dailyReports));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_home, menu);
  }
}