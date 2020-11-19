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
    final JSONObject group = new JSONObject(groupAsJson);

    // TODO messageid is still an arbitrary number, hasn't been implemented yet - Hung Vu.
    return new ChatGroupInfo(
            group.getInt("chatid"), 0
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
