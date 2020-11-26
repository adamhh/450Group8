package edu.uw.comchat.util;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.uw.comchat.io.RequestQueueSingleton;

/**
 * This interface provides functions which helps perform actions on a specific chatroom.
 * This includes adding and removing a user from chat room.
 *
 * @author Hung Vu
 */
public interface ModifyChatRoom extends BiConsumer<ArrayList<String>, Application> {

  /**
   * Provide a function which helps remove a user from specific chat room.
   * This function accept an array list, which contains chatId at index 0,
   *  email of user to be deleted at index 1, and jwt at index 2.
   * This also requires an application context as a second parameter
   *  to help perform a request.
   *
   * @return a ModifyChatRoom function which helps modify chat room
   */
  static ModifyChatRoom removeMember() {
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (memberToDelete, application) -> {
      Request request = new JsonObjectRequest(
              Request.Method.DELETE,
              url + "/" + memberToDelete.get(0) + "/" + memberToDelete.get(1),
              null, //no body for this get request
              response -> {
                try {
                  if (response.getString("success").equals("true")) {
                    Log.i("DELETE user in chatroom", "Successfully delete user "
                            + memberToDelete.get(1)
                            + "in chat room "
                            + memberToDelete.get(0)
                            + response);
                  } else {
                    throw new IllegalStateException("Cannot delete user "
                            + memberToDelete.get(1)
                            + "in chat room "
                            + memberToDelete.get(0)
                            + response);
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              },
              error -> HandleRequestError.handleErrorForChat(error)) {

        @Override
        public Map<String, String> getHeaders() {
          Map<String, String> headers = new HashMap<>();
          // add headers <key,value>
          headers.put("Authorization", memberToDelete.get(2));
          return headers;
        }
      };
      request.setRetryPolicy(new DefaultRetryPolicy(
              10_000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //Instantiate the RequestQueue and add the request to the queue
      RequestQueueSingleton.getInstance(application.getApplicationContext())
              .addToRequestQueue(request);
    };
  }

  static ModifyChatRoom addMember() {
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (memberToDelete, application) -> {
      Request request = new JsonObjectRequest(
              Request.Method.PUT,
              url + "/" + memberToDelete.get(0) + "/" + memberToDelete.get(1),
              null, //no body for this get request
              response -> {
                if (response.has("success")) {
                  Log.i("ADD user in chatroom", "Successfully add user "
                          + memberToDelete.get(1)
                          + "in chat room "
                          + memberToDelete.get(0)
                          + response);
                } else {
                  throw new IllegalStateException("Cannot delete user "
                          + memberToDelete.get(1)
                          + "in chat room "
                          + memberToDelete.get(0)
                          + response);
                }
              },
              error -> HandleRequestError.handleErrorForChat(error)) {

        @Override
        public Map<String, String> getHeaders() {
          Map<String, String> headers = new HashMap<>();
          // add headers <key,value>
          headers.put("Authorization", memberToDelete.get(2));
          return headers;
        }
      };
      request.setRetryPolicy(new DefaultRetryPolicy(
              10_000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //Instantiate the RequestQueue and add the request to the queue
      RequestQueueSingleton.getInstance(application.getApplicationContext())
              .addToRequestQueue(request);
    };
  }

}
