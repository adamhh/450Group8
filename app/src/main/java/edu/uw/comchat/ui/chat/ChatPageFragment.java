package edu.uw.comchat.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatBinding;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.util.ColorUtil;
import edu.uw.comchat.util.StorageUtil;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A fragment that shows the list of chats a user has with other.
 * <p></p>
 * * @author Jerry Springer (UI), Hung Vu (connect to web service).
 * * @version 3 November 2020
 */
// Ignore checkstyle member name error. Checkstyle done, post Sprint 3, Hung Vu.
public class ChatPageFragment extends Fragment {

  private FragmentChatBinding binding;
  private ChatPageViewModel mChatPageViewModel;
  private UserInfoViewModel mUserViewModel;
  private View mChatPageView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mChatPageViewModel = new ViewModelProvider(getActivity()).get(ChatPageViewModel.class);
    mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(false);
    return inflater.inflate(R.layout.fragment_chat, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mChatPageView = view;
    // Get group id upon creation - Hung Vu
    mChatPageViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

    binding = FragmentChatBinding.bind(mChatPageView);
    binding.floatingActionButtonChatMessage.setOnClickListener(
            button -> Navigation.findNavController(getView()).navigate(
              ChatPageFragmentDirections.actionNavigationChatToCreateFragment()
            ));

    TimerTask getGroupTask = new TimerTask() {
      @Override
      public void run() {
        mChatPageViewModel.getAllUserCommunicationGroup(
                mUserViewModel.getEmail(),
                mUserViewModel.getJwt());
      }
    };
    Timer timer = new Timer("Update group page per 0.5 sec");
    timer.scheduleAtFixedRate(getGroupTask, 500L, 500L);
    mChatPageViewModel.getAllUserCommunicationGroup(
            mUserViewModel.getEmail(), mUserViewModel.getJwt());
  }

  /**
   * When receive response from server, create chat rooms along with their respective roomId.
   *
   * @param chatIdList a list of ChatRoomInfo
   */
  private void observeResponse(List<ChatRoomInfo> chatIdList) {
    binding = FragmentChatBinding.bind(mChatPageView);

    StorageUtil storageUtil = new StorageUtil(getContext());
    ColorUtil colorUtil = new ColorUtil(getActivity(), storageUtil.loadTheme());

    binding.listRootChat.setAdapter(new GroupRecyclerViewAdapter(chatIdList, colorUtil));

  }
}