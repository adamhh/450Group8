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
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.ui.chat.ChatGroupInfo;
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
  UserInfoViewModel mUserViewModel;
  private MutableLiveData<JSONObject> mResponse;
  private ConnectionStateAdapter connectionStateAdapter;
  private ViewPager2 mViewPager;
  private String mEmail;
  private ArrayList<String> mConnections = new ArrayList<>();


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());

    return inflater.inflate(R.layout.fragment_connection, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    connectionStateAdapter = new ConnectionStateAdapter(this);
    mViewPager = view.findViewById(R.id.pager_connection);
    mViewPager.setAdapter(connectionStateAdapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout_connection);

    String[] tabNames = {"Existing", "Incoming", "Outgoing", "Suggested"};
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