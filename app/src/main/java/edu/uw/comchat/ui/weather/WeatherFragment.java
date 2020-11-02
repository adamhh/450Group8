package edu.uw.comchat.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentWeatherBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

  WeatherStateAdapter weatherStateAdapter;
  private ViewPager2 mViewPager;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_weather, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // super.onViewCreated(view, savedInstanceState);
    weatherStateAdapter = new WeatherStateAdapter(this);
    mViewPager = view.findViewById(R.id.pager);
    mViewPager.setAdapter(weatherStateAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout);
    new TabLayoutMediator(tabLayout, mViewPager,
            (tab, position) -> tab.setText("OBJECT " + (position + 1))).attach();
  }
}