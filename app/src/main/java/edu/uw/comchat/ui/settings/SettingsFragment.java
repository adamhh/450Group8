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
import edu.uw.comchat.util.StorageUtil;

/**
 * The fragment that shows user settings.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class SettingsFragment extends Fragment {

  //required empty public constructor
  public SettingsFragment() {

  };

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return inflater.inflate(R.layout.fragment_settings, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    SettingsViewModel mModel = new ViewModelProvider(getActivity())
            .get(SettingsViewModel.class);

    StorageUtil storageUtil = new StorageUtil(getContext());
    edu.uw.comchat.databinding.FragmentSettingsBinding mBinding = FragmentSettingsBinding.bind(getView());
    mBinding.darkThemeToggleId.setChecked(storageUtil.loadDarkTheme());
    mBinding.darkThemeToggleId.setOnClickListener(onClick -> {
      ((MainActivity)getActivity()).toggleDarkMode();
    });

  }
}