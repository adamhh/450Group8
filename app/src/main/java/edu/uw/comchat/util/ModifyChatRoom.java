package edu.uw.comchat.util;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.uw.comchat.io.RequestQueueSingleton;

public interface ModifyChatRoom extends BiConsumer<ArrayList<String>, Application> {
  static ModifyChatRoom removeMember(){
    String url = "https://comchat-backend.herokuapp.com/chats";
    return (memberToDelete, application) -> {

      Request request = new JsonObjectRequest(
              Request.Method.DELETE,
              url + "/" + memberToDelete.get(0) + "/" + memberToDelete.get(1),
              null, //no body for this get request
              response -> {
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
