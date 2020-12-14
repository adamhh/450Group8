package edu.uw.comchat.ui.connection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.comchat.MainActivity;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentConnectionCardBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.util.ColorUtil;

import java.util.List;

/**
 * A recycler view adapter to be used for the list of a connections a user has.
 *
 * @author Jerry Springer
 * @author Adam Hall
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionRecyclerViewAdapter extends
        RecyclerView.Adapter<ConnectionRecyclerViewAdapter.ConnectionViewHolder> {
  /**
   * The list of our connection objects.
   */
  private final List<Connection> mConnections;
  /**
   * An instance of the recycler view we are using.
   */
  private RecyclerView mRecyclerView;

  /**
   * An instance of the user info view model that will provide email information.
   */
  private UserInfoViewModel mUserModel;
  /**
   * Represents the position of the recycler tab view (1-4)
   */
  private final int mPosition;
  /**
   * An instance of the user info view model that will provide email information.
   */
  private ConnectionListViewModel mConnectionViewModel;

  private final ColorUtil mColorUtil;
  Parcelable recyclerViewState;

  /**
   * Creates a new connection recycler view adapter with the given list of connections.
   *
   * @param items the list of chats to be displayed.
   */
  public ConnectionRecyclerViewAdapter(List<Connection> items, int position, MainActivity m, ColorUtil colorUtil) {
    this.mConnections = items;
    mPosition = position;
    mConnectionViewModel = new ViewModelProvider(m).get(ConnectionListViewModel.class);
    mColorUtil = colorUtil;
  }

  @NonNull
  @Override
  public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    return new ConnectionViewHolder(LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.fragment_connection_card, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
    holder.setConnection(mConnections.get(position), mColorUtil);

  }



  @Override
  public int getItemCount() {
    return mConnections.size();
  }

  @Override
  public int getItemViewType(int position){
    return position;
  }
  @Override
  public long getItemId(int position) {
    return position;
  }
  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);

    // Store the recycler view so we can unwrap the view index in on click
    mRecyclerView = recyclerView;
  }

  /**
   * Objects from this class represent a single connection row view from the users connections.
   */
  public class ConnectionViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public FragmentConnectionCardBinding mBinding;

    public ConnectionViewHolder(View view) {
      super(view);
      mView = view;
      mBinding = FragmentConnectionCardBinding.bind(view);
      mBinding.cardRootConnectionCard.setOnClickListener(this::onClick);


    }


    /**
     * Navigates to the profile the user clicks on from connection.
     *
     * @param view the view that was clicked on.
     */
    private void onClick(View view) {
      int position = mRecyclerView.getChildAdapterPosition(view);
      // TODO Make this use a message Id
      String profileId = mConnections.get(position).getEmail();
      String firstId = mConnections.get(position).getFirstName();
      String lastId = mConnections.get(position).getLastName();
      String nickName = mConnections.get(position).getNickName();
      Log.d("NICKY", nickName);
      Navigation.findNavController(view).navigate(
              ConnectionFragmentDirections
                      .actionNavigationConnectionToProfileFragment(profileId, mPosition,
                                                                   firstId, lastId, nickName));
    }

    /**
     * Stores the data of the corresponding connection and updates the view.
     *
     * @param connection the connection of the view holder.
     */
    void setConnection(final Connection connection, final ColorUtil colorUtil) {
      // TODO Make this get the person who is not you, if not found throw error
      mBinding.cardEmailId.setText(connection.getEmail());
      mBinding.cardFnameId.setText(connection.getFirstName());
      mBinding.cardLnameId.setText(connection.getLastName());
      mBinding.cardAvatarId.setImageResource(Connection.getAvatar(connection.getEmail()));
      colorUtil.setColor(mBinding.dividerBottom);
    }

  }
}
