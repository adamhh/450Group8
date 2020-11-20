package edu.uw.comchat.ui.connection;

import java.io.Serializable;

/**
 * Represent a connection between two people.
 *
 * @author Jerry Springer
 *
 * Changed to reflect needs
 * @author Adam Hall
 * @version 17 November 2020
 */
// Ignore checkstyle member name error.
public class Connection implements Serializable {

  //private final String mName;
  private final String mEmail;
  //private final String mNickName;


//  /**
//   * Helper class for building connections.
//   *
//   * @author Jerry Springer
//   */
//  public static class Builder {
//
//    //private final String mName;
//    private final String mEmail;
//    //private final String mNickName;
//
//    /**
//     * Constructs a new builder.
//     *
//     * @param email The email
//     */
//    public Builder(String email) {
//      mEmail = email;
//    }
//
//    public Connection build() {
//      return new Connection(this);
//    }
//  }
//
//  private Connection(Builder builder) {
//    this.mEmail = builder.mEmail;
//  }
  public Connection(String email) {
    mEmail = email;
  }
  public String getEmail() {
    return mEmail;
  }


}
