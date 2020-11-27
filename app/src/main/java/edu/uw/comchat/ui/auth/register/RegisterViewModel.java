package edu.uw.comchat.ui.auth.register;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import edu.uw.comchat.util.HandleRequestError;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Class from lab 3.
 * Subject to modification later on - Hung Vu.
 */
// Ignore checkstyle member name error.
public class RegisterViewModel extends AndroidViewModel {
  private MutableLiveData<JSONObject> mResponse;

  /**
   * Constructor.
   *
   * @param application the application
   */
  public RegisterViewModel(@NonNull Application application) {
    super(application);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());
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
   * POST request to webservice for registering account.
   *
   * @param first first name of user.
   * @param last last name of user.
   * @param email email of user.
   * @param password account password.
   */
  public void connect(final String first,
                      final String last,
                      final String email,
                      final String password) {
    String url = "https://comchat-backend.herokuapp.com/register";

    JSONObject body = new JSONObject();
    try {
      body.put("first", first);
      body.put("last", last);
      body.put("email", email);
      body.put("password", password);
      //      Log.i("JSON body", body.toString());
    } catch (JSONException e) {
      // TODO Have a better handler - Hung Vu.
      e.printStackTrace();
    }

    Request request = new JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            mResponse::setValue,
            error -> HandleRequestError.handleErrorForAuth(error, mResponse));

    request.setRetryPolicy(new DefaultRetryPolicy(
            10_000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //Instantiate the RequestQueue and add the request to the queue
    Volley.newRequestQueue(getApplication().getApplicationContext())
            .add(request);
  }


}
