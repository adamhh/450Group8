package edu.uw.comchat.ui.chat.chatroom;

import android.app.Application;
import android.util.Log;
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
import edu.uw.comchat.ui.chat.ChatGroupInfo;
import edu.uw.comchat.util.HandleRequestError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This provides information of a chat room. Currently, it indicates
 * who is in the room.
 *
 * @author Hung Vu
 */
// Ignore checkstyle member name error. Checkstyle done, Sprint 3, Hung Vu.
public class InRoomInfoViewModel extends AndroidViewModel {
  private MutableLiveData<List<String>> mUserInRoomInfo;

  /**
   * A constructor.
   *
   * @param application the application context
   */
  public InRoomInfoViewModel(@NonNull Application application) {
    super(application);
    mUserInRoomInfo = new MutableLiveData<>();
    mUserInRoomInfo.setValue(new ArrayList<>());
  }

  /**
   * Get List of member.
   *
   * @return a List contains email of users in a group
   */
  public List<String> getMemberList() {
    return mUserInRoomInfo.getValue();
  }

  /**
   * Add response observer.
   *
   * @param owner    a life cycle of listener
   * @param observer the response action
   */
  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super List<String>> observer) {
    mUserInRoomInfo.observe(owner, observer);
  }

  /**
   * This perform a request to get email of user in a chat room.
   *
   * @param chatId id of the chat room
   * @param jwt    a json web token
   */
  public void getEmailOfUserInRoom(final int chatId, final String jwt) {
    String url = getApplication().getResources().getString(R.string.base_url)
            + "chats/" + chatId;
    getMemberList().clear();
    Request request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null, //no body for this get request
            this::handelSuccess,
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

  private void handelSuccess(JSONObject response) {
    ArrayList<ChatGroupInfo> list = new ArrayList<>();
    if (!response.has("rows")) {
      throw new IllegalStateException("Unexpected response in InRoomInfoViewModel: " + response);
    }
    try {
      JSONArray contactsArray = response.getJSONArray("rows");
      for (int i = 0; i < contactsArray.length(); i++) {
        JSONObject memberEmail = contactsArray.getJSONObject(i);
        mUserInRoomInfo.getValue().add(memberEmail.getString("email"));
      }
    } catch (JSONException e) {
      Log.e("JSON PARSE ERROR", "Found in handle Success ChatPageViewModel");
      Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
    }
  }

}
