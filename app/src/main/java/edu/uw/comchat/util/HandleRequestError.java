package edu.uw.comchat.util;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.VolleyError;
import java.nio.charset.Charset;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An interface which will handle error response from webserivce.
 *
 * @author Hung Vu
 */
public interface HandleRequestError {

  /**
   * Provide behavior when a HTTP error is returned. This is used when
   * a developer want to update view model with MutableLiveData<JSONObject>
   *
   * @param error HTTP error (encapsulated in VolleyError)
   * @param webResponse a mutable live data which hold JSON response from webserivce
   */
  static void handleErrorForAuth(final VolleyError error,
                                 final MutableLiveData<JSONObject> webResponse){
    if (Objects.isNull(error.networkResponse)) {
      try {
        webResponse.setValue(new JSONObject("{" + "error:\"" + error.getMessage() + "\"}"));
      } catch (JSONException e) {
        Log.e("JSON PARSE", "JSON Parse Error in handleError");
      }
    } else {
      //      Log.i("JSON body", "true");
      String data = new String(error.networkResponse.data, Charset.defaultCharset())
              .replace('\"', '\'');
      try {
        JSONObject response = new JSONObject();
        response.put("code", error.networkResponse.statusCode);
        response.put("data", new JSONObject(data));
        webResponse.setValue(response);
        //        Log.e("HTTP error", error.getMessage());
      } catch (JSONException e) {
        Log.e("JSON PARSE", "JSON Parse Error in handleError");
      }
    }
  }

  /**
   * This method handles request error for view models in chat packages
   *
   * @param error HTTP error (encapsulated in VolleyError)
   */
  static void handleErrorForChat(final VolleyError error){
    if (Objects.isNull(error.networkResponse)) {
      Log.e("NETWORK ERROR", error.getMessage());
    } else {
      String data = new String(error.networkResponse.data, Charset.defaultCharset());
      Log.e("CLIENT ERROR",
              error.networkResponse.statusCode
                      + " "
                      + data);
    }
  }
}
