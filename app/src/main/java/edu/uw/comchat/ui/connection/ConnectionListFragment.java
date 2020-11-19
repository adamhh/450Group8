package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentConnectionCardBinding;
import edu.uw.comchat.databinding.FragmentConnectionListBinding;

/**
 * A fragment that is used to show connections in a list view.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionListFragment extends Fragment {

  private ConnectionListViewModel mModel;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_connection_list, container, false);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
    mModel.connectGet();

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentConnectionListBinding binding = FragmentConnectionListBinding.bind(getView());

    mModel.addConnectionListObserver(getViewLifecycleOwner(), connectionList -> {
      if (!connectionList.isEmpty()) {
        binding.listRootConnection.setAdapter(
                new ConnectionRecyclerViewAdapter(connectionList)
        );
        //binding.layoutWait.setVisibility(View.GONE);
      }
    });
  }
  // Checkstyle: Done - Hung Vu
}