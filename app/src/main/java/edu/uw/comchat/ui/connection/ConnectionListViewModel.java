package edu.uw.comchat.ui.connection;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.ui.chat.ChatGroupInfo;

public class ConnectionListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Connection>> mFriendList;
    public ConnectionListViewModel(@NonNull Application application) {
        super(application);
        mFriendList = new MutableLiveData<>();
        mFriendList.setValue(new ArrayList<>());
    }
    public void addConnectionListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Connection>> observer) {
        mFriendList.observe(owner, observer);
    }
    private void hardCodeFriendsList() {
        //JSONObject
    }
    private void handleError(final VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PTOJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
    private void handleResult(final JSONObject result) {
        Log.d("ZZZ",result.toString());
        try {
            JSONArray contactsArray = result.getJSONArray("contacts");
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp.add(contactsArray.getJSONObject(i).getString("email"));

            }
            Log.d("XXX", temp.toString());
            List<Connection> connList = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                connList.add(new Connection(temp.get(i)));
            }
            mFriendList.setValue(connList);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatPageViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }
    public void connectGet() {
        String url = getApplication().getResources().getString(R.string.base_url) + "connections/test1@test.com";
        JSONObject j = new JSONObject();
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

}
//D/ZZZ: {"contactCount":3,"contacts":[{"email":"test2@test.com"},{"email":"test3@test.com"},{"email":"phongfly0705@gmail.com"}]}
}
