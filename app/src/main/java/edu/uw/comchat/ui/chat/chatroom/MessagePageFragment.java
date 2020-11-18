package edu.uw.comchat.ui.chat.chatroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentMessageListBinding;
import edu.uw.comchat.model.UserInfoViewModel;


/**
 * A fragment that shows the list of messages in a group.
 * <p></p>
 * * @author Jerry Springer (UI), Hung Vu (back-end)
 * * @version 11 November 2020
 */
// Ignore checkstyle member name error.
public class MessagePageFragment extends Fragment {

  //The chat ID, for testing only - Hung Vu.
  // TODO Remove hard-coded id
  private static final int HARD_CODED_CHAT_ID = 1;

  // Implement send message.
  private ChatViewModel mChatModel;
  private UserInfoViewModel mUserModel;
  private ChatSendViewModel mSendModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    // Chat room.
    mSendModel = provider.get(ChatSendViewModel.class);
    mUserModel = provider.get(UserInfoViewModel.class);
    mChatModel = provider.get(ChatViewModel.class);
    mChatModel.getFirstMessages(HARD_CODED_CHAT_ID, mUserModel.getJwt());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_message_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());

    // Send button was clicked. Send the message via the SendViewModel
    binding.buttonSend.setOnClickListener(button -> {
      // TODO Make send button functional - Done, Hung Vu
      mSendModel.sendMessage(HARD_CODED_CHAT_ID,
              mUserModel.getJwt(),
              binding.editMessageBox.getText().toString());
    });

    //when we get the response back from the server, clear the edittext
    mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
            binding.editMessageBox.setText(""));

    // Shows the internal Swiper view progress bar until messages load
    // TODO Change this to true when there is actual loading - Done, Hung Vu
    binding.swipeContainer.setRefreshing(true);

    final RecyclerView rv = binding.recyclerMessages;
    // TODO make this use a UserInfo View Model - Done, Hung Vu
    // Working on...
    // Sets the Adapter to hold a reference to the list for this group ID that the ViewModel holds
    rv.setAdapter(new ChatRecyclerViewAdapter(
            mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID),
            mUserModel.getEmail()
    ));

    // TODO add on refresh listener and message observer - Done, Hung Vu
    //When the user scrolls to the top of the RV, the swiper list will "refresh"
    //The user is out of messages, go out to the service and get more
    binding.swipeContainer.setOnRefreshListener(() -> {
      mChatModel.getNextMessages(HARD_CODED_CHAT_ID, mUserModel.getJwt());
    });

    mChatModel.addMessageObserver(HARD_CODED_CHAT_ID, getViewLifecycleOwner(),
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
  }

  @Override
  public void onResume() {
    // TODO When a user pause activity (app to background), user will receive msg over notification.
    //  However, since there is no view, that msg is not drawn on anything so when we return to our app
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

  // Checkstyle: Done - Hung Vu
}