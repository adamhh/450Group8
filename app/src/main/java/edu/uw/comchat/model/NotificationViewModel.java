package edu.uw.comchat.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationViewModel extends ViewModel {
  private MutableLiveData<Map<LocalTime, List<String>>> mNotificationModel;

  public NotificationViewModel(){
    mNotificationModel = new MutableLiveData<>();
    mNotificationModel.setValue(new HashMap<>());
  }
  // msg 0, sender 1
  public Map<LocalTime, List<String>> getNotificationMap(){
    return mNotificationModel.getValue();
  }

  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super Map<LocalTime, List<String>>> observer) {
    mNotificationModel.observe(owner, observer);
  }
}
