package edu.uw.comchat.ui.chat;

import java.util.Arrays;
import java.util.List;

/**
 * Creates dummy groups for development purposes.
 *
 * @author Jerry Springer
 * @version 11 November 2020
 */
// Ignore checkstyle member name error.
public final class GroupGenerator {

  private static final Group[] GROUPS;
  public static final int COUNT = 20;

  static {
    GROUPS = new Group[COUNT];
    for (int i = 0; i < GROUPS.length; i++) {
      GROUPS[i] = new Group((i + 1), (i + 1));
    }
  }

  public static List<Group> getGroupList() {
    return Arrays.asList(GROUPS);
  }

  private GroupGenerator() {
  }
}
