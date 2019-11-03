package com.example.sewain.ui.tambah_kendaraan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TambahKendaraanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TambahKendaraanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}