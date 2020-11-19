package edu.uw.comchat.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.uw.comchat.R;

/**
 * A fragment to create new groups of people for chats.
 * <p></p>
 * * @author Jerry Springer
 * * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class CreateFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_create, container, false);
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_create, menu);
  }
  // Checkstyle: Done - Hung Vu
}