package edu.uw.comchat.ui.auth.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.uw.comchat.databinding.FragmentStartBinding;

/**
 * A fragment for the Start page
 *
 * @author Jerry Springer
 * @version 17 November 2020
 */
public class StartFragment extends Fragment {

    private FragmentStartBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentStartBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonStartLogin.setOnClickListener(button -> handleLoginButton());
        mBinding.buttonStartRegister.setOnClickListener(button -> handleRegisterButton());
    }

    /**
     * Provides behaviour when Login button is pressed.
     */
    private void handleLoginButton() {
        Navigation.findNavController(getView()).navigate(
                StartFragmentDirections.actionStartFragmentToLoginFragment()
        );
    }

    /**
     * Provide behavior when Register button is pressed.
     */
    private void handleRegisterButton() {
        Navigation.findNavController(getView()).navigate(
                StartFragmentDirections.actionStartFragmentToRegisterFragment()
        );
    }
}