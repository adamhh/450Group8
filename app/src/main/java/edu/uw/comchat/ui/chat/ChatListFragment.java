package edu.uw.comchat.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatListBinding;

/**
 * A fragment that shows the list of chats a user has with other.
 *
 *  * @author Jerry Springer
 *  * @version 3 November 2020
 */
public class ChatListFragment extends Fragment {

  FragmentChatListBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_chat_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding = FragmentChatListBinding.bind(view);
    binding.floatingActionButtonChatListMessage.setOnClickListener(button -> {
      Navigation.findNavController(getView()).navigate(
              ChatListFragmentDirections.actionNavigationChatToCreateFragment());
    });

    // Sets the recycler view adapter for the list (re-use of elements when scrolling)
    binding.listRootChat.setAdapter(
            new ChatRecyclerViewAdapter(ChatGenerator.getChatList()));

    // Adds a divider in the list
    binding.listRootChat.addItemDecoration(
            new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_create, menu);
  }
}