package edu.uw.comchat.ui.connection;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        connectButtonText = "Manage Request";
        break;
      case 3:

        connectButtonText = "Pending";
        break;
      default:
        connectButtonText = "Send Request";
    }
    binding.profileConnectButtonId.setText(connectButtonText);
    binding.profileConnectButtonId.setOnClickListener(this::connectClicked);

  }

  /**
   * Method that handles the connect button being clicked.  Depending on the position (the tab)
   * that the user is in, the user will be given various options and asked to confirm selection.
   * @param view
   */
  private void connectClicked(View view) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
    alertDialogBuilder.setTitle("Connection Options");
    switch (mPosition) {
      case 1:
        //For existing connection tab
        alertDialogBuilder.setMessage("Are you sure you want to remove connection?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mConnectionViewModel.removeConnection(mEmail);
            binding.profileConnectButtonId.setText("Connection Removed");
            binding.profileConnectButtonId.setClickable(false);
          }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        AlertDialog alertDialog = alertDialogBuilder.show();

        break;
      case 2:
        //For incoming connection tab
        alertDialogBuilder.setMessage("Would you like to accept or remove request?");
        alertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mConnectionViewModel.connectionRequest(mEmail);
          }
        });
        alertDialogBuilder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mConnectionViewModel.removeConnection(mEmail);
          }
        });
        AlertDialog alertDialog2 = alertDialogBuilder.show();
        break;
      case 3:
        //For outgoing connection tab
        alertDialogBuilder.setMessage("Connection request pending, would you like to cancel request?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mConnectionViewModel.removeConnection(mEmail);
            binding.profileConnectButtonId.setClickable(false);
            binding.profileConnectButtonId.setText("Request Canceled");
          }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        AlertDialog alertDialog3 = alertDialogBuilder.show();
        break;

      default:
        alertDialogBuilder.setMessage("Suggested connection, would you like to send request?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mConnectionViewModel.connectionRequest(mEmail);
            binding.profileConnectButtonId.setClickable(false);
            binding.profileConnectButtonId.setText("Request Sent");
          }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        AlertDialog alertDialog4 = alertDialogBuilder.show();
        break;
    }

  }

}