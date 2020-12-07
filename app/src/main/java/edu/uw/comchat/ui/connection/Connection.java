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

  /**
   * The email for the Connection.
   */
  private final String mEmail;
  /**
   * The first name for the Connection.
   */
  private final String mFirstName;
  /**
   * The last name for the Connection.
   */
  private final String mLastName;


  /**
   * Constructor method for a connection that takes the email, first name,
   * and last name.
   * @param email The connections email
   * @param fName The connections first name
   * @param lName The connections last name
   */
  public Connection(String email, String fName, String lName) {
    mEmail = email;
    mFirstName = fName;
    mLastName = lName;
  }
  public String getEmail() {
    return mEmail;
  }

  /**
   * Accessor method for the Connections first name
   * @return The First Name
   */
  public String getFirstName() {
    return mFirstName;
  }

  /**
   * Accessor method for the Connections last name
   * @return The Last Name
   */
  public String getLastName() {
    return mLastName;
  }

  @Override
  public String toString() {
    return "Email: " + mEmail + ", First Name: " + mFirstName + ", Last Name: " + mLastName;
  }


}
