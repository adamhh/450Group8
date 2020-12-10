package edu.uw.comchat.util;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.ui.chat.CreateFragmentDirections;
import edu.uw.comchat.ui.chat.chatroom.MessagePageFragmentDirections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This interface provides functions which helps perform actions related to a chat room.
 * This includes adding and removing a user from chat room, or creating a new room.
 *
 * @author Hung Vu
 */
public interface ModifyChatRoom extends BiConsumer<ArrayList<String>, Fragment> {
  // TODO Have to change these functions to comply with latest API (12/8).
  /**
   * Provide a function which helps remove a user from specific chat room.
   * This function accept an array list, which contains chatId at index 0,
   * email of user to be deleted at index 1, and jwt at index 2. Ignore index 3 and 4.
   * This also requires a fragment as a second parameter
   * to help perform a request.
   *
   * @return a ModifyChatRoom function which helps modify chat room
   */
  static ModifyChatRoom removeMember() {
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (memberToDelete, fragment) -> {
      Request request = new JsonObjectRequest(
              Request.Method.DELETE,
              url + "/" + memberToDelete.get(0) + "/" + memberToDelete.get(1),
              null, //no body for this get request
              response -> {
                try {
                  if (response.getString("success").equals("true")) {
                    Log.i("DELETE user in chatroom", "Successfully delete user "
                            + memberToDelete.get(1)
                            + "from chat room "
                            + memberToDelete.get(0)
                            + response);
                  } else {
                    throw new IllegalStateException("Cannot delete user "
                            + memberToDelete.get(1)
                            + "from chat room "
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
      RequestQueueSingleton.getInstance(fragment
              .getActivity()
              .getApplication()
              .getApplicationContext())
              .addToRequestQueue(request);
    };
  }

  /**
   * Provide a function which helps add a user to a specific chat room.
   * This function accept an array list, which contains chatId at index 0,
   * email of user to be added at index 1, and jwt at index 2. Ignore index 3 and 4.
   * This also requires a fragment as a second parameter
   * to help perform a request.
   *
   * @return a ModifyChatRoom function which helps modify chat room
   */
  static ModifyChatRoom addMember() {
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (memberToAdd, fragment) -> {
      Request request = new JsonObjectRequest(
              Request.Method.PUT,
              url + "/" + memberToAdd.get(0) + "?email=" + memberToAdd.get(1),
              null, //no body for this get request
              response -> {
                if (response.has("success")) {
                  Log.i("ADD user in chatroom", "Successfully add user "
                          + memberToAdd.get(1)
                          + "to chat room "
                          + memberToAdd.get(0)
                          + response);
                } else {
                  throw new IllegalStateException("Cannot add user "
                          + memberToAdd.get(1)
                          + "to chat room "
                          + memberToAdd.get(0)
                          + response);
                }
              },
              error -> HandleRequestError.handleErrorForChat(error)) {

        @Override
        public Map<String, String> getHeaders() {
          Map<String, String> headers = new HashMap<>();
          // add headers <key,value>
          headers.put("Authorization", memberToAdd.get(2));
          return headers;
        }
      };
      request.setRetryPolicy(new DefaultRetryPolicy(
              10_000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //Instantiate the RequestQueue and add the request to the queue
      RequestQueueSingleton.getInstance(fragment
              .getActivity()
              .getApplication()
              .getApplicationContext())
              .addToRequestQueue(request);
    };
  }

  /**
   * Provide a function which helps create a new group chat room.
   * This function accept an array list, which contains a room name
   * at index 0, creator's email at index 1 and jwt at index 2.
   * Index 3 store a string whether a room is DM or group chat.
   * "true" for group, "false" for DM. In case of creating DM room,
   * there will be an index 4 storing targeted user's email. Index 4
   * won't be used by this function.
   *
   * This also requires a fragment as a second parameter
   * to help perform a request.
   *
   * @return a ModifyChatRoom function which helps create a group chat room
   */
  static ModifyChatRoom createGroupRoom() {
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (roomToCreate, fragment) -> {
      JSONObject body = new JSONObject();
      try {
        body.put("name", roomToCreate.get(0));
      } catch (JSONException e) {
        // TODO Have a better handler - Hung Vu.
        e.printStackTrace();
      }
      Request request = new JsonObjectRequest(
              Request.Method.POST,
              url,
              body,
              response -> {
                try {
                  if (response.getString("success").equals("true")) {
                    Log.i("CREATE a new room", "Create a new room successfully.");
                    // TODO Since the endpoint does not automatically add the creator after
                    //  a room is created, a separate call to endpoint for adding.
                    //  I'm not sure if there is a case when the call for adding is slow, so a
                    //  user hasn't been added to the room yet, so they can't perform messaging.
                    //  Might need a handler - Hung Vu?
                    ArrayList<String> populateRoom = new ArrayList<>();
                    populateRoom.add(response.getString("chatID"));
                    populateRoom.add(roomToCreate.get(1));
                    populateRoom.add(roomToCreate.get(2));
                    addMember().accept(populateRoom, fragment);

                    Navigation.findNavController(fragment.getView()).navigate(
                            CreateFragmentDirections.actionCreateFragmentToMessageListFragment(
                                    response.getInt("chatID"), Boolean.valueOf(roomToCreate.get(3))
                            )
                    );
                  } else {
                    throw new IllegalStateException("Cannot create a new room." + response);
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
          headers.put("Authorization", roomToCreate.get(2));
          return headers;
        }
      };
      request.setRetryPolicy(new DefaultRetryPolicy(
              10_000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //Instantiate the RequestQueue and add the request to the queue
      RequestQueueSingleton.getInstance(fragment
              .getActivity()
              .getApplication()
              .getApplicationContext())
              .addToRequestQueue(request);
    };
  }

  /**
   * Provide a function which helps create a new DM chat room.
   * This function accept an array list, which contains a room name
   * at index 0, creator's email at index 1 and jwt at index 2.
   * Index 3 store a string whether a room is DM or group chat.
   * "true" for group, "false" for DM. In case of creating DM room,
   * there will be an index 4 storing targeted user's email. Index 4 is used
   * by this function.
   *
   * This also requires a fragment as a second parameter
   * to help perform a request.
   *
   * @return a ModifyChatRoom function which helps create a group chat room
   */
  static ModifyChatRoom createDmRoom(){
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (roomToCreate, fragment) -> {
      JSONObject body = new JSONObject();
      try {
        body.put("name", roomToCreate.get(0));
        body.put("email_A", roomToCreate.get(1));
        body.put("email_B", roomToCreate.get(4));
      } catch (JSONException e) {
        // TODO Have a better handler - Hung Vu.
        e.printStackTrace();
      }
      Request request = new JsonObjectRequest(
              Request.Method.POST,
              url + "/" + "direct",
              body,
              response -> {
                try {
                  if (response.getString("success").equals("true")) {
                    Log.i("CREATE a new room", "Create a new DM room successfully.");
                    Navigation.findNavController(fragment.getView()).navigate(
                            CreateFragmentDirections.actionCreateFragmentToMessageListFragment(
                                    response.getInt("chatID"), Boolean.valueOf(roomToCreate.get(3))
                            )
                    );
                  } else {
                    throw new IllegalStateException("Cannot create a new DM room." + response);
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
          headers.put("Authorization", roomToCreate.get(2));
          return headers;
        }
      };
      request.setRetryPolicy(new DefaultRetryPolicy(
              10_000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //Instantiate the RequestQueue and add the request to the queue
      RequestQueueSingleton.getInstance(fragment
              .getActivity()
              .getApplication()
              .getApplicationContext())
              .addToRequestQueue(request);
    };
  }


  /**
   * Provide a function which helps delete a group chat room.
   * This function accept an array list, which contains a room id at index 0,
   *  and JWT at index 1.
   *
   * This also requires a fragment as a second parameter
   * to help perform a request.
   *
   * @return a ModifyChatRoom function which helps create a group chat room
   */
  static ModifyChatRoom deleteRoom(){
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (roomToDelete, fragment) -> {
      Request request = new JsonObjectRequest(
              Request.Method.DELETE,
              url + "/" + roomToDelete.get(0),
              null,
              response -> {
                try {
                  if (response.getString("success").equals("true")) {
                    Log.i("DELETE a group room", "Delete successfully.");
                    Navigation.findNavController(fragment.getView()).navigate(
                            MessagePageFragmentDirections.actionMessagePageFragmentToNavigationChat()
                    );
                    new MaterialAlertDialogBuilder(fragment.getActivity())
                            .setTitle("Message")
                            .setMessage("Delete room successfully. Tap anywhere to turn off this message.")
                            .show();
                  } else {
                    throw new IllegalStateException("Cannot delete room." + response);
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
          headers.put("Authorization", roomToDelete.get(1));
          return headers;
        }
      };
      request.setRetryPolicy(new DefaultRetryPolicy(
              10_000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      //Instantiate the RequestQueue and add the request to the queue
      RequestQueueSingleton.getInstance(fragment
              .getActivity()
              .getApplication()
              .getApplicationContext())
              .addToRequestQueue(request);
    };
  }

}
