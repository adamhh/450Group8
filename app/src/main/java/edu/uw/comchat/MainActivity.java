package edu.uw.comchat;

import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * This class is a main activity for the program (homepage/weather/connection/chat/).
 */
// Ignore checkstyle member name error.
public class MainActivity extends AppCompatActivity {
  private AppBarConfiguration mAppBarConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //We need to set theme each activity before it is created
    setTheme(Theme.getTheme());
    //or we can recreate activity
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView navView = findViewById(R.id.nav_main_bottom_view);

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
    if(id == R.id.menu_profile){

    } else if (id == R.id.menu_theme){
      handleChangeThemeAction();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void recreate() {
    Log.d("HEARD", "CLICK HEARD");
    setTheme(R.style.Theme_ComChatRed);
  }

  private void handleChangeThemeAction(){
    String[] themeOptions = new String[]{"Default", "Blue Grey", "Red Black"};
    new MaterialAlertDialogBuilder(this)
            .setTitle("Theme Options")
            .setSingleChoiceItems(themeOptions, 0, null)
            .setPositiveButton(getResources().getString(R.string.item_menu_change_theme_accept),
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                    })
            .show();
  }

}