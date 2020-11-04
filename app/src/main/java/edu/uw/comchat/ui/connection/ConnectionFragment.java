package edu.uw.comchat.ui.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.comchat.R;

/**
 * A fragment that holds the UI for a user managing their connections.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
public class ConnectionFragment extends Fragment {

  private ConnectionStateAdapter connectionStateAdapter;
  private ViewPager2 mViewPager;

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
    new TabLayoutMediator(tabLayout, mViewPager,
            (tab, position) -> tab.setText("Connections " + (position + 1))).attach();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_connection, menu);
  }
}