package edu.uw.comchat.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Store jwt and email.
 *
 * @author Hung Vu
 */
public class UserInfoViewModel extends ViewModel {
  private String mJwt;
  private String mEmail;

  private UserInfoViewModel(String email, String jwt) {
    mEmail = email;
    mJwt = jwt;
  }

  public String getEmail() {
    return mEmail;
  }

  public String getJwt() {
    return mJwt;
  }


  public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

    private final String email;
    private final String jwt;

    public UserInfoViewModelFactory(String email, String jwt) {
      this.email = email;
      this.jwt = jwt;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      if (modelClass == UserInfoViewModel.class) {
        return (T) new UserInfoViewModel(email, jwt);
      }
      throw new IllegalArgumentException(
              "Argument must be: " + UserInfoViewModel.class);
    }
  }
  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.

}

