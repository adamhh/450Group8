package edu.uw.comchat.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.comchat.R;

/**
 * A fragment that shows the list of messages in a group.
 *
 *  * @author Jerry Springer
 *  * @version 3 November 2020
 */
public class MessageListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }
}