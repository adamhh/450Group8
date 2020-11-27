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
import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.util.HandleRequestError;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * View model to hold required information to form groups in chat page.
 *
 * @author Hung Vu
 * @version 19 November 2020
 */
public class ChatPageViewModel extends AndroidViewModel {
    private MutableLiveData<List<ChatGroupInfo>> mGroupInfo;
    private static ChatGroupInfo[] mGroupArray;


    public ChatPageViewModel(@NonNull Application application) {
        super(application);
        mGroupInfo = new MutableLiveData<>();
        mGroupInfo.setValue(new ArrayList<>());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatGroupInfo>> observer) {
        mGroupInfo.observe(owner, observer);
    }

    /**
     * Make a call to server and get all chat room id of a provided user.
     * E.g: Provide all chat room ids of test1@test.com
     *
     * @param email a user email.
     * @param jwt   json web token.
     */
    public void getAllUserCommunicationGroup(final String email, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) + "chats/getchatid/" + email;

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

    /**
     * Upon receive chatIds, create their respective ChatGroupInfo.
     * @param response web response
     */
    private void handelSuccess(final JSONObject response) {
        ArrayList<ChatGroupInfo> list = new ArrayList<>();
        if (!response.has("contacts")) {
            throw new IllegalStateException("Unexpected response in ChatPageViewModel: " + response);
        }
        try {
            JSONArray contactsArray = response.getJSONArray("contacts");
            mGroupArray = new ChatGroupInfo[contactsArray.length()];
            for (int i = 0; i < contactsArray.length(); i++) {
                mGroupArray[i] = ChatGroupInfo.createFromJsonString(contactsArray.getJSONObject(i).toString());
            }
            mGroupInfo.setValue(Arrays.asList(mGroupArray));
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatPageViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

//    /**
//     * Print to the console error messages.
//     * @param error an error received from server
//     */
//    private void handleError(final VolleyError error) {
//        if (Objects.isNull(error.networkResponse)) {
//            Log.e("NETWORK ERROR", error.getMessage());
//        } else {
//            String data = new String(error.networkResponse.data, Charset.defaultCharset());
//            Log.e("CLIENT ERROR",
//                    error.networkResponse.statusCode
//                            + " "
//                            + data);
//        }
//    }
}
