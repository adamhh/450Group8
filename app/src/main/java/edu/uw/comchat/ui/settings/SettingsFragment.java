package edu.uw.comchat.ui.settings;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.uw.comchat.MainActivity;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentSettingsBinding;

/**
 * The fragment that shows user settings.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class SettingsFragment extends Fragment {
  private FragmentSettingsBinding mBinding;
  private SettingsViewModel mModel;

  //required empty public constructor
  public SettingsFragment() {

  }

  ;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return inflater.inflate(R.layout.fragment_settings, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mModel = new ViewModelProvider(getActivity())
            .get(SettingsViewModel.class);
    //Will save state of toggle once we used sharedpreferences
    mBinding = FragmentSettingsBinding.bind(getView());
    mBinding.darkThemeToggleId.setOnClickListener(this::toggleDark);

  }

  private void toggleDark(View view) {
    ((MainActivity) getActivity()).toggleDarkMode();
  }

  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}