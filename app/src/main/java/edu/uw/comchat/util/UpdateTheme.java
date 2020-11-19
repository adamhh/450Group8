package edu.uw.comchat.util;


import android.util.Log;

import java.util.function.BiConsumer;

import edu.uw.comchat.MainActivity;
import edu.uw.comchat.Theme;

public interface UpdateTheme extends BiConsumer<String, MainActivity> {
  static UpdateTheme updateThemeColor() {
    Log.i("Change Theme", "Pressed");
    return (themeName, activity) -> {
      Theme.setTheme(themeName);
      activity.recreate();
    };
  }
}
