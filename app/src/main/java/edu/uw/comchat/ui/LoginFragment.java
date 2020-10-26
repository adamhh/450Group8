package edu.uw.comchat.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.

 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonSignIn.setOnClickListener(button -> handleSignInButton());
        mBinding.buttonRegister.setOnClickListener(button -> handleRegisterButton());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    /**
     * Provide behavior when Sign In button is pressed.
     */
    private void handleSignInButton(){

    }

    /**
     * Provide behavior when Register button is pressed.
     */
    private void handleRegisterButton(){
        Navigation.findNavController(getView()).navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        );
    }
}