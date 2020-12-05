package edu.uw.comchat.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentCreateBinding;
import edu.uw.comchat.databinding.FragmentMessageListBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.util.ModifyChatRoom;

/**
 * A fragment to create new groups of people for chats.
 * <p></p>
 * * @author Jerry Springer (fragment flow), Hung Vu (backend)
 * * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class CreateFragment extends Fragment {

  private UserInfoViewModel mUserModel;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    mUserModel = provider.get(UserInfoViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_create, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentCreateBinding binding = FragmentCreateBinding.bind(getView());
    // Inidcate whether a DM room is created or not.
    String[] groupMessage = {"true"};
    binding.checkBoxCreateDm.setOnClickListener(checkBox -> {
      if(binding.checkBoxCreateDm.isChecked()){
        binding.editTextChatWith.setVisibility(View.VISIBLE);
        binding.editTextTargetUser.setVisibility(View.VISIBLE);
        groupMessage[0] = "false";
      } else {
        binding.editTextChatWith.setVisibility(View.INVISIBLE);
        binding.editTextTargetUser.setVisibility(View.INVISIBLE);
        binding.editTextTargetUser.setText("");
        groupMessage[0] = "true";
      }
    });
    binding.buttonCreateAccept.setOnClickListener(button -> {
      if (binding.editTextCreate.getText().toString().isEmpty()){
        binding.editTextCreate.setError("Chat group name can't be empty.");
      }
      ArrayList<String> infoToCreateRoom = new ArrayList<>();
      infoToCreateRoom.add(binding.editTextCreate.getText().toString());
      infoToCreateRoom.add(mUserModel.getEmail());
      infoToCreateRoom.add(mUserModel.getJwt());
      infoToCreateRoom.add(groupMessage[0]);
      ModifyChatRoom.createRoom().accept(infoToCreateRoom, this);
    });
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_create, menu);
  }

}