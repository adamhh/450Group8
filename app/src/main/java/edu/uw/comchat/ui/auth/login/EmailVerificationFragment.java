package edu.uw.comchat.ui.auth.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import edu.uw.comchat.R;

/**
 * May not use this fragment, haven't decide on verification method yet.
 * This one is for displaying another fragment after pressing accept button on register page.
 * As a result, implementation of password recovery will have be changed too.
 *
 * @author Hung Vu
 */
public class EmailVerificationFragment extends Fragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_email_verification, container, false);
  }
}