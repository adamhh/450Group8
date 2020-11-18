package edu.uw.comchat.ui.chat.generatorfortesting;

import java.util.Arrays;
import java.util.List;

import edu.uw.comchat.ui.chat.ChatGroupInfo;

/**
 * Creates dummy groups for development purposes.
 *
 * @author Jerry Springer
 * @version 11 November 2020
 */
// Ignore checkstyle member name error.
// TODO Do we still need to keep this class? - Hung Vu
public final class GroupGenerator {

  private static final ChatGroupInfo[] GROUPS;
  public static final int COUNT = 20;

  static {
    GROUPS = new ChatGroupInfo[COUNT];
    for (int i = 0; i < GROUPS.length; i++) {
      GROUPS[i] = new ChatGroupInfo((i + 1), (i + 1));
    }
  }

  public static List<ChatGroupInfo> getGroupList() {
    return Arrays.asList(GROUPS);
  }

  private GroupGenerator() {
  }
}
