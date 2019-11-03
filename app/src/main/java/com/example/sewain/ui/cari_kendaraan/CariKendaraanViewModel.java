package com.example.sewain.ui.cari_kendaraan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CariKendaraanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CariKendaraanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}