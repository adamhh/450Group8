package edu.uw.comchat;

/**
 * This class support changing theme action.
 */
public class Theme {
  public static int mTheme = R.style.Theme_ComChat;
  public static boolean mIsDark = false;

  /**
   * Return a theme.
   *
   * @return an integer indicate theme id
   */
  public static int getTheme() {
    return mTheme;
  }

  /**
   * Change theme status based on a given string.
   *
   * @param description a string indicate which theme you should choose
   */
  public static void setTheme(String description) {
    int result = 0;
    String str = description.toLowerCase();
    switch (str) {
      case "red":
        result = R.style.Theme_ComChatRed;
        break;
      case "grey":
        result = R.style.Theme_ComChatBlueGrey;
        break;
      default:
        result = R.style.Theme_ComChat;
        break;
    }
    mTheme = result;

  }
  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.

}
