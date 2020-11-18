package edu.uw.comchat.ui.chat;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;

/**
 * View model to hold required information to form groups in chat page.
 * @author Hung Vu
 */
public class ChatGroupInfoViewModel extends AndroidViewModel {
  private MutableLiveData<List<Integer>> mGroupInfo;

  public ChatGroupInfoViewModel (@NonNull Application application){
    super(application);
    mGroupInfo = new MutableLiveData<>();
    mGroupInfo.setValue(new ArrayList<>());
  }

  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super List<Integer>> observer) {
    mGroupInfo.observe(owner, observer);
  }

  public void getAllUserCommunicationGroup(final String email, final String jwt){
    String url = getApplication().getResources().getString(R.string.base_url) +
            "chats/getchatid/" + email;

    Request request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null, //no body for this get request
            this::handelSuccess,
            this::handleError) {

      @Override
      public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        // add headers <key,value>
        headers.put("Authorization", jwt);
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

    //code here will run
  }

  private void handelSuccess(final JSONObject response) {
    ArrayList<Integer> list = new ArrayList<>();
    if (!response.has("contacts")) {
      throw new IllegalStateException("Unexpected response in ChatGroupInfoViewModel: " + response);
    }
    try {
      JSONArray contactsArray = response.getJSONArray("contacts");
      for (int i = 0; i < contactsArray.length(); i++) {
        JSONObject chatId = contactsArray.getJSONObject(i);
        list.add(chatId.getInt("chatid"));
      }
      mGroupInfo.setValue(list);
    } catch (JSONException e) {
      Log.e("JSON PARSE ERROR", "Found in handle Success ChatGroupInfoViewModel");
      Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
    }
  }

  private void handleError(final VolleyError error) {
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
