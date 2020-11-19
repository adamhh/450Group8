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
import edu.uw.comchat.databinding.FragmentWeatherBinding;
import edu.uw.comchat.databinding.FragmentWeatherFiveDayBinding;
import edu.uw.comchat.databinding.FragmentWeatherFiveDayCardBinding;
import edu.uw.comchat.model.UserInfoViewModel;

/**
 * Fragment that shows the weather in a tabular layout for multiple
 * types of weather reports.
 *
 * @author Jerry Springer (UI), Hung Vu (connect to webservice and populate data).
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherFragment extends Fragment {

  private WeatherStateAdapter weatherStateAdapter;
  private ViewPager2 mViewPager;

  // Connect to webservice - Hung Vu.
  private WeatherViewModel mWeatherModel;
  private FragmentWeatherBinding mWeatherBinding;


  public WeatherFragment() {

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

    // Set jwt dynamically.
    UserInfoViewModel userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    String jwt = userInfoViewModel.getJwt();
    mWeatherModel.getToken().setValue(jwt);


  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mWeatherBinding = FragmentWeatherBinding.inflate(inflater);
    return mWeatherBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    weatherStateAdapter = new WeatherStateAdapter(this);
    mViewPager = view.findViewById(R.id.pager_weather);
    mViewPager.setAdapter(weatherStateAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout_weather);

    String[] tabNames = {getString(R.string.item_weather_current),
            getString(R.string.item_weather_daily),
            getString(R.string.item_weather_five_day)};

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
//    mWeatherCurrentBinding = FragmentWeatherCurrentBinding.bind(getView());
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

  // TODO there is no location info in JSON response. This method does nothing, just a template for later modification.
  private void populateWeatherPage(JSONObject response) throws JSONException {
    mWeatherBinding.textWeatherLocation.setText(
            "Location: " + response.getString("City")
                    + ", " + response.getString("State")
                    + ", " + response.getString("Country")
    );

  }


}