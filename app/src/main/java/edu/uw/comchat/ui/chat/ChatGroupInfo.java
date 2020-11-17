package edu.uw.comchat.ui.chat;

import androidx.annotation.Nullable;

import org.json.JSONException;

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
  private final int mMessageId;

  public ChatGroupInfo(int groupId, int messageId) {
    mGroupId = groupId;
    mMessageId = messageId;
  }

  public static ChatGroupInfo createFromJsonString(final String groupAsJson) throws JSONException {
    // TODO Create group from a JSON String
    return null;
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
