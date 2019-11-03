package com.example.sewain.ui.cari_kendaraan;

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

public class CariKendaraanFragment extends Fragment {

    private CariKendaraanViewModel cariKendaraanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cariKendaraanViewModel =
                ViewModelProviders.of(this).get(CariKendaraanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cari_kendaraan, container, false);
        final TextView textView = root.findViewById(R.id.text_cari_kendaraan);
        cariKendaraanViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}