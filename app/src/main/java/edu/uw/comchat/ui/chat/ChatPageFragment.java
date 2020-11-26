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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A fragment that shows the list of chats a user has with other.
 * <p></p>
 * * @author Jerry Springer (UI), Hung Vu (connect to web service).
 * * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ChatPageFragment extends Fragment {

  FragmentChatBinding binding;
  ChatPageViewModel mChatPageViewModel;
  UserInfoViewModel mUserViewModel;
  View mChatPageView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mChatPageViewModel = new ViewModelProvider(getActivity()).get(ChatPageViewModel.class);
    mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_chat, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mChatPageView = view;
    // Get group id upon creation - Hung Vu
    mChatPageViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

    // TODO I attempt to update chat group list per 0.5 sec to update UI in real time.
    //  For example, when user 1 delete 2 from chat group 3, screen of 2 will update.
    //  But this doesn't work - Hung Vu.
    TimerTask getGroupTask = new TimerTask() {
      @Override
      public void run() {
        mChatPageViewModel.getAllUserCommunicationGroup(
                mUserViewModel.getEmail(),
                mUserViewModel.getJwt());
      }
    };
    Timer timer = new Timer("Update group page per 0.5 sec");
    timer.schedule(getGroupTask, 500L);
    //    mChatPageViewModel.getAllUserCommunicationGroup(mUserViewModel.getEmail(),
    //    mUserViewModel.getJwt());
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getActivity().getMenuInflater().inflate(R.menu.toolbar_create, menu);
  }

  /**
   * When receive response from server, create chat rooms along with their respective groupId.
   *
   * @param chatIdList a list of ChatGroupInfo
   */
  private void observeResponse(List<ChatGroupInfo> chatIdList) {
    binding = FragmentChatBinding.bind(mChatPageView);
    binding.floatingActionButtonChatMessage.setOnClickListener(button -> {
      Navigation.findNavController(getView()).navigate(
              ChatPageFragmentDirections.actionNavigationChatToCreateFragment());
    });

    // Sets the recycler view adapter for the list (re-use of elements when scrolling)
    binding.listRootChat.setAdapter(
            new GroupRecyclerViewAdapter(chatIdList));

    // Adds a divider in the list
    binding.listRootChat.addItemDecoration(
            new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
  }
}