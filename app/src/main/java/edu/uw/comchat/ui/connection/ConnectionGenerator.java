package edu.uw.comchat.ui.connection;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create dummy connections for development.
 * Actual connections will be obtained from our web server and possibly
 * local storage.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class ConnectionGenerator {

  private static final Connection[] CONNECTIONS;
  public static final int COUNT = 20;

  static {
    CONNECTIONS = new Connection[20];
    for (int i = 0; i < COUNT; i++) {
      CONNECTIONS[i] = new Connection
              .Builder(
              "Person " + (i + 1),
              "Person " + (i + 1))
              .build();
    }
  }

  public static List<Connection> getConnections() {
    return Arrays.asList(CONNECTIONS);
  }

  private ConnectionGenerator() {
  }
  // Checkstyle: Done - Hung Vu
}
