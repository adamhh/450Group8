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

/**
 * @author Hung Vu
 */
public class PasswordRecoveryFragment extends Fragment {

  private FragmentPasswordRecoveryBinding mBinding;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mBinding = FragmentPasswordRecoveryBinding.inflate(inflater);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.buttonPasswordRecoverySendPin.setOnClickListener(button -> handleSendPinButton());
  }

  private void handleSendPinButton() {
    Navigation.findNavController(getView()).navigate(
            PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToPasswordRecoveryUpdateFragment()
    );
  }
}