package edu.uw.comchat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import edu.uw.comchat.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

/**
 * This class is activity for authentication section (login/register).
 */
public class AuthenticationActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);
    //If it is not already running, start the Pushy listening service
    Pushy.listen(this);

    initiatePushyTokenRequest();
  }

  /**
   * Initiate pushy token request upon starting an application (auth activity).
   */
  private void initiatePushyTokenRequest() {
    new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
  }

  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}