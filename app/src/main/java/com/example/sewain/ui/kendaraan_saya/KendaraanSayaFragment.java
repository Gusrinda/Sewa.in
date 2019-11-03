package com.example.sewain.ui.kendaraan_saya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sewain.R;

public class KendaraanSayaFragment extends Fragment {

    private KendaraanSayaViewModel kendaraanSayaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kendaraanSayaViewModel =
                ViewModelProviders.of(this).get(KendaraanSayaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_kendaraan_saya, container, false);
        final TextView textView = root.findViewById(R.id.text_kendaraan_saya);
        kendaraanSayaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}