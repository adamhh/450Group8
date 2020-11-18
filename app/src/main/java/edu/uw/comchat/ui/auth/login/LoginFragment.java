package edu.uw.comchat.ui.auth.login;

import static edu.uw.comchat.util.EmailValidator.EmailValidationResult;
import static edu.uw.comchat.util.EmailValidator.checkEmail;
import static edu.uw.comchat.util.PasswordValidator.PasswordValidationResult;
import static edu.uw.comchat.util.PasswordValidator.checkPwdContainsUppercase;
import static edu.uw.comchat.util.PasswordValidator.checkPwdLength;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.comchat.databinding.FragmentLoginBinding;
import edu.uw.comchat.util.EmailValidator;
import edu.uw.comchat.util.PasswordValidator;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This is a fragment for login page.
 *
 * @author Hung Vu, Jerry
 * @version 17 November 2020
 */
// Ignore checkstyle member name error.
public class LoginFragment extends Fragment {
  /**
   * An error thrown when a user type in invalid account login information.
   */
  private static final String INVALID_ERROR = "Invalid username and/or password";
  /**
   * View binding of login page.
   */
  private FragmentLoginBinding mBinding;
  /**
   * View model of login page.
   */
  private LoginViewModel mLoginModel;

  // TODO Apply combinator design pattern and refactor the code in next sprint - Hung Vu.
  /**
   * A function to check whether password meets length requirement.
   */
  private PasswordValidator checkPasswordLength = checkPwdLength();

  /**
   * A function to check whether password contains at least 1 uppercase letter.
   */
  private PasswordValidator checkPasswordContainUpperCase = checkPwdContainsUppercase();

  /**
   * A function to validates email address.
   */
  private EmailValidator checkValidEmail = checkEmail();

  /**
   * Empty constructor (required).
   */
  public LoginFragment(){

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLoginModel = new ViewModelProvider(getActivity())
            .get(LoginViewModel.class);

  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mBinding = FragmentLoginBinding.inflate(inflater);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.buttonLoginSignIn.setOnClickListener(button -> handleSignInButton());
    mBinding.buttonLoginPasswordRecovery.setOnClickListener(button -> handlePasswordRecoveryButton());
    mLoginModel.addResponseObserver(
            getViewLifecycleOwner(),
            this::observeResponse);
  }

  private void handlePasswordRecoveryButton() {
    Navigation.findNavController(getView()).navigate(
            LoginFragmentDirections.actionLoginFragmentToPasswordRecoveryFragment()
    );
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mBinding = null;
  }

  /**
   * Provide behavior when Login button is pressed.
   */
  private void handleSignInButton() {
    // Intentionally give the same error message, unlike register page
    // to avoid user with malicious intention guessing the account login information.
    String emailString = mBinding.editTextLoginEmail.getText().toString();
    String passwordString = mBinding.editTextLoginPassword.getText().toString();
    if (EmailValidationResult.EMAIL_INVALID.equals(
            checkValidEmail
                    .apply(emailString)
                    .get())
            || PasswordValidationResult.PWD_INVALID_LENGTH.equals(
                    checkPwdLength().apply(passwordString).get())
            || PasswordValidationResult.PWD_MISSING_UPPER.equals(
                    checkPwdContainsUppercase().apply(passwordString).get())) {
      mBinding.editTextLoginEmail.setError(INVALID_ERROR);
      mBinding.editTextLoginPassword.setError(INVALID_ERROR);

    } else {
      this.verifyAuthWithServer();
    }
  }

  /**
   * Verify user name and password (server side).
   */
  private void verifyAuthWithServer() {
    mLoginModel.connect(
            mBinding.editTextLoginEmail.getText().toString().toUpperCase(),
            mBinding.editTextLoginPassword.getText().toString());
    //This is an Asynchronous call. No statements after should rely on the
    //result of connect().

  }

  /**
   * An observer on the HTTP Response from the web server. This observer should be
   * attached to SignInViewModel.
   *
   * @param response the Response from the server
   */
  private void observeResponse(final JSONObject response) {
    if (response.length() > 0) {
      if (response.has("code")) {
        try {
          mBinding.editTextLoginEmail.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
          mBinding.editTextLoginPassword.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
        try {
          navigateToMainActivity(
                  mBinding.editTextLoginEmail.getText().toString(),
                  response.getString("token")
          );
        //          Log.i("jwt",response.getString("token"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      }
    } else {
      Log.d("JSON Response", "No Response");
    }

  }

  /**
   * Navigate to home page.
   */
  public void navigateToMainActivity(String email, String jwt) {
    Navigation.findNavController(getView())
            .navigate(LoginFragmentDirections
                    .actionLoginFragmentToMainActivity(email, jwt));
    getActivity().finish();
  }
  // Checkstyle: Done - Hung Vu
}