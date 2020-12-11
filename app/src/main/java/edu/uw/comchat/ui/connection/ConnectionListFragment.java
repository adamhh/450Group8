package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.uw.comchat.MainActivity;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentConnectionListBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.util.ColorUtil;
import edu.uw.comchat.util.StorageUtil;

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
  private ConnectionListViewModel mConnectionViewModel;

  /**
   * An instance of the user info view model that will provide email information.
   */
  private UserInfoViewModel mUserModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mConnectionViewModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
    mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_connection_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentConnectionListBinding binding = FragmentConnectionListBinding.bind(getView());
    Bundle mArgs = getArguments();
    mConnectionViewModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());

    StorageUtil storageUtil = new StorageUtil(getContext());
    ColorUtil colorUtil = new ColorUtil(getActivity(), storageUtil.loadTheme());

    switch (mArgs.getInt(ARG_POSITION)) {
      case 1:
        mConnectionViewModel.addConnectionListObserver(getViewLifecycleOwner(), connectionList -> binding.listRootConnection.setAdapter(
                new ConnectionRecyclerViewAdapter(connectionList, 1, (MainActivity) ConnectionListFragment.this.getActivity(), colorUtil)));
        break;
      case 2:
        mConnectionViewModel.addIncomingListObserver(getViewLifecycleOwner(), connectionList -> binding.listRootConnection.setAdapter(
                new ConnectionRecyclerViewAdapter(connectionList, 2, (MainActivity) getActivity(), colorUtil)));
        break;
      case 3:
        mConnectionViewModel.addOutgoingListObserver(getViewLifecycleOwner(), connectionList -> binding.listRootConnection.setAdapter(
                new ConnectionRecyclerViewAdapter(connectionList, 3, (MainActivity) getActivity(), colorUtil)));
        break;
      default:
        mConnectionViewModel.addSuggestedListObserver(getViewLifecycleOwner(), connectionList -> binding.listRootConnection.setAdapter(
                new ConnectionRecyclerViewAdapter(connectionList, 4, (MainActivity) getActivity(), colorUtil)));
        break;


    }
  }

}