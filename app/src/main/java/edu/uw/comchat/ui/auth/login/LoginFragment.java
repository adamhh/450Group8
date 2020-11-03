package edu.uw.comchat.ui.auth.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.comchat.databinding.FragmentLoginBinding;
import edu.uw.comchat.util.EmailValidator;
import edu.uw.comchat.util.PasswordValidator;

import static edu.uw.comchat.util.EmailValidator.*;
import static edu.uw.comchat.util.PasswordValidator.*;



/**
 * This is a fragment for login page.
 * @author Hung Vu, Jerry
 */
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

  // TODO Apply combinator design pattern and refactor the code in next sprint.
  /**
   * A function to check whether password meets length requirement.
   */
  private PasswordValidator checkPasswordLength = checkPwdLength();

  /**
   * A function to check whether password contains at least 1 uppercase letter.
   */
  private PasswordValidator checkPasswordContainUpperCase = checkPwdContainUppercase();

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
    mBinding.buttonSignIn.setOnClickListener(button -> handleSignInButton());
    mBinding.buttonRegister.setOnClickListener(button -> handleRegisterButton());
    mLoginModel.addResponseObserver(
            getViewLifecycleOwner(),
            this::observeResponse);
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
    // Is checking on login page a need?
    // It is somewhat illogical since the check will let
    // user with malicious intention have clue to search for correct combination.

    // Note, the code below is only partially implemented.
//    boolean isValid = true;
//    String emailString = mBinding.editTextLogin.getText().toString();
//    String passwordString = mBinding.editTextPassword.getText().toString();
//    if (EmailValidationResult.EMAIL_INVALID.equals(
//            checkValidEmail
//                    .apply(emailString)
//                    .get())) {
//      mBinding.editTextLogin.setError(INVALID_ERROR);
//      mBinding.editTextPassword.setError(INVALID_ERROR);
//      isValid = false;
//    }

    this.verifyAuthWithServer();
  }

  /**
   * Provide behavior when Register button is pressed.
   */
  private void handleRegisterButton() {
    Navigation.findNavController(getView()).navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
    );
  }

  /**
   * Verify user name and password (server side).
   */
  private void verifyAuthWithServer() {
    mLoginModel.connect(
            mBinding.editTextLogin.getText().toString(),
            mBinding.editTextPassword.getText().toString());
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
          mBinding.editTextLogin.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
          mBinding.editTextPassword.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
        try {
          navigateToMainActivity(
                  mBinding.editTextLogin.getText().toString(),
                  response.getString("token")
          );
//          Log.i("jwt",response.getString("token"));
        }
        catch (JSONException e) {
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
  public void navigateToMainActivity(String email, String jwt){
    Navigation.findNavController(getView())
            .navigate(LoginFragmentDirections
                    .actionLoginFragmentToMainActivity(email, jwt));
    getActivity().finish();
  }
}