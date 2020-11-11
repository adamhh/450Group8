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
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.comchat.R;

/**
 * Fragment that shows the weather in a tabular layout for multiple
 * types of weather reports.
 *
 * @author Jerry Springer, Hung Vu
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherFragment extends Fragment {

  private WeatherStateAdapter weatherStateAdapter;
  private ViewPager2 mViewPager;

  private WeatherViewModel mWeatherModel;

  public WeatherFragment() {

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mWeatherModel = new ViewModelProvider(this).get(WeatherViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_weather, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    weatherStateAdapter = new WeatherStateAdapter(this);
    mViewPager = view.findViewById(R.id.pager_weather);
    mViewPager.setAdapter(weatherStateAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout_weather);

    String[] tabNames = {getString(R.string.item_weather_current),
            getString(R.string.item_weather_daily),
            getString(R.string.item_weather_ten_day)};

    new TabLayoutMediator(tabLayout, mViewPager,
            new TabLayoutMediator.TabConfigurationStrategy() {
              @Override
              public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabNames[position]);
              }
            }).attach();

    // Connect to webservice - Hung Vu.
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
          populateWeatherPage();
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }

  private void populateWeatherPage() {

  }


}