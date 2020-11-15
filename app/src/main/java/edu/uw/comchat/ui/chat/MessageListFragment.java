package edu.uw.comchat.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatBinding;
import edu.uw.comchat.databinding.FragmentMessageListBinding;

/**
 * A fragment that shows the list of messages in a group.
 * <p></p>
 * * @author Jerry Springer
 * * @version 11 November 2020
 */
// Ignore checkstyle member name error.
public class MessageListFragment extends Fragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    });

    // Shows the internal Swiper view progress bar until messages load
    // TODO Change this to true when there is actual loading
    binding.swipeContainer.setRefreshing(false);

    final RecyclerView rv = binding.recyclerMessages;
    // TODO make this use a UserInfo View Model
    // Sets the Adapter to hold a reference to the list for this group ID that the ViewModel holds
    rv.setAdapter(new ChatRecyclerViewAdapter(
            ChatMessageGenerator.getChatList(),
            "email@email.com"
    ));

    // TODO add on refresh listener and message observer
  }

  // Checkstyle: Done - Hung Vu
}