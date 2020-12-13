package edu.uw.comchat.ui.connection;

import androidx.annotation.Nullable;
import edu.uw.comchat.R;
import java.io.Serializable;

/**
 * Represent a connection between two people.
 *
 * @author Jerry Springer
 * <p>
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
   * The nick name for the Connection.
   */
  private final String mNickName;


  /**
   * Constructor method for a connection that takes the email, first name,
   * and last name.
   *
   * @param email The connections email
   * @param fName The connections first name
   * @param lName The connections last name
   */
  public Connection(String email, String fName, String lName, String nickName) {
    mEmail = email;
    mFirstName = fName;
    mLastName = lName;
    mNickName = nickName;
  }

  public String getEmail() {
    return mEmail;
  }

  /**
   * Accessor method for the Connections first name
   *
   * @return The First Name
   */
  public String getFirstName() {
    return mFirstName;
  }

  /**
   * Accessor method for the Connections last name
   *
   * @return The Last Name
   */
  public String getLastName() {
    return mLastName;
  }

  /**
   * Accessor method for the Connections last name
   *
   * @return The Nick Name
   */
  public String getNickName() {
    return mNickName;
  }

  /**
   * In place of shared preferences
   *
   * @return
   */
  public static int getAvatar(String email) {
    int avatar;
    if (email.contains("10")) {
      avatar = R.drawable.ic_avatar_red;
    } else if (email.contains("9")) {
      avatar = R.drawable.ic_avatar_black;
    } else if (email.contains("8")) {
      avatar = R.drawable.ic_avatar_forrest;
    } else if (email.contains("7")) {
      avatar = R.drawable.ic_avatar_light_blue;
    } else if (email.contains("6")) {
      avatar = R.drawable.ic_avatar_orange;
    } else if (email.contains("5")) {
      avatar = R.drawable.ic_avatar_purple;
    } else if (email.contains("4")) {
      avatar = R.drawable.ic_avatar_yellow;
    } else if (email.contains("3")) {
      avatar = R.drawable.ic_avatar_teal;
    } else if (email.contains("2")) {
      avatar = R.drawable.ic_avatar_green;
    } else {
      avatar = R.drawable.ic_avatar_blue;
    }
    return avatar;
  }

  @Override
  public String toString() {
    return "Email: " + mEmail + ", First Name: " + mFirstName + ", Last Name: " + mLastName;
  }

  // Help compare objects - Hung Vu
  @Override
  public boolean equals(@Nullable Object other) {

    if (other == this) {
      return true;
    }
    if (!(other instanceof Connection)) {
      return false;
    }

    Connection otherConnection = (Connection) other;

    return mEmail.equals(otherConnection.mEmail)
            && mLastName.equals(otherConnection.mLastName)
            && mFirstName.equals(otherConnection.mFirstName)
            && mNickName.equals(otherConnection.mNickName);
  }


}
