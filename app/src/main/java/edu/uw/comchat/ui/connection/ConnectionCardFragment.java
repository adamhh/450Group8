package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import edu.uw.comchat.R;

/**
 * A fragment that represents a single card for a connection
 * in the connections list for users. The lists are for existing,
 * outgoing connection requests, and incoming connection requests.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionCardFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_connection_card, container, false);
  }
  // Checkstyle: Done - Hung Vu
}