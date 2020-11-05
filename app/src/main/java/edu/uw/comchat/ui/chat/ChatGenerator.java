package edu.uw.comchat.ui.chat;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create dummy chats for development.
 * Actual chats will be obtained from our web server and possibly
 * local storage.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public final class ChatGenerator {

  private static final Chat[] CHATS;
  public static final int COUNT = 20;

  static {
    CHATS = new Chat[COUNT];
    for (int i = 0; i < CHATS.length; i++) {
      CHATS[i] = new Chat
              .Builder("Sender " + (i + 1),
              "Receiver " + (i + 1),
              "Message")
              .build();
    }
  }

  public static List<Chat> getChatList() {
    return Arrays.asList(CHATS);
  }

  private ChatGenerator() {
  }
  // Checkstyle: Done - Hung Vu
}
