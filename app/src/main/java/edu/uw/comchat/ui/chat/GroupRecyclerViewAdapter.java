package edu.uw.comchat.ui.chat;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatGroupBinding;
import java.util.List;

/**
 * A recycler view adapter to be used for the list of chat groups
 * a user belongs to.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class GroupRecyclerViewAdapter extends
        RecyclerView.Adapter<GroupRecyclerViewAdapter.GroupViewHolder> {

  private final List<ChatGroupInfo> mGroups;
  private RecyclerView mRecyclerView;

  /**
   * Creates a new group recycler view adapter with the given list of groups.
   *
   * @param items the list of groups to be displayed.
   */
  public GroupRecyclerViewAdapter(List<ChatGroupInfo> items) {
    this.mGroups = items;
  }

  @NonNull
  @Override
  public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.fragment_chat_group, parent, false);

    // Sets the on click listener for the view / card
    view.setOnClickListener(this::onClick);
    return new GroupViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
    holder.setGroup(mGroups.get(position));
  }

  @Override
  public int getItemCount() {
    return mGroups.size();
  }

  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);

    // Store the recycler view so we can unwrap the view index in on click
    mRecyclerView = recyclerView;
  }

  /**
   * Navigates to the conversation the user clicks on.
   *
   * @param view the view that was clicked on.
   */
  private void onClick(View view) {

    // Gets the index of the clicked group
    int position = mRecyclerView.getChildAdapterPosition(view);

    // TODO Make this use a group ID - Done, Hung vu
    Integer groupId = mGroups.get(position).getGroupId();

    // Navigates to the group
    Navigation.findNavController(view).navigate(
            ChatPageFragmentDirections
                    .actionNavigationChatToMessageListFragment(groupId));
  }

  /**
   * Objects from this class represent a single row view in the list of groups a user is in.
   */
  class GroupViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public FragmentChatGroupBinding binding;
    private ChatGroupInfo mGroup;

    /**
     * Creates a new view  holder containing the group card fragment.
     *
     * @param view the corresponding view in the recycler view.
     */
    public GroupViewHolder(View view) {
      super(view);
      mView = view;
      binding = FragmentChatGroupBinding.bind(view);
    }

    /**
     * Stores the data of the corresponding group and updates the view.
     *
     * @param group the group of the view holder.
     */
    void setGroup(final ChatGroupInfo group) {
      mGroup = group;
      binding.textChatGroupName.setText("" + group.getGroupId());

      final String preview = Html.fromHtml(
              "" + group.getMessageId(),
              Html.FROM_HTML_MODE_COMPACT)
              .toString();
      binding.textChatGroupMessage.setText(preview);
    }
  }
  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}
