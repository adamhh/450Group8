package edu.uw.comchat.ui.chat;

import java.util.Arrays;
import java.util.List;

import edu.uw.comchat.ui.chat.chatroom.ChatMessage;

/**
 * This class is used to create dummy chats for development.
 * Actual chats will be obtained from our web server and possibly
 * local storage.
 *
 * @author Jerry Springer
 * @version 11 November 2020
 */
// Ignore checkstyle member name error.
public final class ChatMessageGenerator {

  private static final ChatMessage[] CHATS;
  public static final int COUNT = 20;

  static {
    CHATS = new ChatMessage[COUNT];
    for (int i = 0; i < CHATS.length; i++) {
      CHATS[i] = new ChatMessage((i + 1),
              "Message",
              "Sender " + (i + 1),
              "Time");
    }
  }

  public static List<ChatMessage> getChatList() {
    return Arrays.asList(CHATS);
  }

  private ChatMessageGenerator() {
  }
  // Checkstyle: Done - Hung Vu
}
