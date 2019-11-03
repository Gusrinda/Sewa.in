package com.example.sewain.ui.pesan;

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

public class PesanFragment extends Fragment {

    private PesanViewModel pesanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pesanViewModel =
                ViewModelProviders.of(this).get(PesanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pesan, container, false);
//        final TextView textView = root.findViewById(R.id.text_share);
        pesanViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }
}