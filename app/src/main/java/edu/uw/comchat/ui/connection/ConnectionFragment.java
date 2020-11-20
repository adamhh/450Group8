package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
 * A fragment that holds the UI for a user managing their connections.
 *
 * @author Jerry Springer
 * Added a tab, and changed the names
 * @author Adam Hall
 * @version 18 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionFragment extends Fragment {

  /**
   * An instance of the connection state adapter.
   */
  private ConnectionStateAdapter connectionStateAdapter;
  /**
   * An instance of a page viewer.
   */
  private ViewPager2 mViewPager;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_connection, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    connectionStateAdapter = new ConnectionStateAdapter(this);
    mViewPager = view.findViewById(R.id.pager_connection);
    mViewPager.setAdapter(connectionStateAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout_connection);

    String[] tabNames = {getString(R.string.connection_tab_existing), getString(R.string.connection_tab_incoming),
            getString(R.string.connection_tab_outgoing), getString(R.string.connection_tab_suggested)};
    new TabLayoutMediator(tabLayout, mViewPager,
            (tab, position) -> tab.setText(tabNames[position])).attach();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_connection, menu);
  }

}