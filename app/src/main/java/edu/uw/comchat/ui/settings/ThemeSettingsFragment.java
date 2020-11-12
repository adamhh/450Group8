package edu.uw.comchat.ui.settings;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.comchat.MainActivity;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentThemeSettingsBinding;
import edu.uw.comchat.Theme;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThemeSettingsFragment extends Fragment {

    private final String mRedTheme = "red";
    private final String mDefaultTheme = "default";
    private final String mBlueGreyTheme = "grey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setOnClickListeners(view);
    }

    /**
     *
     * @param view
     */
    private void setOnClickListeners(View view) {
        //local access to binding for now.  If I need to use elsewhere will make class variable
        FragmentThemeSettingsBinding binding = FragmentThemeSettingsBinding.bind(getView());

        //necessary?
        SettingsViewModel model = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        //set click listeners
        binding.radioButtonThemeDefault.setOnClickListener(this::defaultTheme);

        binding.radioButtonThemeGrey.setOnClickListener(this::greyTheme);

        binding.radioButtonThemeRed.setOnClickListener(this::redTheme);
    }

    /**
     *
     * @param view
     */
    private void defaultTheme(View view) {
        Theme.setTheme(mDefaultTheme);
        getActivity().recreate();
        TaskStackBuilder.create(getActivity())
                .addNextIntent(new Intent(getActivity(), MainActivity.class))
                .addNextIntent(getActivity().getIntent())
                .startActivities();
    }

    /**
     *
     * @param view
     */
    private void greyTheme(View view) {
        Theme.setTheme(mBlueGreyTheme);
        getActivity().recreate();
        TaskStackBuilder.create(getActivity())
                .addNextIntent(new Intent(getActivity(), MainActivity.class))
                .addNextIntent(getActivity().getIntent())
                .startActivities();
    }

    /**
     *
     * @param view
     */
    private void redTheme(View view) {
        Theme.setTheme(mRedTheme);
        getActivity().recreate();
        TaskStackBuilder.create(getActivity())
                .addNextIntent(new Intent(getActivity(), MainActivity.class))
                .addNextIntent(getActivity().getIntent())
                .startActivities();
    }



}