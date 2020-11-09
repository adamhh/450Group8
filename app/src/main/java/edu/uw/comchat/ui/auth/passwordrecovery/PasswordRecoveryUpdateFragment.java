package edu.uw.comchat.ui.auth.passwordrecovery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentPasswordRecoveryBinding;
import edu.uw.comchat.databinding.FragmentPasswordRecoveryUpdateBinding;

/**
 * @author Hung Vu
 */
public class PasswordRecoveryUpdateFragment extends Fragment {
  private FragmentPasswordRecoveryUpdateBinding mBinding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mBinding = FragmentPasswordRecoveryUpdateBinding.inflate(inflater);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.buttonPasswordRecoveryUpdateAccept.setOnClickListener(button -> handleAcceptButton());

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  private void handleAcceptButton() {
    Navigation.findNavController(getView()).navigate(
            PasswordRecoveryUpdateFragmentDirections.actionPasswordRecoveryUpdateFragmentToLoginFragment()
    );
  }

}