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
import edu.uw.comchat.ui.chat.ChatMessageGenerator;
import edu.uw.comchat.ui.chat.chatroom.ChatRecyclerViewAdapter;

/**
 * A fragment that shows the list of messages in a group.
 * <p></p>
 * * @author Jerry Springer
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
      // TODO Make send button functional
      // Working on...
      mSendModel.sendMessage(HARD_CODED_CHAT_ID,
              mUserModel.getJwt(),
              binding.editMessageBox.getText().toString());
    });

    //when we get the response back from the server, clear the edittext
    mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
            binding.editMessageBox.setText(""));

    // Shows the internal Swiper view progress bar until messages load
    // TODO Change this to true when there is actual loading
    binding.swipeContainer.setRefreshing(false);

    final RecyclerView rv = binding.recyclerMessages;
    // TODO make this use a UserInfo View Model
    // Working on...
    // Sets the Adapter to hold a reference to the list for this group ID that the ViewModel holds
    rv.setAdapter(new ChatRecyclerViewAdapter(
            // Dummy chat generator
//            ChatMessageGenerator.getChatList(),
            mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID),
            mUserModel.getEmail()
    ));

    // TODO add on refresh listener and message observer
  }

  // Checkstyle: Done - Hung Vu
}