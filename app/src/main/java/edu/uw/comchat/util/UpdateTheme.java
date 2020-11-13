package edu.uw.comchat.util;

import android.app.TaskStackBuilder;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import edu.uw.comchat.MainActivity;
import edu.uw.comchat.Theme;

public interface UpdateTheme extends BiConsumer<String, AppCompatActivity> {
  static void updateThemeColor (String themeName, AppCompatActivity owner){
    Theme.setTheme(themeName);
    owner.recreate();
    TaskStackBuilder.create(owner)
            .addNextIntent(new Intent(owner, MainActivity.class))
            .addNextIntent(owner.getIntent())
            .startActivities();
  }
}
