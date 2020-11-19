package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentConnectionBinding;
import edu.uw.comchat.databinding.FragmentSettingsBinding;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.ui.chat.ChatGroupInfo;
import edu.uw.comchat.ui.settings.SettingsViewModel;
import edu.uw.comchat.util.HandleRequestError;

/**
 * A fragment that holds the UI for a user managing their connections.
 *
 * @author Jerry Springer
 * Added a tab, and changed the names
 * @author Adam Hall
 * @version 3 November 2020
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
  /**
   * An instance of the connection view model that this class will update with the email.
   */
  private ConnectionListViewModel mModel;
  /**
   * An instance of the user info view model that will provide email information.
   */
  private UserInfoViewModel mUserModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    mModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
    Log.d("???", mUserModel.getEmail());
    mModel.setUserEmailConnect(mUserModel.getEmail());

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
  // Checkstyle: Done - Hung Vu
}