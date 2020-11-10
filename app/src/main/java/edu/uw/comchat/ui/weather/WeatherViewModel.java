package edu.uw.comchat.ui.weather;

import android.app.Application;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.comchat.io.RequestQueueSingleton;

/**
 * This class provide data and backend connection to webservice for weather fragment.
 *
 * @author Hung Vu
 */
public class WeatherViewModel extends AndroidViewModel {
  private MutableLiveData<JSONObject> mResponse;
  public WeatherViewModel(@NonNull Application application) {
    super(application);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());
  }

  public void addResponseObserver (@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super JSONObject> observer){
    mResponse.observe(owner, observer);
  }

  private void handleError(final VolleyError error){
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

  private void connect(String latitude, String longitude){
    final String url = "https://comchat-backend.herokuapp.com/weather";
    JSONObject body = new JSONObject();
    try {
      body.put("lat", 37.372);
      body.put("lon", -122.038);
    } catch (JSONException e){
      e.printStackTrace();
    }
    Request request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            mResponse::setValue,
            this::handleError);
    
    request.setRetryPolicy(new DefaultRetryPolicy(
            10_000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //Instantiate the RequestQueue and add the request to the queue
    RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
            .addToRequestQueue(request);
  }


}
