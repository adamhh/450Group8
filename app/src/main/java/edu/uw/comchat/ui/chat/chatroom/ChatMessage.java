package edu.uw.comchat.ui.chat.chatroom;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a message in a group.
 *
 * @author Jerry Springer, Hung Vu
 * @version 10 Dec 2020
 */
// Ignore checkstyle member name error. Checkstyle done, Sprint 3, Hung Vu.
public final class ChatMessage implements Serializable {
  /**
   * Id of chat message.
   */
  private int mMessageId;

  /**
   * The message.
   */
  private final String mMessage;

  /**
   * The sender's email.
   */
  private final String mSender;

  /**
   * The time stamp of email.
   */
  private final String mTimeStamp;

  /**
   * Constructor.
   *
   * @param messageId the id of message
   * @param message   the message
   * @param sender    the sender's email
   * @param timeStamp the time stamp of email
   */
  public ChatMessage(int messageId, String message, String sender, String timeStamp) {
    mMessageId = messageId;
    mMessage = message;
    mSender = sender;
    mTimeStamp = timeStamp;
  }

  /**
   * Turn JSON string into a Chat Message object.
   *
   * @param cmAsJson a JSON string to be converted.
   * @return a ChatMessage representation of JSON string
   * @throws JSONException handle by caller
   */
  public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
    final JSONObject msg = new JSONObject(cmAsJson);
    return new ChatMessage(msg.getInt("messageid"),
            msg.getString("message"),
            msg.getString("email"),
            msg.getString("timestamp"));
  }

  /**
   * Return message id.
   *
   * @return message id
   */
  public int getMessageId() {
    return mMessageId;
  }

  /**
   * Return message.
   *
   * @return message
   */
  public String getMessage() {
    return mMessage;
  }

  /**
   * Return sender's email.
   *
   * @return sender's email
   */
  public String getSender() {
    return mSender;
  }

  /**
   * Return timestamp of a message. For now, it is not used since the application use
   * application time instead. There is a margin of difference, but negligible. Might
   * change to this method later on.
   *
   * @return a timestamp of a message
   */
  public String getTimeStamp() {
    return mTimeStamp;
  }

  /**
   * Provides equality solely based on MessageId.
   *
   * @param other the other object to check for equality
   * @return true if other message ID matches this message ID, false otherwise
   */
  @Override
  public boolean equals(@Nullable Object other) {
    boolean result = false;
    if (other instanceof ChatMessage) {
      result = mMessageId == ((ChatMessage) other).mMessageId;
    }
    return result;
  }

  @Override
  public int hashCode() {
    return Objects.hash(mMessageId, mMessage, mSender);
  }


}
