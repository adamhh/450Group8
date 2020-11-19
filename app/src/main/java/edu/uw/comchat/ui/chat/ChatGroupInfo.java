package edu.uw.comchat.ui.chat;

import androidx.annotation.Nullable;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;



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

  /**
   * This method will create group from a json string. However, since webservice
   * implementation has been changed, this method is redundant at the moment. We may
   * or may not use it.
   *
   * @param groupAsJson json string contains information about chat group
   * @return a ChatGroupInfo object.
   * @throws JSONException an exception
   */
  public static ChatGroupInfo createFromJsonString(final String groupAsJson) throws JSONException {
    // TODO Create group from a JSON String - Done, Hung Vu
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
  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}
