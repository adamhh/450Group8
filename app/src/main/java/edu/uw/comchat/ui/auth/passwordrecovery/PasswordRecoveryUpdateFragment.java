package edu.uw.comchat.ui.auth.passwordrecovery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentPasswordRecoveryUpdateBinding;
import edu.uw.comchat.util.PasswordValidator;

import static edu.uw.comchat.util.PasswordValidator.checkPwdContainsUppercase;
import static edu.uw.comchat.util.PasswordValidator.checkPwdLength;

/**
 * @author Hung Vu
 */
public class PasswordRecoveryUpdateFragment extends Fragment {
  private FragmentPasswordRecoveryUpdateBinding mBinding;
  private PasswordRecoveryUpdateViewModel mRecoveryUpdateViewModel;
  private String mEmail;

  /**
   * A function to check whether password meets length requirement.
   */
  private static final PasswordValidator CHECK_PWD_LENGTH = checkPwdLength();

  /**
   * A function to check whether password contains at least 1 uppercase letter.
   */
  private static final PasswordValidator CHECK_CONTAIN_UPPERCASE = checkPwdContainsUppercase();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRecoveryUpdateViewModel = new ViewModelProvider(this).get(PasswordRecoveryUpdateViewModel.class);
    mEmail = PasswordRecoveryUpdateFragmentArgs.fromBundle(getArguments()).getEmail();
  }

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
    mRecoveryUpdateViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

  }



  private void handleAcceptButton() {
    boolean isValid = true;
    String pinString = mBinding.editTextPasswordRecoveryPin.getText().toString();
    String passwordString = mBinding.editTextPasswordRecoveryNewPassword.getText().toString();
    if (PasswordValidator.PasswordValidationResult.PWD_INVALID_LENGTH.equals(
            CHECK_PWD_LENGTH
                    .apply(passwordString)
                    .get())) {
      mBinding.editTextPasswordRecoveryNewPassword.setError("Invalid password length");
      isValid = false;
    } else if (PasswordValidator.PasswordValidationResult.PWD_MISSING_UPPER.equals(
            CHECK_CONTAIN_UPPERCASE
                    .apply(passwordString)
                    .get())) {
      mBinding.editTextPasswordRecoveryNewPassword.setError("Need at least 1 uppercase letter");
      isValid = false;
    }

    String passwordRetypeString = mBinding.editTextPasswordRecoveryPasswordRetype.getText().toString();
    if (!passwordRetypeString.equals(passwordString)) {
      mBinding.editTextPasswordRecoveryPasswordRetype.setError("Password mismatches");
      isValid = false;
    }
    if (isValid) {
      mRecoveryUpdateViewModel.connect(mEmail, pinString, passwordString);
    }
  }
  
  private void observeResponse(JSONObject response) {
    //    Log.i("JSON body", response.toString());
    if (response.length() > 0) {
      if (response.has("code")) {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(getResources().getString(R.string.text_recover_pw_status))
                .setMessage(getResources().getString(R.string.text_recover_pw_fail_process))
                .show();
        try {
          mBinding.editTextPasswordRecoveryPin.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(getResources().getString(R.string.text_recover_pw_status))
                .setMessage(getResources().getString(R.string.text_recover_pw_success_process))
                .show();
        navigateToLoginPage();
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }
  private void navigateToLoginPage() {
    Navigation.findNavController(getView()).navigate(
            PasswordRecoveryUpdateFragmentDirections.actionPasswordRecoveryUpdateFragmentToLoginFragment()
    );
  }

}