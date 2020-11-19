package edu.uw.comchat;

public class Theme {
    public static final String DEFAULT_THEME = "default";
    public static final String GREY_THEME = "grey";
    public static final String RED_THEME = "red";

    private static String mThemeName = DEFAULT_THEME;
    private static int mTheme = R.style.Theme_ComChat;
    private static boolean mIsDark = false;

    public static void toggleDark() {
        mIsDark = !mIsDark;
    }

    public static boolean getIsDark() {
        return mIsDark;
    }

    public static int getTheme() {
        return mTheme;
    }

    public static String getThemeName() {
        return mThemeName;
    }

    public static void setTheme(String description) {
        int result = 0;
        String str = description.toLowerCase();
        switch (str) {
            case "red":
                result = R.style.Theme_ComChatRed;
                mThemeName = RED_THEME;
                break;
            case "grey":
                result = R.style.Theme_ComChatBlueGrey;
                mThemeName = GREY_THEME;
                break;
            default:
                result = R.style.Theme_ComChat;
                mThemeName = DEFAULT_THEME;
                break;
        }
        mTheme = result;
    }
}
