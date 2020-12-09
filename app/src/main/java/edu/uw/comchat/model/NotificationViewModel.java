package edu.uw.comchat.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

/**
 * This class store information of notification. It is possible to retrieve
 * all received notification in a life time of an application.
 *
 * @author Hung Vu
 */
public class NotificationViewModel extends ViewModel {
  private MutableLiveData<TreeMap<String, List<String>>> mNotificationModel;

  /**
   * Constructor.
   */
  public NotificationViewModel() {
    mNotificationModel = new MutableLiveData<>();
    mNotificationModel.setValue(new TreeMap<>());
  }

  /**
   * Update the view model with latest notification.
   *
   * @param time         store the timestamp of moment receiving a notification
   * @param msgAndSender store the information of latest message and its respective sender.
   *                     Index 0 stores the message. Index 1 stores the sender email.
   */
  public void updateNotificationData(LocalDateTime time, List<String> msgAndSender) {
    TreeMap<String, List<String>> incomingNotification = mNotificationModel.getValue();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    String formattedTime = time.format(formatter);

    incomingNotification.put(formattedTime, msgAndSender);
    mNotificationModel.setValue(incomingNotification);

  }

  /**
   * Get timestamp of the latest notification.
   * @return a string indicates hours and minute receiving the notification
   */
  public String getLatestNotificationTime() {
    return mNotificationModel.getValue().lastEntry().getKey().substring(0, 5);
  }

  /**
   * Get date of the latest notification.
   * @return a string indicates month and date receiving the notification
   */
  public String getLatestNotificationDate() {
    return mNotificationModel.getValue().lastEntry().getKey().substring(6, 11);
  }

  /**
   * Get the recently received message.
   * @return return the latest message
   */
  public String getLatestNotificationMessage() {
    return mNotificationModel.getValue().lastEntry().getValue().get(0);
  }

  /**
   * Get the latest message sender.
   * @return an email of the sender
   */
  public String getLatestNotificationSender() {
    return mNotificationModel.getValue().lastEntry().getValue().get(1);
  }

  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super TreeMap<String, List<String>>> observer) {
    mNotificationModel.observe(owner, observer);
  }
}
