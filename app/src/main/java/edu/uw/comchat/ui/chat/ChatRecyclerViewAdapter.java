package edu.uw.comchat.ui.chat;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatCardBinding;

/**
 * A recycler view adapter to be used for the list of a conversations or chats that
 * a user is having with other.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
public class ChatRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder> {

    private final List<Chat> mChats;
    private RecyclerView mRecyclerView;

    /**
     * Creates a new chat recycler view adapter with the given list of chats.
     *
     * @param items the list of chats to be displayed.
     */
    public ChatRecyclerViewAdapter(List<Chat> items) {
        this.mChats = items;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent,false);

        // Sets the on click listener for the view / card
        view.setOnClickListener(this::onClick);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Store the recycler view so we can unwrap the view index in on click
        mRecyclerView = recyclerView;
    }

    /**
     * Navigates to the conversation the user clicks on.
     *
     * @param view the view that was clicked on.
     */
    private void onClick(View view) {

        // Gets the index of the clicked chat
        int position = mRecyclerView.getChildAdapterPosition(view);

        // TODO Make this use a chat ID
        String chatId = "" + position;

        // Navigates to the chat
        Navigation.findNavController(view).navigate(
                ChatListFragmentDirections
                        .actionNavigationChatToMessageListFragment(chatId));
    }

    /**
     * Objects from this class represent a single chat row view from the users chats.
     */
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private Chat mChat;

        /**
         * Creates a new view  holder containing the chat card fragment.
         *
         * @param view the corresponding view in the recycler view.
         */
        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
        }

        /**
         * Stores the data of the corresponding chat and updates the view.
         *
         * @param chat the chat of the view holder.
         */
        void setChat(final Chat chat) {
            mChat = chat;
            binding.textChatCardName.setText(chat.getSender());

            final String preview = Html.fromHtml(
                    chat.getMessage(),
                    Html.FROM_HTML_MODE_COMPACT)
                    .toString();
            binding.textChatCardMessage.setText(preview);
        }
    }
}
