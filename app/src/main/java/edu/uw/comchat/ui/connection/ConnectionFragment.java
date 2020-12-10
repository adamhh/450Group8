package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

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
  //delete?
  private View mView;

  /**
   * An instance of our ConnectionListViewModel
   */
  private ConnectionListViewModel mConnectionViewModel;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mConnectionViewModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
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
    mView = view;
    TabLayout tabLayout = view.findViewById(R.id.tab_layout_connection);

    String[] tabNames = {getString(R.string.connection_tab_existing), getString(R.string.connection_tab_incoming),
            getString(R.string.connection_tab_outgoing), getString(R.string.connection_tab_suggested)};
    new TabLayoutMediator(tabLayout, mViewPager,
            (tab, position) -> tab.setText(tabNames[position])).attach();
  }
  MenuItem menuItem;
  SearchView searchView;
  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_connection, menu);
    menuItem = menu.findItem(R.id.connection_search);
    searchView = (SearchView) menuItem.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override //Submit clicked
      public boolean onQueryTextSubmit(String query) {
        //Grab lists and initialize variables
        List<Connection> connList = mConnectionViewModel.getConnectionList();
        List<Connection> incList = mConnectionViewModel.getIncomingList();
        List<Connection> outList = mConnectionViewModel.getOutgoingList();
        List<Connection> suggList = mConnectionViewModel.getSuggestedList();

        String email = "";
        String fName = "";
        String lName = "";
        String nickName = "";
        int tabPosition = 0;
        boolean found = false;
        boolean searching = true;
        //Search each list, keep searching until found = true or searching = false
        //Found if found, searching false if found or search ends
        while (searching) {
          //Search current connections
          for (int i = 0; i < connList.size(); i++) {
            Connection c = connList.get(i);
            if (checkForMatch(query, c)) {
              email = c.getEmail();
              fName = c.getFirstName();
              lName = c.getLastName();
              nickName = c.getNickName();
              tabPosition = 1;
              found = true;
              searching = false;
              i = connList.size();
            }
          }

          //Search incoming connections
          for (int j = 0; j < incList.size(); j++) {
            Connection c = incList.get(j);
            if (checkForMatch(query, c)) {
              email = c.getEmail();
              fName = c.getFirstName();
              lName = c.getLastName();
              nickName = c.getNickName();
              tabPosition = 2;
              found = true;
              searching = false;
              j = incList.size();
            }
          }

          //Search outgoing connections
          for (int h = 0; h < outList.size(); h++) {
            Connection c = outList.get(h);
            if (checkForMatch(query, c)) {
              email = c.getEmail();
              fName = c.getFirstName();
              lName = c.getLastName();
              nickName = c.getNickName();
              tabPosition = 3;
              found = true;
              searching = false;
              h = outList.size();
            }
          }

          //Search suggested connections
          for (int k = 0; k < suggList.size(); k++) {
            Connection c = suggList.get(k);
            if (checkForMatch(query, c)) {
              email = c.getEmail();
              fName = c.getFirstName();
              lName = c.getLastName();
              nickName = c.getNickName();
              tabPosition = 4;
              found = true;
              searching = false;
              k = suggList.size();
            }
          }
          searching = false;
        }
        //If found navigate to profile
        if (found) {
          Navigation.findNavController(mView).navigate(
                  ConnectionFragmentDirections
                          .actionNavigationConnectionToProfileFragment(email, tabPosition,
                                  fName, lName, nickName));
        }
        else {
          new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                  .setMessage("\"" + query + "\"" + " Not Found")
                  .setTitle("Error locating User")
                  .setPositiveButton(R.string.connection_search_ok, (dialog, which) -> {
                  })
                  .show();
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
  }
  private boolean checkForMatch(String query, Connection c) {
    return c.getEmail().toLowerCase().equals(query.trim().toLowerCase())
            || (c.getFirstName().toLowerCase() + " " + c.getLastName().toLowerCase()).equals(query.trim().toLowerCase())
            || c.getNickName().toLowerCase().equals(query.trim().toLowerCase());
  }

}