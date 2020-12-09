package edu.uw.comchat.ui.auth.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.auth0.android.jwt.JWT;

import edu.uw.comchat.databinding.FragmentStartBinding;
import edu.uw.comchat.util.StorageUtil;

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

    @Override
    public void onStart() {
        super.onStart();

        StorageUtil util = new StorageUtil(getContext());
        String token = util.loadCredentials();

        if (!token.equals("")) {
            JWT jwt = new JWT(token);

            if (!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();

                Navigation.findNavController(getView())
                        .navigate(StartFragmentDirections
                                .actionStartFragmentToMainActivity(email, token));
                getActivity().finish();
            }
        }
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