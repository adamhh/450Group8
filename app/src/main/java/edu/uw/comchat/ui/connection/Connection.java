package edu.uw.comchat.ui.connection;

import java.io.Serializable;

/**
 * Represent a connection between two people.
 *
 * @author Jerry Springer
 * @version 3 November 2020
 */
// Ignore checkstyle member name error.
public class Connection implements Serializable {

  private final String mPerson1;
  private final String mPerson2;

  /**
   * Helper class for building connections.
   *
   * @author Jerry Springer
   */
  public static class Builder {

    private final String mPerson1;
    private final String mPerson2;

    /**
     * Constructs a new builder.
     *
     * @param person1 the first person in the connection.
     * @param person2 the second person in the connection.
     */
    public Builder(String person1, String person2) {
      this.mPerson1 = person1;
      this.mPerson2 = person2;
    }

    public Connection build() {
      return new Connection(this);
    }
  }

  private Connection(Builder builder) {
    this.mPerson1 = builder.mPerson1;
    this.mPerson2 = builder.mPerson2;
  }

  public String getPerson1() {
    return mPerson1;
  }

  public String getPerson2() {
    return mPerson2;
  }
  // Checkstyle: Done - Hung Vu
}
