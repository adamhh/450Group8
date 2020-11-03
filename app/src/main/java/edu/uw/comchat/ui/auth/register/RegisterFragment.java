package edu.uw.comchat.ui.auth.register;

import static edu.uw.comchat.util.PasswordValidator.*;
import static edu.uw.comchat.util.EmailValidator.*;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.comchat.databinding.FragmentRegisterBinding;
import edu.uw.comchat.util.EmailValidator;
import edu.uw.comchat.util.PasswordValidator;


/**
 * This is a fragment for register page.
 * @author Hung Vu, Jerry
 */
public class RegisterFragment extends Fragment {
  /**
   * Empty field error.
   */
  private static final String EMPTY_FIELD_ERROR = "Empty field";
  /**
   * View binding of register page.
   */
  private FragmentRegisterBinding mBinding;

  /**
   * View model of register page.
   */
  private RegisterViewModel mRegisterModel;

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
  public RegisterFragment(){

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRegisterModel = new ViewModelProvider(getActivity())
            .get(RegisterViewModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    mBinding = FragmentRegisterBinding.inflate(inflater);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.buttonAccept.setOnClickListener(button -> handleAcceptButton());
    mRegisterModel.addResponseObserver(
            getViewLifecycleOwner(), this::observeResponse);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mBinding = null;
  }

  /**
   * Provide behavior when the Accept register button is pressed.
   */
  private void handleAcceptButton() {
    // TODO Refactor the code after combinator design is applied.
    boolean isValid = true;
    EditText firstName = mBinding.editTextFirstName;
    EditText lastName = mBinding. editTextLastName;
    EditText nickname = mBinding.editTextNickname;
    if (isLengthZero(firstName)) {
      firstName.setError(EMPTY_FIELD_ERROR);
      isValid = false;
    }
    if (isLengthZero(lastName)) {
      lastName.setError(EMPTY_FIELD_ERROR);
      isValid = false;
    }
    if (isLengthZero(nickname)) {
      nickname.setError(EMPTY_FIELD_ERROR);
      isValid = false;
    }

    String emailString = mBinding.editTextEmail.getText().toString();
    String passwordString = mBinding.editTextPassword.getText().toString();
    if (EmailValidationResult.EMAIL_INVALID.equals(
            checkValidEmail
                    .apply(emailString)
                    .get())) {
      mBinding.editTextEmail.setError("Invalid email");
      isValid = false;
    }
    if (PasswordValidationResult.PWD_INVALID_LENGTH.equals(
            checkPasswordLength
                    .apply(passwordString)
                    .get())) {
      mBinding.editTextPassword.setError("Invalid password length");
      isValid = false;
    } else if (PasswordValidationResult.PWD_MISSING_UPPER.equals(
            checkPasswordContainUpperCase
                    .apply(passwordString)
                    .get())) {
      mBinding.editTextPassword.setError("Need at least 1 uppercase letter");
      isValid = false;
    }

    String passwordRetypeString = mBinding.editTextPasswordRetype.getText().toString();
    if (!passwordRetypeString.equals(passwordString)) {
      mBinding.editTextPasswordRetype.setError("Password mismatches");
      isValid = false;
    }
    if (isValid) {
      this.verifyAuthWithServer();
    }
  }

  /**
   * A helper method to see if a text of an EditText object is empty.
   * @param element the given UI's EditText object
   * @return true if the text if empty, false otherwise
   */
  private boolean isLengthZero(EditText element) {
    boolean isLengthZero = false;
    if (element.getText().toString().length() == 0)
      isLengthZero = true;

    return isLengthZero;
  }

  /**
   * Send registration data to server.
   */
  private void verifyAuthWithServer() {
    mRegisterModel.connect(
            mBinding.editTextFirstName.getText().toString(),
            mBinding.editTextLastName.getText().toString(),
            mBinding.editTextEmail.getText().toString(),
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
//    Log.i("JSON body", response.toString());
    if (response.length() > 0) {
      if (response.has("code")) {
        try {
          mBinding.editTextEmail.setError(
                  "Error Authenticating: " + response.getJSONObject("data").getString("message"));
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
        navigateToLogin();
      }
    } else {
      Log.d("JSON Response", "No Response");
    }
  }

  /**
   * Navigate from register fragment to login fragment.
   */
  private void navigateToLogin(){
    Navigation.findNavController(getView()).navigate(
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
    );
  }
}