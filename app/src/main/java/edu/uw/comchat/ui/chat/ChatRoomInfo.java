package edu.uw.comchat.ui.chat;

import androidx.annotation.Nullable;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a messaging group.
 *
 * @author Jerry Springer, Hung Vu
 * @version 12 Dec 2020
 */
// Ignore checkstyle member name error. Checkstyle done, post Sprint 3, Hung Vu.
public class ChatRoomInfo implements Serializable {

  /**
   * Name of group (or to be more precise, a room name).
   */
  private final String mRoomName;

  /**
   * Room id.
   */
  private final int mRoomId;

  /**
   * Last message in a room.
   */
  private final String mMessage;

  /**
   * Time of lastest message.
   */
  private final String mTime;

  /**
   * True if the room is for group chat, false for Direct message room.
   */
  private final boolean mIsGroupChat;

  /**
   * Constructor.
   *
   * @param roomName name of chat room
   * @param roomId if of chat room
   * @param message last message of the room
   * @param time time of latest message
   * @param groupFlag indicate whether this is a group or dm room
   */
  public ChatRoomInfo(String roomName, int roomId, String message, String time, boolean groupFlag) {
    this.mRoomName = roomName;
    this.mRoomId = roomId;
    this.mMessage = message;
    this.mTime = time;
    this.mIsGroupChat = groupFlag;
  }

  /**
   * Turn a JSON string to its respective ChatRoomInfo object.
   *
   * @param roomAsJson the json string
   * @return a ChatRoomInfo representation of the JSON string
   * @throws JSONException handle by caller
   */
  public static ChatRoomInfo createFromJsonString(final String roomAsJson) throws JSONException {
    final JSONObject group = new JSONObject(roomAsJson);
    return new ChatRoomInfo(
            group.getString("name"),
            group.getInt("chatid"),
            group.getString("message"),
            group.getString("timestamp"),
            group.getInt("direct") == 1 ? false : true);
  }

  /**
   * Get room id.
   *
   * @return room id
   */
  public int getRoomId() {
    return mRoomId;
  }

  /**
   * Get room name.
   *
   * @return room name
   */
  public String getRoomName() {
    return this.mRoomName;
  }

  /**
   * Get latest message.
   *
   * @return latest message in a room
   */
  public String getMessage() {
    return this.mMessage;
  }

  /**
   * Get time of the latest message.
   *
   * @return time of latest message
   */
  public String getTime() {
    return this.mTime;
  }

  /**
   * Indicate whether a room is for group chat or not.
   *
   * @return true for group chat, false for DM chat
   */
  public boolean isGroupChat() {
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
    if (other instanceof ChatRoomInfo) {
      result = mRoomId == ((ChatRoomInfo) other).mRoomId;
    }

    return result;
  }

  @Override
  public String toString() { // For testing only - Hung Vu.
    return "ChatGroupInfo{"
            + "mGroupName='" + mRoomName + '\''
            + ", mGroupId=" + mRoomId
            + ", mMessage='" + mMessage + '\''
            + ", mTime='" + mTime + '\''
            + '}';
  }
}
