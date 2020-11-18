package edu.uw.comchat.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatBinding;
import edu.uw.comchat.ui.chat.generatorfortesting.GroupGenerator;


/**
 * A fragment that shows the list of chats a user has with other.
 * <p></p>
 * * @author Jerry Springer
 * * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ChatPageFragment extends Fragment {

  FragmentChatBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_chat, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding = FragmentChatBinding.bind(view);
    binding.floatingActionButtonChatMessage.setOnClickListener(button -> {
      Navigation.findNavController(getView()).navigate(
              ChatPageFragmentDirections.actionNavigationChatToCreateFragment());
    });

    // Sets the recycler view adapter for the list (re-use of elements when scrolling)
    binding.listRootChat.setAdapter(
            new GroupRecyclerViewAdapter(GroupGenerator.getGroupList()));

    // Adds a divider in the list
    binding.listRootChat.addItemDecoration(
            new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_create, menu);
  }

  // Checkstyle: Done - Hung Vu
}