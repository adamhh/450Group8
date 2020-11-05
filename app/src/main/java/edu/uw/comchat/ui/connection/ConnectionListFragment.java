package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentConnectionListBinding;

/**
 * A fragment that is used to show connections in a list view.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionListFragment extends Fragment {

  FragmentConnectionListBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_connection_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding = FragmentConnectionListBinding.bind(view);

    // Sets the recycler view adapter
    binding.listRootConnection.setAdapter(
            new ConnectionRecyclerViewAdapter(ConnectionGenerator.getConnections()));

    // Adds the divider between list items
    binding.listRootConnection.addItemDecoration(
            new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
  }
  // Checkstyle: Done - Hung Vu
}