package edu.uw.comchat.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NotificationViewModel extends ViewModel {
  private MutableLiveData<TreeMap<LocalDateTime, List<String>>> mNotificationModel;

  public NotificationViewModel(){
    mNotificationModel = new MutableLiveData<>();
    mNotificationModel.setValue(new TreeMap<>());
  }
  // msg 0, sender 1
  public TreeMap<LocalDateTime, List<String>> getNotificationMap(){
    return mNotificationModel.getValue();
  }

  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super TreeMap<LocalDateTime, List<String>>> observer) {
    mNotificationModel.observe(owner, observer);
  }
}
