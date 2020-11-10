package edu.uw.comchat.ui.weather;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * The state adapter is used with the weather tab layout.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class WeatherStateAdapter extends FragmentStateAdapter {

  public WeatherStateAdapter(Fragment fragment) {
    super(fragment);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    Fragment fragment = new WeatherCardFragment();

    // TODO Have the weather card be populated with some actual data
    Bundle args = new Bundle();
    args.putInt(WeatherCardFragment.ARG_POSITION, position + 1);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public int getItemCount() {
    return 3;
  }
  // Checkstyle: Done - Hung Vu
}
