package edu.uw.comchat.util;

import android.util.Log;
import edu.uw.comchat.MainActivity;
import edu.uw.comchat.Theme;
import java.util.function.BiConsumer;

/**
 * This interface helps changing the theme at runtime.
 *
 * @author Hung Vu
 */
public interface UpdateTheme extends BiConsumer<String, MainActivity> {

  /**
   * This method changes application theme at runtime. The drawback is
   *  an activity will be recreated.
   *
   * @return a function which helps changing theme
   */
  static UpdateTheme updateThemeColor() {
    Log.i("Change Theme", "Pressed");
    return (themeName, activity) -> {
      Theme.setTheme(themeName);
      activity.recreate();
    };
  }
}
