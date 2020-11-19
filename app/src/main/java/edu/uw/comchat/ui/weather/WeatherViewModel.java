package edu.uw.comchat.ui.weather;

import static edu.uw.comchat.util.HandleRequestError.handleError;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import edu.uw.comchat.io.RequestQueueSingleton;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * This class provide data and backend connection to webservice for weather fragment.
 *
 * @author Hung Vu
 */
public class WeatherViewModel extends AndroidViewModel {
  private MutableLiveData<JSONObject> mResponse;

  private MutableLiveData<String> mToken;

  /**
   * Constructor.
   *
   * @param application the application
   */
  public WeatherViewModel(@NonNull Application application) {
    super(application);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());
    mToken = new MutableLiveData<>();
    mToken.setValue("");
  }

  /**
   * Make token holder become accessible from fragment.
   *
   * @return a holder of jwt.
   */
  public MutableLiveData<String> getToken() {
    return mToken;
  }

  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super JSONObject> observer) {
    mResponse.observe(owner, observer);
  }

  /**
   * Initiate a request to web service to get weather forecast for
   * current weather, 24-hour weather, and 5-day weather.
   *
   * @param zip the zip code of place a user want to see forecast
   */
  public void connect(String zip) {
    final String url = "https://comchat-backend.herokuapp.com/weather?zip=" + zip;
    JSONObject body = new JSONObject();
    Request request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            mResponse::setValue,
            error -> handleError(error, mResponse)) {
      @Override
      public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        // add headers <key,value>
        headers.put("Authorization", mToken.getValue());
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

  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}
