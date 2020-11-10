package edu.uw.comchat.ui.connection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentConnectionCardBinding;
import java.util.List;

/**
 * A recycler view adapter to be used for the list of a connections a user has.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionRecyclerViewAdapter extends
        RecyclerView.Adapter<ConnectionRecyclerViewAdapter.ConnectionViewHolder> {

  private final List<Connection> mConnections;
  private RecyclerView mRecyclerView;


  /**
   * Creates a new connection recycler view adapter with the given list of connections.
   *
   * @param items the list of chats to be displayed.
   */
  public ConnectionRecyclerViewAdapter(List<Connection> items) {
    this.mConnections = items;
  }

  @NonNull
  @Override
  public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.fragment_connection_card, parent, false);

    // Sets the on click listener for the view / card
    view.setOnClickListener(this::onClick);
    return new ConnectionRecyclerViewAdapter.ConnectionViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
    holder.setConnection(mConnections.get(position));
  }

  @Override
  public int getItemCount() {
    return mConnections.size();
  }

  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);

    // Store the recycler view so we can unwrap the view index in on click
    mRecyclerView = recyclerView;
  }

  /**
   * Navigates to the profile the user clicks on from connection.
   *
   * @param view the view that was clicked on.
   */
  private void onClick(View view) {
    int position = mRecyclerView.getChildAdapterPosition(view);

    // TODO Make this use a message Id
    String profileId = "" + position;

    Navigation.findNavController(view).navigate(
            ConnectionFragmentDirections
                    .actionNavigationConnectionToProfileFragment(profileId));
  }

  /**
   * Objects from this class represent a single connection row view from the users connections.
   */
  public class ConnectionViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public FragmentConnectionCardBinding binding;
    private Connection mConnection;

    /**
     * Creates a new view  holder containing the connection card fragment.
     *
     * @param view the corresponding view in the recycler view.
     */
    public ConnectionViewHolder(View view) {
      super(view);
      mView = view;
      binding = FragmentConnectionCardBinding.bind(view);
    }


    /**
     * Stores the data of the corresponding connection and updates the view.
     *
     * @param connection the connection of the view holder.
     */
    void setConnection(final Connection connection) {
      mConnection = connection;

      // TODO Make this get the person who is not you, if not found throw error
      binding.cardNameId.setText(connection.getPerson2());
    }
  }
  // Checkstyle: Done - Hung Vu
}
