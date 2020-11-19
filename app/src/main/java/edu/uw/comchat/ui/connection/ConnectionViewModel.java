package edu.uw.comchat.ui.connection;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uw.comchat.MainActivity;
import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.model.UserInfoViewModel;
import edu.uw.comchat.util.HandleRequestError;

/**
 * This class is the view model for our connection java classes.
 * @author Adam Hall
 * @version 1
 */
public class ConnectionViewModel extends AndroidViewModel {
    /**
     * An array list of string that will hold connections in CSV format at each index.
     */
    private ArrayList<String> mConnections;
    private MutableLiveData<JSONObject> mResponse;
    private String mUserEmail;


    /**
     * Public constructor
     * @param application
     */
    public ConnectionViewModel(@NonNull Application application) {
        super(application);
        mConnections = new ArrayList<String>();

        Log.d("EMAIL IS", mUserEmail);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        buildConnections();
    }

    private void buildConnections() {
        String url = getApplication().getResources().getString(R.string.base_url) + "connections/test1@test.com";
        JSONObject j = new JSONObject();
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::buildConnectionList,
                this::handleError);

    }

    private void handleError(VolleyError volleyError) {
    }

    private void buildConnectionList(JSONObject jsonObject) {
        Log.d("Heard","1234");
    }

    /**
     * Add observer to a view model instance.
     *
     * @param owner life cycle of the class bound to model instance
     * @param observer the response
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * GET request to webservice to login.
     *
     * @param email user's email address
     * @param password user's password
     */
    public void buildConnections(final String email, final String password) {
        String url = "https://comchat-backend.herokuapp.com/connections/test1@test.com";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                error -> HandleRequestError.handleError(error, mResponse)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                String credentials = email + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
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
