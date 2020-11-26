package edu.uw.comchat.ui.chat.chatroom;

import android.app.Application;
import android.os.Bundle;
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
  private int chatId;

  // Get in-room info
  private InRoomInfoViewModel mInRoomInfoViewModel;
  private ConnectionListViewModel mConnectionListViewModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    chatId = MessagePageFragmentArgs.fromBundle(getArguments()).getChatId();
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
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_message_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());

    // Send button was clicked. Send the message via the SendViewModel
    binding.buttonSend.setOnClickListener(button -> {
      mSendModel.sendMessage(chatId,
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
            mChatModel.getMessageListByChatId(chatId),
            mUserModel.getEmail(),
            getActivity().getTheme()
    ));

    //When the user scrolls to the top of the RV, the swiper list will "refresh"
    //The user is out of messages, go out to the service and get more
    binding.swipeContainer.setOnRefreshListener(() -> {
      mChatModel.getNextMessages(chatId, mUserModel.getJwt());
    });

    mChatModel.addMessageObserver(chatId, getViewLifecycleOwner(),
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

    mChatModel.getFirstMessages(chatId, mUserModel.getJwt());

    mInRoomInfoViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
      // For now, the response doesn't cause any changes to the UI - Hung Vu.
    });
    mInRoomInfoViewModel.getEmailOfUserInRoom(chatId, mUserModel.getJwt());

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
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * This creates a dialog which contains name of users in chat room
   * and handle removing action for user.
   */
  private void handleRemoveMemberToChatRoomAction() {
    List<String> emailList = mInRoomInfoViewModel.getMemberList();
    CharSequence[] multiItems = emailList.toArray(new CharSequence[emailList.size()]);
    boolean[] checkedItems = new boolean[emailList.size()];
    createDialog(multiItems, checkedItems, emailList, ModifyChatRoom.removeMember());
    mInRoomInfoViewModel.getEmailOfUserInRoom(chatId, mUserModel.getJwt());
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
    CharSequence[] multiItems = emailList.toArray(new CharSequence[emailList.size()]);
    boolean[] checkedItems = new boolean[multiItems.length];
    createDialog(multiItems, checkedItems, emailList, ModifyChatRoom.addMember());
    mConnectionListViewModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());

  }

  /**
   * This is a helper method which create dialog and perform actions accordingly in chat room.
   * For example, create a dialog for remove member from group chat. This also perform request
   * to a respective web service endpoint to perform an action.
   *
   * @param multiItems   a CharSequence array required to create a dialog
   * @param checkedItems a list of checked item in a box
   * @param userList     a list of user email required for add and delete operations
   * @param whichAction  a function indicate whether a user want to add or remove user from chat.
   */
  private void createDialog(CharSequence[] multiItems, boolean[] checkedItems,
                            List<String> userList, BiConsumer<ArrayList<String>,
                            Application> whichAction) {
    new MaterialAlertDialogBuilder(getActivity())
            //Multi-choice items (initialized with checked items)
            .setMultiChoiceItems(multiItems, checkedItems, (dialog, which, checked) -> {
              checkedItems[which] = checked;
            })
            .setPositiveButton(getResources().getString(
                    R.string.item_menu_chat_list_accept),
                    (dialog, which) -> {
                      for (int i = 0; i < checkedItems.length; i++) {
                        if (checkedItems[i] == true) {
                          ArrayList<String> memberToModify = new ArrayList<>();
                          memberToModify.add(String.valueOf(chatId));
                          memberToModify.add(userList.get(i));
                          memberToModify.add(mUserModel.getJwt());
                          whichAction.accept(memberToModify, getActivity().getApplication());
                        }
                      }
              })
            .setNegativeButton(getResources().getString(R.string.item_menu_chat_list_decline),
                    (dialog, which) -> { })
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