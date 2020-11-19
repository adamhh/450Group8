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
import edu.uw.comchat.databinding.FragmentPasswordRecoveryBinding;
import edu.uw.comchat.util.EmailValidator;

import static edu.uw.comchat.util.EmailValidator.checkEmail;

/**
 * This is a page where users type in the email of account that they want to recover.
 * @author Hung Vu
 */
public class PasswordRecoveryFragment extends Fragment {

  private FragmentPasswordRecoveryBinding mBinding;
  private PasswordRecoveryViewModel mRecoveryViewModel;
  /**
   * A function to validates email address.
   */
  private static final EmailValidator CHECK_VALID_EMAIL = checkEmail();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRecoveryViewModel = new ViewModelProvider(this).get(PasswordRecoveryViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mBinding = FragmentPasswordRecoveryBinding.inflate(inflater);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecoveryViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    mBinding.buttonPasswordRecoverySendPin.setOnClickListener(button -> handleSendPinButton());
  }

  private void handleSendPinButton() {
    String email = mBinding.editTextPasswordRecovery.getText().toString();
    if (EmailValidator.EmailValidationResult.EMAIL_INVALID.equals(
            CHECK_VALID_EMAIL.apply(email).get()
    )) {
      mBinding.editTextPasswordRecovery.setError("Invalid email");
      return;
    } else {
      mRecoveryViewModel.connect(email);
    }
  }

  private void observeResponse(final JSONObject response) {
    //    Log.i("JSON body", response.toString());
    if (response.length() > 0) {
      if (response.has("code")) {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(getResources().getString(R.string.text_recover_pw_status))
                .setMessage(getResources().getString(R.string.text_recover_pw_fail_email_check))
                .show();
        try {
          mBinding.editTextPasswordRecovery.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(getResources().getString(R.string.text_recover_pw_status))
                .setMessage(getResources().getString(R.string.text_recover_pw_success_email_check))
                .show();
        navigateToEnterPinPage();
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }

  private void navigateToEnterPinPage() {
    Navigation.findNavController(getView()).navigate(
            PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToPasswordRecoveryUpdateFragment(
                    mBinding.editTextPasswordRecovery.getText().toString())
    );
  }
}