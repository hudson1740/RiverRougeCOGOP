package com.cogop.riverrougecogop; // Or adjust as per your preference

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedVerseViewModel extends ViewModel {

    private final MutableLiveData<String> currentVerse = new MutableLiveData<>();

    public LiveData<String> getCurrentVerse() {
        return currentVerse;
    }

    public void setVerse(String verse) {
        currentVerse.setValue(verse);
    }
}