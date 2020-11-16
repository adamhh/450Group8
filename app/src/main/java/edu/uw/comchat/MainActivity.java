package edu.uw.comchat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.function.BiConsumer;

import edu.uw.comchat.model.UserInfoViewModel;

import static edu.uw.comchat.util.UpdateTheme.*;

/**
 * This class is a main activity for the program (homepage/weather/connection/chat/).
 */
// Minor adjustment so we use dialog instead of a whole fragment - Hung Vu
// Ignore checkstyle member name error.
public class MainActivity extends AppCompatActivity {
  private AppBarConfiguration mAppBarConfiguration;
  private UserInfoViewModel mModel;
  private AlertDialog mAlertDialog;

  private final String RED_THEME = "red";
  private final String DEFAULT_THEME = "default";
  private final String GREY_THEME = "grey";


  private static final BiConsumer<String, MainActivity> changeThemeHandler = updateThemeColor();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //We need to set theme each activity before it is created
    setTheme(Theme.getTheme());
    //or we can recreate activity
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView navView = findViewById(R.id.nav_main_bottom_view);

    // Store email and jwt upon creation - Hung Vu.
    MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
    String email = args.getEmail();
    String jwt = args.getJwt();
    mModel = new ViewModelProvider(
            this,
            new UserInfoViewModel.UserInfoViewModelFactory(email, jwt))
            .get(UserInfoViewModel.class); // First time initialize using inner factory method.

    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_weather,
            R.id.navigation_connection, R.id.navigation_chat)
            .build();

    // Get nav controller
    NavController navController = Navigation.findNavController(this, R.id.fragment_container_main);

    // Setup toolbar
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

    // Setup bottom nav
    NavigationUI.setupWithNavController(navView, navController);
  }


  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.fragment_container_main);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

//    if (id == R.id.action_settings) {
//      NavController navController = Navigation.findNavController(
//              this, R.id.fragment_container_main);
//      navController.navigate(R.id.navigation_settings);
//    }
    if (id == R.id.menu_profile) {
      // This can be changed to a profile page. Just mapping setting fragment
      //  here to show the button is working normally.
      NavController navController = Navigation.findNavController(
              this, R.id.fragment_container_main);
      navController.navigate(R.id.navigation_settings);
    } else if (id == R.id.menu_theme) {
      handleChangeThemeAction();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void recreate() {
    Intent intent = new Intent(MainActivity.this, MainActivity.class);
    intent.putExtra("email", mModel.getEmail());
    intent.putExtra("jwt", mModel.getJwt());

    if (mAlertDialog != null && mAlertDialog.isShowing())
      mAlertDialog.dismiss();

    startActivity(intent);
    finish();
  }

  public void toggleDarkMode()  {
    switch(getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK){
      case Configuration.UI_MODE_NIGHT_YES:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        break;
      default:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

  }

  private void handleChangeThemeAction() {
    String[] themeOptions = new String[]{"Default", "Blue Grey", "Red Black"};
    MainActivity thisActivity = this;
    mAlertDialog = new MaterialAlertDialogBuilder(this)
            .setTitle("Theme Options")
            // Recreate activity = lose all info (still can backup using bundle).
            // Checked item is a default choice, can be stored in bundle too. - Hung Vu
            .setSingleChoiceItems(themeOptions, 0,
                    (dialog, which) -> {
                      if (which == 0) {
                        updateThemeColor().accept(DEFAULT_THEME, thisActivity);

                      } else if (which == 1) {
                        updateThemeColor().accept(GREY_THEME, thisActivity);
                        Log.i("Theme name", "true");

                      } else if (which == 2) {
                        updateThemeColor().accept(RED_THEME, thisActivity);

                      }
                    })
            //  Require to press accept button after having a choice, but not working.
//            .setPositiveButton(getResources().getString(R.string.item_menu_change_theme_accept),
//                    (dialog, which) -> {
//                      if (which == 0){
//                        updateThemeColor().accept(DEFAULT_THEME, thisActivity);
////                        changeTheme(DEFAULT_THEME);
//                      } else if (which == 1){
//                        updateThemeColor().accept(GREY_THEME, thisActivity);
//                        Log.i("Theme name", "true");
////                        changeTheme(GREY_THEME);
//                      } else if (which == 2){
//                        updateThemeColor().accept(RED_THEME, thisActivity);
////                        changeTheme(RED_THEME);
//                      }
//                    })
            .show();
  }
}