package edu.uw.comchat.ui.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatGroupBinding;
import edu.uw.comchat.util.ColorUtil;

import java.util.List;


/**
 * A recycler view adapter to be used for the list of chat groups
 * a user belongs to.
 *
 * @author Jerry Springer
 @version 12 Dec 2020
 */
// Ignore checkstyle member name error.
public class GroupRecyclerViewAdapter extends
        RecyclerView.Adapter<GroupRecyclerViewAdapter.GroupViewHolder> {

  private final List<ChatRoomInfo> mGroups;
  private RecyclerView mRecyclerView;
  private ColorUtil mColorUtil;

  /**
   * Creates a new group recycler view adapter with the given list of groups.
   *
   * @param groups the list of groups to be displayed.
   * @param colorUtil the utility used to help manage theme coloring.
   */
  public GroupRecyclerViewAdapter(List<ChatRoomInfo> groups, ColorUtil colorUtil) {
    mGroups = groups;
    mColorUtil = colorUtil;
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
    holder.setGroup(mGroups.get(position), mColorUtil);
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

    int groupId = mGroups.get(position).getRoomId();
    boolean isGroupChat = mGroups.get(position).isGroupChat();

    // Navigates to the group
    Navigation.findNavController(view).navigate(
            ChatPageFragmentDirections
                    .actionNavigationChatToMessageListFragment(groupId, isGroupChat));
  }

  /**
   * Objects from this class represent a single row view in the list of groups a user is in.
   */
  static class GroupViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public FragmentChatGroupBinding mBinding;

    /**
     * Creates a new view  holder containing the group card fragment.
     *
     * @param view the corresponding view in the recycler view.
     */
    public GroupViewHolder(View view) {
      super(view);
      mView = view;
      mBinding = FragmentChatGroupBinding.bind(view);
    }

    /**
     * Stores the data of the corresponding group and updates the view.
     *
     * @param group the group of the view holder.
     */
    // Small adjustment to comply with 12/8/20 API - Hung Vu
    void setGroup(final ChatRoomInfo group, final ColorUtil colorUtil) {
      mBinding.textChatGroupName.setText(group.getRoomName());
      colorUtil.setColor(mBinding.dividerBottom);

      String preview = group.getMessage();
      if (preview.length() > 70)
        preview = preview.substring(0, 70) + "...";
      mBinding.textChatGroupMessage.setText(preview);

      mBinding.textChatGroupType.setText(group.isGroupChat() ? "Group" : "Direct Message");
      try {
        mBinding.textChatGroupDate.setText(group.getTime().substring(11, 16));
      } catch (IndexOutOfBoundsException e){
        Log.i("GroupRVAdapter", "Index out of bound in parsing time due to empty msg group chat");
      }
    }
  }
}
