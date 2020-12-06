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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentCreateBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.ui.connection.Connection;
import edu.uw.comchat.ui.connection.ConnectionListViewModel;
import edu.uw.comchat.util.ModifyChatRoom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



/**
 * A fragment to create new groups of people for chats.
 * <p></p>
 * * @author Jerry Springer (fragment flow), Hung Vu (backend, functionality)
 * * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class CreateFragment extends Fragment {

  private UserInfoViewModel mUserModel;
  private ConnectionListViewModel mConnectionListViewModel;
  private FragmentCreateBinding mBinding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    mUserModel = provider.get(UserInfoViewModel.class);
    mConnectionListViewModel = provider.get(ConnectionListViewModel.class);
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
    mBinding = FragmentCreateBinding.bind(getView());

    mConnectionListViewModel.addConnectionListObserver(getViewLifecycleOwner(), response -> {
      // For now, the response doesn't cause any changes to the UI - Hung Vu.
    });
    mConnectionListViewModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());

    // Indicate whether a DM room is created or not.
    String[] wantToCreateGroup = {"true"};

    mBinding.checkBoxCreateDm.setOnClickListener(checkBox -> {
      if (mBinding.checkBoxCreateDm.isChecked()) {
        mBinding.editTextChatWith.setVisibility(View.VISIBLE);
        mBinding.editTextTargetUser.setVisibility(View.VISIBLE);
        wantToCreateGroup[0] = "false";
        handleAddTargetUser(wantToCreateGroup);
      } else {
        mBinding.editTextChatWith.setVisibility(View.INVISIBLE);
        mBinding.editTextTargetUser.setVisibility(View.INVISIBLE);
        mBinding.editTextTargetUser.setText("");
        wantToCreateGroup[0] = "true";
      }

    });
    mBinding.buttonCreateAccept.setOnClickListener(button -> {
      if (mBinding.editTextCreate.getText().toString().isEmpty()) {
        mBinding.editTextCreate.setError("Chat group name can't be empty.");
      }
      ArrayList<String> infoToCreateRoom = new ArrayList<>();
      infoToCreateRoom.add(mBinding.editTextCreate.getText().toString());
      infoToCreateRoom.add(mUserModel.getEmail());
      infoToCreateRoom.add(mUserModel.getJwt());
      infoToCreateRoom.add(wantToCreateGroup[0]);
      infoToCreateRoom.add(mBinding.editTextTargetUser.getText().toString());
      ModifyChatRoom.createRoom().accept(infoToCreateRoom, this);
    });


  }

  /**
   * Create a dialog to select DM user and update "wantToCreateGroup" signal accordingly;
   * @param wantToCreateGroup index 0 store true if a user want to create group chat,
   *                          false for a DM room.
   */
  private void handleAddTargetUser(String[] wantToCreateGroup) {
    List<Connection> friendList = mConnectionListViewModel.getConnectionList();
    List<String> emailList = friendList.stream()
            .map(connection -> connection.getEmail())
            .collect(Collectors.toList());
    // Default choice at index 0
    emailList.add(0, "None");
    CharSequence[] multiItems = emailList.toArray(new CharSequence[emailList.size()]);
    mConnectionListViewModel.getAllConnections(mUserModel.getEmail(), mUserModel.getJwt());

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
                        mBinding.editTextTargetUser.setText(String.valueOf(multiItems[choice[0]]));
                      } else {
                        mBinding.editTextChatWith.setVisibility(View.INVISIBLE);
                        mBinding.editTextTargetUser.setVisibility(View.INVISIBLE);
                        mBinding.checkBoxCreateDm.setChecked(false);
                        mBinding.editTextTargetUser.setText("");
                        wantToCreateGroup[0] = "true";
                      }
                    }
            )
            .setNegativeButton(getResources().getString(R.string.item_menu_chat_list_decline),
                    (dialog, which) -> {
                      mBinding.editTextChatWith.setVisibility(View.INVISIBLE);
                      mBinding.editTextTargetUser.setVisibility(View.INVISIBLE);
                      mBinding.checkBoxCreateDm.setChecked(false);
                      mBinding.editTextTargetUser.setText("");
                      wantToCreateGroup[0] = "true";
                    })
            .show();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_create, menu);
  }

}