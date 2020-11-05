package edu.uw.comchat.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import edu.uw.comchat.R;

/**
 * Fragment that shows the weather in a tabular layout for multiple
 * types of weather reports.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherFragment extends Fragment {

  private WeatherStateAdapter weatherStateAdapter;
  private ViewPager2 mViewPager;

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
    new TabLayoutMediator(tabLayout, mViewPager,
            (tab, position) -> tab.setText("Weather " + (position + 1))).attach();
  }
  // Checkstyle: Done - Hung Vu
}