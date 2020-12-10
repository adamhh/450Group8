package edu.uw.comchat.ui.chat;

import androidx.annotation.Nullable;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a messaging group.
 *
 * @author Jerry Springer, Hung Vu
 * @version 11 November 2020
 */
public class ChatGroupInfo implements Serializable {
  // Update GroupInfo to comply with 12/8/20 API
  private final String mGroupName;
  private final int mGroupId;
  private final String mMessage;
  private final String mTime;
  private final boolean mIsGroupChat;

  public ChatGroupInfo(String groupName, int groupId, String message, String time, boolean groupFlag) {
    this.mGroupName = groupName;
    this.mGroupId = groupId;
    this.mMessage = message;
    this.mTime = time;
    this.mIsGroupChat = groupFlag;
  }

  public static ChatGroupInfo createFromJsonString(final String groupAsJson) throws JSONException {
    final JSONObject group = new JSONObject(groupAsJson);
    return new ChatGroupInfo(
            group.getString("name"),
            group.getInt("chatid"),
            group.getString("message"),
            group.getString("timestamp"),
            group.getInt("direct") == 1 ? false : true);
  }

  public int getGroupId() {
    return mGroupId;
  }

  public String getGroupName(){
    return this.mGroupName;
  }
  public String getMessage(){
    return this.mMessage;
  }
  public String getTime(){
    return this.mTime;
  }
  public boolean isIsGroupChat(){
    return mIsGroupChat;
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

  @Override
  public String toString() {
    return "ChatGroupInfo{" +
            "mGroupName='" + mGroupName + '\'' +
            ", mGroupId=" + mGroupId +
            ", mMessage='" + mMessage + '\'' +
            ", mTime='" + mTime + '\'' +
            '}';
  }
}
