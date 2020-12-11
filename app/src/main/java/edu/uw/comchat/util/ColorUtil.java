package edu.uw.comchat.util;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import edu.uw.comchat.R;

/**
 * Used to help with the coloring of certain items according to theme.
 * @author Jerry Springer
 * @version 10 Dec 2020
 */
public class ColorUtil {

    private final Activity mActivity;
    private final int mThemeId;

    /**
     * Creates a new color util object.
     * @param activity the activity using the color util.
     * @param themeId the style id of the theme.
     */
    public ColorUtil(Activity activity, int themeId) {
        mActivity = activity;
        mThemeId = themeId;
    }

    /**
     * Sets the background color of a view which is used for dividers.
     * @param view the view that is getting a background color change.
     */
    public void setColor(View view) {
        Resources resources = mActivity.getResources();
        if (mThemeId == R.style.Theme_ComChatRed) {
            view.setBackgroundColor(resources.getColor(R.color.redAccentColorDark, mActivity.getTheme()));
        } else if (mThemeId == R.style.Theme_ComChatBlueGrey) {
            view.setBackgroundColor(resources.getColor(R.color.greyAccentColorLight, mActivity.getTheme()));
        } else {
            view.setBackgroundColor(resources.getColor(R.color.greyAccentColorLight, mActivity.getTheme()));
        }
    }
}
