package edu.uw.comchat.ui.chat;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Represent a messaging group.
 *
 * @author Jerry Springer
 * @version 11 November 2020
 */
public class ChatGroupInfo implements Serializable {

  private final int mGroupId;
  /**
   * Used to show the most recent message.
   */
  // Show in the card itself? - Hung Vu
  private final int mMessageId;

  public ChatGroupInfo(int groupId, int messageId) {
    mGroupId = groupId;
    mMessageId = messageId;
  }

  public static ChatGroupInfo createFromJsonString(final String groupAsJson) throws JSONException {
    // TODO Create group from a JSON String
    // We would need all fields like in ChatMessage, these 2 may not be enough.
    // TODO We can have a map of GroupID as a key, ChatViewModel as a value so we can
    //  initialize multiple different rooms. For now, all (dummy) room are the same as they own
    //  the same view model. Will we use "Message" class for that? - Hung Vu
    final JSONObject group = new JSONObject(groupAsJson);
    return new ChatGroupInfo(
            group.getInt("chatId"),
            group.getJSONArray("row").getJSONObject(0).getInt("messageid")
    );
  }

  public int getGroupId() {
    return mGroupId;
  }

  public int getMessageId() {
    return mMessageId;
  }

  /**
   * Provides equality solely based on GroupId.
   *
   * @param other the other object to check for equality
   * @return true if other group ID matches this group ID, false otherwise
   */
  @Override
  public boolean equals(@Nullable Object other) {
    boolean result = false;
    if (other instanceof ChatGroupInfo) {
      result = mGroupId == ((ChatGroupInfo) other).mGroupId;
    }

    return result;
  }
}
