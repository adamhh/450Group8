package edu.uw.comchat.ui.settings;

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
import edu.uw.comchat.databinding.FragmentSettingsBinding;

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

    //Local access to the ViewBinding object. No need to create as Instance Var as it is only
    //used here.
    FragmentSettingsBinding binding = FragmentSettingsBinding.bind(getView());
    //Note argument sent to the ViewModelProvider constructor. It is the Activity that
    //holds this fragment.
    SettingsViewModel model = new ViewModelProvider(getActivity())
            .get(SettingsViewModel.class);

    //set on click listener to theme settings
    binding.themeSettingsButton.setOnClickListener(button ->
            Navigation.findNavController(getView()).navigate(
                    SettingsFragmentDirections
                      .actionNavigationSettingsToThemeSettingsFragment()
            ));
  }
}