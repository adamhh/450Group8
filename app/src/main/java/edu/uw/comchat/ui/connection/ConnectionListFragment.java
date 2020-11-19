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
import edu.uw.comchat.model.UserInfoViewModel;

/**
 * A fragment that is used to show connections in a list view.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionListFragment extends Fragment {
  /**
   * The argument for which tab position.
   */
  public static final String ARG_POSITION = "position";
  /**
   * An instance of the view model.
   */
  private ConnectionListViewModel mModel;
  /**
   * A bundle that will give access to the arguments.
   */
  private Bundle mArgs;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
    mModel.connectGet();

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view;
    switch (getArguments().getInt(ARG_POSITION)) {
      // Not using layout ids as case as it is not recommended
      // Layout ids are not final on runtime could cause error?
      case 1:
        view = inflater.inflate(R.layout.fragment_connection_existing, container, false);
        break;

      case 2:
        view = inflater.inflate(R.layout.fragment_connection_incoming, container, false);
        break;

      case 3:
        view = inflater.inflate(R.layout.fragment_connection_outgoing, container, false);
        break;
      case 4:
        view = inflater.inflate(R.layout.fragment_connection_suggested, container, false);
      default:
        // TODO Throw error
        view = null;
        break;
    }

    return view;
  }
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//    super.onViewCreated(view, savedInstanceState);
    FragmentConnectionListBinding binding = FragmentConnectionListBinding.bind(getView());
    mArgs = getArguments();

    /*hard coded recycler view only to populate for existing connections.
    I need to figure out if I can reuse the recycler view for all four tabs
    or if i'll have to have four different recycler views.  I should know more once the
    the webservice is set up for incoming/outgoing requests and whatever we choose for suggested
    */

    mModel.addConnectionListObserver(getViewLifecycleOwner(), connectionList -> {
      if (!connectionList.isEmpty() && mArgs.getInt(ARG_POSITION) == 1) {
        binding.listRootConnection.setAdapter(
                new ConnectionRecyclerViewAdapter(connectionList)
        );
        //binding.layoutWait.setVisibility(View.GONE);
      }
    });
  }

  // Checkstyle: Done - Hung Vu
}