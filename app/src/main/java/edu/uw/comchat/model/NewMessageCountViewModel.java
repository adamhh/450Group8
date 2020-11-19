package edu.uw.comchat.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * This class is from lab 5 - Hung Vu.
 */
public class NewMessageCountViewModel extends ViewModel {
  private MutableLiveData<Integer> mNewMessageCount;

  public NewMessageCountViewModel() {
    mNewMessageCount = new MutableLiveData<>();
    mNewMessageCount.setValue(0);
  }

  public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                      @NonNull Observer<? super Integer> observer) {
    mNewMessageCount.observe(owner, observer);
  }

  public void increment() {
    mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
  }

  public void reset() {
    mNewMessageCount.setValue(0);
  }

  // Checkstyle done, sprint 2 - Hung Vu. Ignore member name errors if they exist.
}
