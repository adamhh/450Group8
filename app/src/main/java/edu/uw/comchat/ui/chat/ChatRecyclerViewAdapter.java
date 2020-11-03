package edu.uw.comchat.ui.chat;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatCardBinding;

public class ChatRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder> {

    private final List<Chat> mChats;

    public ChatRecyclerViewAdapter(List<Chat> items) {
        this.mChats = items;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    /**
     * Objects from this class represent a single row view from the list.
     */
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private Chat mChat;

        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
        }

        void setChat(final Chat chat) {
            mChat = chat;
            binding.textName.setText(chat.getSender());

            final String preview = Html.fromHtml(
                    chat.getMessage(),
                    Html.FROM_HTML_MODE_COMPACT)
                    .toString()
                    + "...";
            binding.textMessage.setText(preview);
        }
    }
}
