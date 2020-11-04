package edu.uw.comchat.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.uw.comchat.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

  private FragmentLoginBinding mBinding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mBinding = FragmentLoginBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.buttonLoginSignIn.setOnClickListener(button -> handleSignInButton());
    mBinding.buttonLoginRegister.setOnClickListener(button -> handleRegisterButton());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mBinding = null;
  }

  /**
   *
   */
  private void handleSignInButton() {
    // TODO add Verification
    Navigation.findNavController(getView()).navigate(
            LoginFragmentDirections.actionLoginFragmentToMainActivity()
    );

    // Exit the activity so users cannot back navigate to login
    getActivity().finish();
  }

  /**
   * Provide behavior when Register button is pressed.
   */
  private void handleRegisterButton() {
    Navigation.findNavController(getView()).navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
    );
  }
}