package edu.uw.comchat.ui.auth.login;

import android.app.Application;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import edu.uw.comchat.io.RequestQueueSingleton;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;




/**
 * Class from lab 3.
 * Subject to modification later on - Hung Vu.
 */
// Ignore checkstyle member name error.
public class LoginViewModel extends AndroidViewModel {
  private MutableLiveData<JSONObject> mResponse;

  /**
   * Constructor.
   *
   * @param application the application
   */
  public LoginViewModel(@NonNull Application application) {
    super(application);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());
  }

  /**
   * Add observer to a view model instance.
   *
   * @param owner life cycle of the class bound to model instance
   * @param observer the response
   */
  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super JSONObject> observer) {
    mResponse.observe(owner, observer);
  }

  /**
   * Provide behavior when a HTTP error is returned.
   *
   * @param error HTTP error (encapsulated in VolleyError)
   */
  private void handleError(final VolleyError error) {
    if (Objects.isNull(error.networkResponse)) {
      try {
        mResponse.setValue(new JSONObject("{" + "error:\"" + error.getMessage() + "\"}"));
      } catch (JSONException e) {
        Log.e("JSON PARSE", "JSON Parse Error in handleError");
      }
    } else {
      String data = new String(error.networkResponse.data, Charset.defaultCharset())
              .replace('\"', '\'');
      try {
        JSONObject response = new JSONObject();
        response.put("code", error.networkResponse.statusCode);
        response.put("data", new JSONObject(data));
        mResponse.setValue(response);
      } catch (JSONException e) {
        Log.e("JSON PARSE", "JSON Parse Error in handleError");
      }
    }
  }

  /**
   * GET request to webservice to login.
   *
   * @param email user's email address
   * @param password user's password
   */
  public void connect(final String email, final String password) {
    String url = "https://comchat-backend.herokuapp.com/auth";

    Request request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null, //no body for this get request
            mResponse::setValue,
            this::handleError) {
      @Override
      public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        // add headers <key,value>
        String credentials = email + ":" + password;
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
      }
    };
    request.setRetryPolicy(new DefaultRetryPolicy(
            10_000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //Instantiate the RequestQueue and add the request to the queue
    RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
            .addToRequestQueue(request);
  }
  // Ignore checkstyle member name error.
}
