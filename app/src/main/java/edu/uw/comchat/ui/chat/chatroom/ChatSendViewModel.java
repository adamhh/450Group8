package edu.uw.comchat.ui.chat.chatroom;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.util.HandleRequestError;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A view model for sent messages in a chat room. This class
 * is from lab 5 and is modified to suit our application.
 *
 * @author Hung Vu
 */
// Ignore checkstyle member name error. Checkstyle done, Sprint 3, Hung Vu.
public class ChatSendViewModel extends AndroidViewModel {

  /**
   * Hold JSON object of sent messages in a chat room.
   */
  private final MutableLiveData<JSONObject> mResponse;

  /**
   * A constructor.
   *
   * @param application an application context
   */
  public ChatSendViewModel(@NonNull Application application) {
    super(application);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());
  }

  /**
   * Add response observer to the view model.
   *
   * @param owner life cycle of the observer
   * @param observer the action to perform when a change happens
   */
  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super JSONObject> observer) {
    mResponse.observe(owner, observer);
  }

  /**
   * Connect to web service and add sent message information.
   *
   * @param chatId the room ID
   * @param jwt the JSON web token
   * @param message the Message
   */
  public void sendMessage(final int chatId, final String jwt, final String message) {
    String url = getApplication().getResources().getString(R.string.base_url) + "messages";

    JSONObject body = new JSONObject();
    try {
      body.put("message", message);
      body.put("chatId", chatId);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Request request = new JsonObjectRequest(
            Request.Method.POST,
            url,
            body, //push token found in the JSONObject body
            mResponse::setValue, // we get a response but do nothing with it
            error -> HandleRequestError.handleErrorForChat(error)) {

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
  }

}
