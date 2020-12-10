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
import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used as the view model for the connections list.
 *
 * @author Adam Hall, Jerry Springer
 * @version 19 November 2020
 */
public class ConnectionListViewModel extends AndroidViewModel {
    /**
     * Mutable live data to store connection list.
     */
    private MutableLiveData<List<Connection>> mFriendList;
    /**
     * A list to handle connections so that our recycler view updates in real time.
     */
    private List<Connection> mFriendsHandle = new ArrayList<Connection>();

    /**
     * Mutable live data to store incoming connections request list.
     */
    private MutableLiveData<List<Connection>> mIncomingReqList;
    /**
     * A list to handle our incoming connections to help recycler update in real time.
     */
    private List<Connection> mIncomingHandle = new ArrayList<Connection>();

    /**
     * Mutable live data to store outgoing connections request list.
     */
    private MutableLiveData<List<Connection>> mOutgoingReqList;
    /**
     * A list to handle our outgoing connections to help recycler update in real time.
     */
    private List<Connection> mOutgoingHandle = new ArrayList<Connection>();

    /**
     * Mutable live data to store suggested connections list.
     */
    private MutableLiveData<List<Connection>> mSuggFriendList;
    /**
     * A list to handle our suggested connections to help recycler update in real time.
     */
    private List<Connection> mSuggHandle = new ArrayList<Connection>();
    /**
     * The JWT.
     */
    private String mJwt;
    /**
     * The email.
     */
    private String mEmail;

    /**
     * Public constructor for the view model.
     * @param application the application context.
     */
    public ConnectionListViewModel(@NonNull Application application) {
        super(application);
        mFriendList = new MutableLiveData<>();
        mFriendList.setValue(new ArrayList<>());

        mIncomingReqList = new MutableLiveData<>();
        mIncomingReqList.setValue(new ArrayList<>());

        mOutgoingReqList = new MutableLiveData<>();
        mOutgoingReqList.setValue(new ArrayList<>());

        mSuggFriendList = new MutableLiveData<>();
        mSuggFriendList.setValue(new ArrayList<>());

    }

