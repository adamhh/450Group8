package edu.uw.comchat.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.uw.comchat.ui.connection.Connection;

/**
 * This class store information of notification. It is possible to retrieve
 * all received notification in a life time of an application.
 *
 * @author Hung Vu
 */
public class NotificationViewModel extends ViewModel {
  /**
   * Store information about latest chat message.
   */
  private MutableLiveData<TreeMap<String, List<String>>> mMsgNotificationModel;

  /**
   * Store information about latest incoming request.
   */
  private MutableLiveData<List<Connection>> mIncomingConnectionRequestModel;
  /**
   * Constructor.
   */
  public NotificationViewModel() {
    mMsgNotificationModel = new MutableLiveData<>();
    mMsgNotificationModel.setValue(new TreeMap<>());
    mIncomingConnectionRequestModel = new MutableLiveData<>();
    mIncomingConnectionRequestModel.setValue(new ArrayList<>());
  }

  /**
   * Update the view model with latest notification.
   *
   * @param time         store the timestamp of moment receiving a notification
   * @param msgAndSender store the information of latest message and its respective sender.
   *                     Index 0 stores the message. Index 1 stores the sender email.
   */
  public void updateChatNotificationData(LocalDateTime time, List<String> msgAndSender) {
    TreeMap<String, List<String>> incomingNotification = mMsgNotificationModel.getValue();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    String formattedTime = time.format(formatter);

    incomingNotification.put(formattedTime, msgAndSender);
    mMsgNotificationModel.setValue(incomingNotification);

  }

  public void updateConnectionNotificationData(List<Connection> incomingRequest){
    mIncomingConnectionRequestModel.setValue(incomingRequest);
  }

  /**
   * Return a list containing information of latest incoming connection request.
   *
   * @return the list of string which holds all information about incoming request
   *          Index 0 store the email
   *          Index 1 store the first name
   *          Index 2 store the last name
   *          Index 3 stores the id of avatar (in String)
   */
  public List<String> getLatestConnectionRequest() {
    int lastIndex = mIncomingConnectionRequestModel.getValue().size() - 1;
    Connection lastIncomingConnection = mIncomingConnectionRequestModel.getValue().get(lastIndex);
    List<String> incomingRequestInfo = new ArrayList<>();
    incomingRequestInfo.add(lastIncomingConnection.getEmail());
    incomingRequestInfo.add(lastIncomingConnection.getFirstName());
    incomingRequestInfo.add(lastIncomingConnection.getLastName());
    incomingRequestInfo.add(String.valueOf(lastIncomingConnection.getAvatar(
            lastIncomingConnection.getEmail()
    )));
    return incomingRequestInfo;
  }

  /**
   * Get timestamp of the latest notification.
   * @return a string indicates hours and minute receiving the notification
   */
  public String getLatestNotificationTime() {
    return mMsgNotificationModel.getValue().lastEntry().getKey().substring(6, 11);
  }

  /**
   * Get date of the latest notification.
   * @return a string indicates month and date receiving the notification
   */
  public String getLatestNotificationDate() {
    return mMsgNotificationModel.getValue().lastEntry().getKey().substring(0, 5);
  }

  /**
   * Get the recently received message.
   * @return return the latest message
   */
  public String getLatestNotificationMessage() {
    return mMsgNotificationModel.getValue().lastEntry().getValue().get(0);
  }

  /**
   * Get the latest message sender.
   * @return an email of the sender
   */
  public String getLatestNotificationSender() {
    return mMsgNotificationModel.getValue().lastEntry().getValue().get(1);
  }

  public void addChatResponseObserver(@NonNull LifecycleOwner owner,
                                      @NonNull Observer<? super TreeMap<String, List<String>>> observer) {
    mMsgNotificationModel.observe(owner, observer);
  }
  public void addConnectionResponseObserver(@NonNull LifecycleOwner owner,
                                      @NonNull Observer<? super List<Connection>> observer) {
    mIncomingConnectionRequestModel.observe(owner, observer);
  }
}
