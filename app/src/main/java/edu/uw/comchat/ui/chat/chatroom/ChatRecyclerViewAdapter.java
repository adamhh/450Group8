package edu.uw.comchat.ui.chat.chatroom;

import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentChatMessageBinding;
import java.util.List;

/**
 * Recycler View Adapter for chat messages in chat room.
 */
// Ignore checkstyle member name error. Checkstyle done, Sprint 3, Hung Vu.
public class ChatRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatRecyclerViewAdapter.MessageViewHolder> {

  /**
   * A List contains all chat message information.
   */
  private final List<ChatMessage> mMessages;

  /**
   * Email of sender.
   */
  private final String mEmail;

  /**
   * The theme.
   */
  private final Resources.Theme mTheme;

  /**
   * Constructor.
   *
   * @param messages the list of chat message information
   * @param email sender's email
   * @param theme the theme
   */
  public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email, Resources.Theme theme) {
    mMessages = messages;
    mEmail = email;

    // TODO Make this come from the Theme class
    mTheme = theme;
  }

  @NonNull
  @Override
  public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new MessageViewHolder(LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.fragment_chat_message, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    holder.setMessage(mMessages.get(position));
  }

  @Override
  public int getItemCount() {
    return mMessages.size();
  }

  /**
   * Annonymous class for view holder.
   */
  class MessageViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private FragmentChatMessageBinding binding;

    public MessageViewHolder(@NonNull View view) {
      super(view);
      mView = view;
      binding = FragmentChatMessageBinding.bind(view);
    }

    public void setMessage(final ChatMessage message) {
      final Resources res = mView.getContext().getResources();
      final MaterialCardView card = binding.cardRootChatMessage;

      int standard = (int) res.getDimension(R.dimen.chat_margin);
      int extended = (int) res.getDimension(R.dimen.chat_margin_sided);

      if (mEmail.equals(message.getSender())) {
        // This message is from the user. Format is as such
        binding.textChatMessage.setText(message.getMessage());
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        //Set the left margin
        layoutParams.setMargins(extended, standard, standard, standard);
        // Set this View to the right (end) side
        ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
                Gravity.END;

        card.setCardBackgroundColor(
                ColorUtils.setAlphaComponent(
                        res.getColor(R.color.primary, mTheme), 16));

        card.setStrokeWidth(standard / 5);
        card.setStrokeColor(ColorUtils.setAlphaComponent(
                res.getColor(R.color.primaryLight, mTheme), 200));

        binding.textChatMessage.setTextColor(
                res.getColor(R.color.secondaryTextFade, mTheme));


        //Round the corners on the left side
        card.setShapeAppearanceModel(
                card.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, standard * 2)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, standard * 2)
                        .setBottomRightCornerSize(0)
                        .setTopRightCornerSize(0)
                        .build());

        card.requestLayout();
      } else {
        //This message is from another user. Format it as such
        binding.textChatMessage.setText(message.getSender()
                + ": " + message.getMessage());
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();

        //Set the right margin
        layoutParams.setMargins(standard, standard, extended, standard);
        // Set this View to the left (start) side
        ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
                Gravity.START;

        card.setCardBackgroundColor(
                ColorUtils.setAlphaComponent(
                        res.getColor(R.color.secondaryLight, mTheme), 16));

        card.setStrokeWidth(standard / 5);
        card.setStrokeColor(ColorUtils.setAlphaComponent(
                res.getColor(R.color.secondaryLight, mTheme), 200));

        binding.textChatMessage.setTextColor(
                res.getColor(R.color.secondaryTextFade, mTheme));


        //Round the corners on the right side
        card.setShapeAppearanceModel(
                card.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, standard * 2)
                        .setBottomRightCorner(CornerFamily.ROUNDED, standard * 2)
                        .setBottomLeftCornerSize(0)
                        .setTopLeftCornerSize(0)
                        .build());
        card.requestLayout();
      }
    }
  }
}
