package com.example.sewain.ui.tambah_kendaraan;

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

public class TambahKendaraanFragment extends Fragment {

    private TambahKendaraanViewModel tambahKendaraanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tambahKendaraanViewModel =
                ViewModelProviders.of(this).get(TambahKendaraanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tambah_kendaraan, container, false);
        final TextView textView = root.findViewById(R.id.text_tambah_kendaraan);
        tambahKendaraanViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}