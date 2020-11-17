package edu.uw.comchat.ui.chat;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represent a message in a group.
 *
 * @author Jerry Springer
 * @version 11 November 2020
 */
// Ignore checkstyle member name error.
public final class ChatMessage implements Serializable {

  private int mMessageId;
  private final String mMessage;
  private final String mSender;
  private final String mTimeStamp;

  public ChatMessage(int messageId, String message, String sender, String timeStamp) {
    mMessageId = messageId;
    mMessage = message;
    mSender = sender;
    mTimeStamp = timeStamp;
  }

  public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
    // TODO create message from a Json String
    return null;
  }

  public int getMessageId() {
    return mMessageId;
  }

  public String getMessage() {
    return mMessage;
  }

  public String getSender() {
    return mSender;
  }

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

  // Checkstyle: Done - Hung Vu
}
