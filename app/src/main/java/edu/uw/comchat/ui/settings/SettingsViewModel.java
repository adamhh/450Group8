package edu.uw.comchat.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SettingsViewModel extends ViewModel {

  public static class SettingsViewModelFactory implements ViewModelProvider.Factory {
    public SettingsViewModelFactory() {
    }

    ;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      if (modelClass == SettingsViewModel.class) {
        return (T) new SettingsViewModel();
      }
      throw new IllegalArgumentException(
              "Argument must be: " + SettingsViewModel.class);
    }

  }
  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}
