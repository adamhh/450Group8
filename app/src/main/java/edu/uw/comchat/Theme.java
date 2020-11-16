package edu.uw.comchat;

public class Theme {
    public static int mTheme = R.style.Theme_ComChat;
    public static boolean mIsDark = false;

    public static int getTheme() {
        return mTheme;
    }

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



}
