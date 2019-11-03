package com.example.sewain.ui.kendaraan_saya;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KendaraanSayaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public KendaraanSayaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}