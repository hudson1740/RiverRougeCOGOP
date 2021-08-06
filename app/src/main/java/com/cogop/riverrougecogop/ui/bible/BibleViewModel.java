package com.cogop.riverrougecogop.ui.bible;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BibleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BibleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("BIBLE COGOP");
    }

    public LiveData<String> getText() {
        return mText;
    }
}