    /**
     * Adds an observer for our connection list.
     * @param owner The owner
     * @param observer The observer
     */
    public void addConnectionListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Connection>> observer) {
        mFriendList.observe(owner, observer);
    }
    /**
     * Adds an observer for our incoming connection requests list.
     * @param owner The owner
     * @param observer The observer
     */
    public void addIncomingListObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Connection>> observer) {
        mIncomingReqList.observe(owner, observer);
    }
    /**
     * Adds an observer for our outgoing connection requests list.
     * @param owner The owner
     * @param observer The observer
     */
    public void addOutgoingListObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Connection>> observer) {
        mOutgoingReqList.observe(owner, observer);
    }
    /**
     * Adds an observer for our suggested connection list.
     * @param owner The owner
     * @param observer The observer
     */
    public void addSuggestedListObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Connection>> observer) {
        mSuggFriendList.observe(owner, observer);
    }

    /**
     * Called if we receive an error from the webservice on our call.
     * @param error The error
     */
    private void handleError(final VolleyError error) {
        try {
            Log.e("CONNECTION ERROR", error.getLocalizedMessage() + "");
        } catch (Exception e) {
            Log.e("Connection Error", e + "");
        }

    }

    /**
     * This method handles the resulting JSON object and parses it into
     * list of strings, and then a list of connections.
     * @param result The resulting JSON object from webservice GET request
     */
    private void handleResult(final JSONObject result) {

        try {
            JSONArray contactsArray = result.getJSONArray("contacts");
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp.add(contactsArray.getJSONObject(i).getString("email"));
                temp.add(contactsArray.getJSONObject(i).getString("firstname"));
                temp.add(contactsArray.getJSONObject(i).getString("lastname"));
            }
            mFriendsHandle = new ArrayList<>();
            for (int i = 0; i < temp.size(); i+=3) {
                mFriendsHandle.add(new Connection(temp.get(i), temp.get(i+1), temp.get(i+2)));
            }
            mFriendList.setValue(mFriendsHandle);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        try {
            JSONArray contactsArray = result.getJSONArray("sentRequests");
            List<String> temp2 = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp2.add(contactsArray.getJSONObject(i).getString("email"));
                temp2.add(contactsArray.getJSONObject(i).getString("firstname"));
                temp2.add(contactsArray.getJSONObject(i).getString("lastname"));

            }
            mOutgoingHandle = new ArrayList<>();
            for (int i = 0; i < temp2.size(); i+=3) {
                mOutgoingHandle.add(new Connection(temp2.get(i), temp2.get(i+1), temp2.get(i+2)));
            }
            mOutgoingReqList.setValue(mOutgoingHandle);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        try {
            JSONArray contactsArray = result.getJSONArray("receivedRequests");
            List<String> temp3 = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp3.add(contactsArray.getJSONObject(i).getString("email"));
                temp3.add(contactsArray.getJSONObject(i).getString("firstname"));
                temp3.add(contactsArray.getJSONObject(i).getString("lastname"));

            }
            mIncomingHandle = new ArrayList<>();
            for (int i = 0; i < temp3.size(); i+=3) {
                mIncomingHandle.add(new Connection(temp3.get(i), temp3.get(i+1), temp3.get(i+2)));
            }
            mIncomingReqList.setValue(mIncomingHandle);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * This method will make the GET call to our webservice to retrieve a list of contacts
     * a given user has.
     *
     * @param email The users email.
     * @param jwt The users JWT given after authentication.
     */
    public void getAllConnections(final String email, final String jwt) {
        mJwt = jwt;
        mEmail = email;
        Log.i("JWT", mJwt);
        String url = getApplication().getResources().getString(R.string.connections_url) + mEmail;
        JSONObject j = new JSONObject();
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mJwt);
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
        getSuggestedConnections(mEmail, mJwt);
    }

    /**
     * This method will make the GET call to our webservice to retrieve a list suggested contacts
     *
     * @param email The users email.
     * @param jwt The users JWT given after authentication.
     */
    public void getSuggestedConnections(final String email, final String jwt) {
        mJwt = jwt;
        mEmail = email;
        Log.i("JWT", mJwt);
        String url = getApplication().getResources().getString(R.string.connections_url) + "stranger/" + mEmail;
        JSONObject j = new JSONObject();
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleSuggested,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mJwt);
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
     * This method handles the resulting JSON object and parses it into
     * list of strings, and then a list of connections.
     * @param result The resulting JSON object from webservice GET request
     */
    private void handleSuggested(final JSONObject result) {
        try {
            JSONArray contactsArray = result.getJSONArray("strangers");
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp.add(contactsArray.getJSONObject(i).getString("email"));
                temp.add(contactsArray.getJSONObject(i).getString("firstname"));
                temp.add(contactsArray.getJSONObject(i).getString("lastname"));
            }
            mSuggHandle = new ArrayList<>();
            for (int i = 0; i < temp.size(); i+=3) {
                mSuggHandle.add(new Connection(temp.get(i), temp.get(i+1), temp.get(i+2)));
            }
            mSuggFriendList.setValue(mSuggHandle);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle sugggested Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * Method that accepts incoming connection request.
     */
    public void connectionRequest(String theOtherEmail) {
        String url = getApplication().getResources().getString(R.string.connections_url);
        JSONObject j = new JSONObject();
        try {
            j.put("email_A", mEmail);
            j.put("email_B", theOtherEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                j,
                null,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mJwt);
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
     * Method that removes a connection.
     */
    public void removeConnection(String theOtherEmail) {
        String url = getApplication().getResources().getString(R.string.connections_url) + "delete";
        JSONObject j = new JSONObject();
        try {
            j.put("email_A", mEmail);
            j.put("email_B", theOtherEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                j, //no body for this get request
                null,
                null) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mJwt);
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
     * Handles removal of connection from current friends.
     * @param c The connection
     */
    public void connectionListRemove(Connection c, String theEmail) {
        this.removeConnection(theEmail);
        mFriendsHandle.remove(c);
        mFriendList.setValue(mFriendsHandle);

    }

    /**
     * Helps accept connection request.
     * @param c The connection
     */
    public void incomingListAdd(Connection c, String theEmail) {
        this.connectionRequest(theEmail);
        mIncomingHandle.remove(c);
        mIncomingReqList.setValue(mIncomingHandle);

    }

    /**
     * Handles removal on connection from incoming list.
     * @param c The connection
     */
    public void incomingListRemove(Connection c, String theEmail) {
        this.removeConnection(theEmail);
        mIncomingHandle.remove(c);
        mIncomingReqList.setValue(mIncomingHandle);

    }

    /**
     * Helps cancel an outgoing request.
     * @param c The connection
     */
    public void outgoingListRemove(Connection c, String theEmail) {
        this.removeConnection(theEmail);
        mOutgoingHandle.remove(c);
        mOutgoingReqList.setValue(mOutgoingHandle);

    }

    /**
     * Handles removal on connection from current friends.
     * @param c The connection
     */
    public void suggListAdd(Connection c, String theEmail) {
        this.connectionRequest(theEmail);
        mSuggHandle.remove(c);
        mSuggFriendList.setValue(mSuggHandle);
    }

    /**
     * Updates to help real time.
     */
    public void updateLists() {
        mFriendList.setValue(mFriendsHandle);
        mOutgoingReqList.setValue(mOutgoingHandle);
        mIncomingReqList.setValue(mIncomingHandle);
        mSuggFriendList.setValue(mSuggHandle);
    }

    /**
     * Return a list of current connections.
     * @return a list of Connection objects.
     */
    public List<Connection> getConnectionList(){
        return mFriendList.getValue();
    }

    /**
     * Return a list of incoming connections.
     * @return a list of Connection objects.
     */
    public List<Connection> getIncomingList(){
        return mIncomingReqList.getValue();
    }

    /**
    * Return a list of outgoing connections.
    * @return a list of Connection objects.
    */
    public List<Connection> getOutgoingList() { return mOutgoingReqList.getValue(); }

    /**
     * Return a list of suggested connections.
     * @return a list of Connection objects.
     */
    public List<Connection> getSuggestedList() { return mSuggFriendList.getValue(); }

}