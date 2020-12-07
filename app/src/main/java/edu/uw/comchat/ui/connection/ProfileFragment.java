package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentProfileBinding;
import edu.uw.comchat.io.RequestQueueSingleton;

/**
 * A fragment that is used to show a profile.
 *
 * @author Jerry Springer
 * @author Adam Hall
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ProfileFragment extends Fragment {
  FragmentProfileBinding binding;
  private int mPosition;
  private String mEmail;
  private ConnectionListViewModel mConnectionViewModel;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    ImageView image = (ImageView) view.findViewById(R.id.profile_image);
    image.setImageResource(R.drawable.ic_temp_avatar);
    mConnectionViewModel = new ViewModelProvider(getActivity()).get(ConnectionListViewModel.class);
    return view;
  }
  //TODO need to get data from individual card to populate profile
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding = FragmentProfileBinding.bind(view);
    ProfileFragmentArgs profileArgs = ProfileFragmentArgs.fromBundle(getArguments());
    mEmail = profileArgs.getProfileId();
    binding.profileEmailId.setText(mEmail);
    binding.profileFirstId.setText(profileArgs.getProfilefirstname());
    binding.profileLastId.setText(profileArgs.getProfilelastname());
    mPosition = profileArgs.getPosition();
    String connectButtonText;
    switch (mPosition) {
      case 1:
        connectButtonText = "Remove Connection";
        break;
      case 2:
        connectButtonText = "Accept Request";
        break;
      case 3:
        connectButtonText = "Pending";
        break;
      default:
        connectButtonText = "Send Request";
    }
    binding.profileConnectButtonId.setText(connectButtonText);
    binding.profileConnectButtonId.setOnClickListener(this::connectClicked);
    binding.profileMessageButtonId.setOnClickListener(this::messageClicked);

  }

  private void connectClicked(View view) {
    switch (mPosition) {
      case 1:
        mConnectionViewModel.removeConnection(mEmail);
        break;
      case 3:
        binding.profileMessageButtonId.setText("Pending");
        break;
      case 2:
        //handles accepting incoming requests and sending requests to suggested
        Log.d("CONNREQ", "SENT");
        mConnectionViewModel.connectionRequest(mEmail);
        break;
      default:
        break;
    }
    binding.profileConnectButtonId.setClickable(false);
  }

  /**
   *
   * @param view
   */
  private void messageClicked(View view) {
  }



}