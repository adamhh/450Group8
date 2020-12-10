package edu.uw.comchat.ui.chat.chatroom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentMessageListBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.ui.connection.Connection;
import edu.uw.comchat.ui.connection.ConnectionListViewModel;
import edu.uw.comchat.util.ModifyChatRoom;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;



/**
 * A fragment that shows the list of messages in a group.
 * <p></p>
 * * @author Jerry Springer (UI), Hung Vu (back-end)
 * * @version 11 November 2020
 */
// Ignore checkstyle member name error.
public class MessagePageFragment extends Fragment {

  // Implement send message.
  private ChatViewModel mChatModel;
  private UserInfoViewModel mUserModel;
  private ChatSendViewModel mSendModel;
  private int mChatId;
  private Boolean mEnableMenu;

  // Get in-room info
  private InRoomInfoViewModel mInRoomInfoViewModel;
  private ConnectionListViewModel mConnectionListViewModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    mChatId = MessagePageFragmentArgs.fromBundle(getArguments()).getChatId();
    mEnableMenu = MessagePageFragmentArgs.fromBundle(getArguments()).getEnableMenu();
    // Chat room.
    mSendModel = provider.get(ChatSendViewModel.class);
    mUserModel = provider.get(UserInfoViewModel.class);
    mChatModel = provider.get(ChatViewModel.class);
    mInRoomInfoViewModel = provider.get(InRoomInfoViewModel.class);
    mConnectionListViewModel = provider.get(ConnectionListViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // If true -> group chat -> enable menu, otherwise -> DM -> no menu.
    setHasOptionsMenu(mEnableMenu);
    return inflater.inflate(R.layout.fragment_message_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());

    // Send button was clicked. Send the message via the SendViewModel
    binding.buttonSend.setOnClickListener(button -> {
      mSendModel.sendMessage(mChatId,
              mUserModel.getJwt(),
              binding.editMessageBox.getText().toString());
    });

    //when we get the response back from the server, clear the edittext
    mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
            binding.editMessageBox.setText(""));

    // Shows the internal Swiper view progress bar until messages load
    binding.swipeContainer.setRefreshing(true);

    final RecyclerView rv = binding.recyclerMessages;
    // Sets the Adapter to hold a reference to the list for this group ID that the ViewModel holds
    rv.setAdapter(new ChatRecyclerViewAdapter(
            mChatModel.getMessageListByChatId(mChatId),
            mUserModel.getEmail(),
            getActivity().getTheme()
    ));

    //When the user scrolls to the top of the RV, the swiper list will "refresh"
    //The user is out of messages, go out to the service and get more
    binding.swipeContainer.setOnRefreshListener(() -> {
      mChatModel.getNextMessages(mChatId, mUserModel.getJwt());
    });

    mChatModel.addMessageObserver(mChatId, getViewLifecycleOwner(),
            list -> {
              // TODO note from lab, need to consider
              /*
               * This solution needs work on the scroll position. As a group,
               * you will need to come up with some solution to manage the
               * recyclerview scroll position. You also should consider a
               * solution for when the keyboard is on the screen.
               */
              //inform the RV that the underlying list has (possibly) changed
              rv.getAdapter().notifyDataSetChanged();
              rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
              binding.swipeContainer.setRefreshing(false);
            });

    mChatModel.getFirstMessages(mChatId, mUserModel.getJwt());

    mInRoomInfoViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
      // For now, the response doesn't cause any changes to the UI - Hung Vu.
    });
    mInRoomInfoViewModel.getEmailOfUserInRoom(mChatId, mUserModel.getJwt());

    mConnectionListViewModel.addConnectionListObserver(getViewLifecycleOwner(), response -> {
      // For now, the response doesn't cause any changes to the UI - Hung Vu.
    });
    mConnectionListViewModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());

  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_chatroom, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.menu_chat_list_add) {
      handleAddMemberToChatRoomAction();
    } else if (id == R.id.menu_chat_list_remove) {
      handleRemoveMemberToChatRoomAction();
    } else if (id == R.id.menu_chat_list_delete_room) {
      new MaterialAlertDialogBuilder(getActivity())
              .setTitle("Message")
              .setMessage("Are you sure want to delete this room? This will remove all chat in the room along with its members.")
              .setNegativeButton(getResources().getString(R.string.item_menu_chat_list_decline),
                      (dialog, which) -> {
                      })
              .setPositiveButton(getResources().getString(
                      R.string.item_menu_chat_list_accept), (dialog, which) -> {
                ArrayList<String> roomToDelete = new ArrayList<String>();
                roomToDelete.add(String.valueOf(mChatId));
                roomToDelete.add(mUserModel.getJwt());
                ModifyChatRoom.deleteRoom().accept(roomToDelete,this);
              })
              .show();
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * This creates a dialog which contains name of users in chat room
   * and handle removing action for user.
   */
  private void handleRemoveMemberToChatRoomAction() {
    List<String> emailList = mInRoomInfoViewModel.getMemberList();
    // Default choice at index 0
    emailList.add(0, "None");
    CharSequence[] multiItems = emailList.toArray(new CharSequence[emailList.size()]);
    createDialog(multiItems, ModifyChatRoom.removeMember());
    // Note, if the response is delayed, the next call to this method will still use old info.
    //  However, the response can arrive when the dialog is opened with old info
    //  Since the response update info already, it can potentially cause OutOfBound - Hung Vu
    mInRoomInfoViewModel.getEmailOfUserInRoom(mChatId, mUserModel.getJwt());
  }

  /**
   * This creates a dialog which contains name of users in chat room
   * and handle adding action for user.
   */
  private void handleAddMemberToChatRoomAction() {
    List<Connection> friendList = mConnectionListViewModel.getConnectionList();
    List<String> emailList = friendList.stream()
            .map(connection -> connection.getEmail())
            .collect(Collectors.toList());
    // Default choice at index 0
    emailList.add(0, "None");
    CharSequence[] multiItems = emailList.toArray(new CharSequence[emailList.size()]);
    //    boolean[] checkedItems = new boolean[multiItems.length];
    //    Log.i("", String.valueOf(multiItems[0]));
    //    Log.i("", friendList.toString());
    createDialog(multiItems, ModifyChatRoom.addMember());
    mConnectionListViewModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());

  }

  /**
   * This is a helper method which create dialog and perform actions accordingly in chat room.
   * For example, create a dialog for remove member from group chat. This also perform request
   * to a respective web service endpoint to perform an action.
   *
   * @param multiItems  a CharSequence array required to create a dialog
   * @param whichAction a function indicate whether a user want to add or remove user from chat.
   */
  private void createDialog(CharSequence[] multiItems,
                            BiConsumer<ArrayList<String>, Fragment> whichAction) {
    // Work around since "which" in setPositiveButton isn't the same as "which"
    //  in setSingleChoiceItem.
    int[] choice = new int[1];
    new MaterialAlertDialogBuilder(getActivity())
            //Multi-choice items (initialized with checked items)
            .setSingleChoiceItems(multiItems, 0, (dialog, which) -> {
              choice[0] = which;
            })
            .setPositiveButton(getResources().getString(
                    R.string.item_menu_chat_list_accept), (dialog, which) -> {

                      if (choice[0] != 0) {
                        ArrayList<String> memberToModify = new ArrayList<>();
                        // Catch index out of bound exception due to slow response.
                        // Log.i("", String.valueOf(choice[0]));
                        try {
                          memberToModify.add(String.valueOf(mChatId));
                          // Choice has "None" at i = 0, so its length = userList + 1
                          memberToModify.add(String.valueOf(multiItems[choice[0]]));
                          memberToModify.add(mUserModel.getJwt());
                          memberToModify.add(mUserModel.getEmail());
                        } catch (IndexOutOfBoundsException e) {
                          // TODO dialog not show? Maybe because getActivity doesn't work inside lambda method? - Hung Vu
                          // userList -> multiItems[]
                          // A change has been made to this method to "completely" mitigate the exception,
                          //  but we will keep the catch phrase here just in case. However, the behavior
                          //  (showing "removed" user) is still there, and will cause HTTP 400 response.
                          new MaterialAlertDialogBuilder(getActivity())
                                  .setTitle("Status")
                                  .setMessage("Remove unsuccessfully. Please try again. "
                                          + "Tap anywhere to close this message. ");
                          Log.i("MessagePageFragment", "Index out of bound when remove user.");
                          return;
                        }
                        whichAction.accept(memberToModify, this);
                      }
                    }
            )
            .setNegativeButton(getResources().getString(R.string.item_menu_chat_list_decline),
                    (dialog, which) -> {
                    })
            .show();
  }

  @Override
  public void onResume() {
    // TODO When a user pause activity (app to background), user will receive msg over notification.
    //  However, since there is no view, that msg is not drawn on anything so when we return
    //  to our app.
    //  The latest msg is not there. I haven't found out a way to fix this yet - Hung Vu
    super.onResume();
    //    FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());
    //    final RecyclerView rv = binding.recyclerMessages;
    //    mChatModel.getFirstMessages(HARD_CODED_CHAT_ID, mUserModel.getJwt());
    //    rv.setAdapter(new ChatRecyclerViewAdapter(
    //            mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID),
    //            mUserModel.getEmail()
    //    ));
    //    rv.getAdapter().notifyDataSetChanged();
    //    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
  }

}