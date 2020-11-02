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



@SuppressWarnings("checkstyle:MemberName")
public class LoginFragment extends Fragment {

  private FragmentLoginBinding mBinding;
  private LoginViewModel mLoginModel;

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
   *
   */
  private void handleSignInButton() {
    // TODO add Verification (client side).
//    Navigation.findNavController(getView()).navigate(
//            LoginFragmentDirections.actionLoginFragmentToMainActivity()
//    );

    // Exit the activity so users cannot back navigate to login
//    getActivity().finish();
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
        } catch (JSONException e) {
          Log.e("JSON Parse Error", e.getMessage());
        }
      } else {
//        try {
          navigateToMainActivity(
//                  binding.editTextEmail.getText().toString(),
//                  response.getString("token")
          );
//        }
//        catch (JSONException e) {
//          Log.e("JSON Parse Error", e.getMessage());
//        }
      }
    } else {
      Log.d("JSON Response", "No Response");
    }

  }

  /**
   * Navigate to home page.
   */
  public void navigateToMainActivity(){
    Navigation.findNavController(getView())
            .navigate(LoginFragmentDirections
                    .actionLoginFragmentToMainActivity());
    getActivity().finish();
  }
}