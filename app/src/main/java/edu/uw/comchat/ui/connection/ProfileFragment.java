package edu.uw.comchat.ui.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentProfileBinding;

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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    ImageView image = (ImageView) view.findViewById(R.id.profile_image);
    image.setImageResource(R.drawable.ic_temp_avatar);
    return view;
  }

  //TODO need to get data from individual card to populate profile
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding = FragmentProfileBinding.bind(view);
  }


  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}