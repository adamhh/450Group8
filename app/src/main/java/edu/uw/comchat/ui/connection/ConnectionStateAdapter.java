package edu.uw.comchat.ui.connection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * The state adapter that is used with the connection tab layout.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionStateAdapter extends FragmentStateAdapter {

  /**
   * Creates a new connection state adapter from the given fragment.
   *
   * @param fragment the fragment containing the state adapter.
   */
  public ConnectionStateAdapter(Fragment fragment) {
    super(fragment);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {

    // TODO Set the connection list up properly
    Fragment fragment = new ConnectionListFragment();
    return fragment;
  }

  @Override
  public int getItemCount() {
    return 3;
  }
  // Checkstyle: Done - Hung Vu
}
