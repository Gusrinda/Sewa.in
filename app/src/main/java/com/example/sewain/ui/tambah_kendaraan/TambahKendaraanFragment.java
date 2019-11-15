package com.example.sewain.ui.tambah_kendaraan;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sewain.ui.cari_kendaraan.CariKendaraanFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.sewain.R;

import java.util.HashMap;

public class TambahKendaraanFragment extends Fragment {

    private TambahKendaraanViewModel tambahKendaraanViewModel;
    private DatabaseReference mReference;
    private EditText nama, jenis, harga, lokasi, deskripsi;
    private ImageView foto;
    private Button simpan, batal;
    private FirebaseAuth mAuth;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private int REQUEST_IMAGE_CAPTURE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tambahKendaraanViewModel =
                ViewModelProviders.of(this).get(TambahKendaraanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tambah_kendaraan, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        REQUEST_IMAGE_CAPTURE = 1;
        nama = root.findViewById(R.id.edtNamaKendaraan);
        jenis = root.findViewById(R.id.edtJenisKendaraan);
        harga = root.findViewById(R.id.edtHargaSewa);
        lokasi = root.findViewById(R.id.edtLokasiKendaraan);
        deskripsi = root.findViewById(R.id.edtDeskripsi);
        foto = root.findViewById(R.id.imgFotoKendaraan);
        simpan = root.findViewById(R.id.btnSimpan);
        batal = root.findViewById(R.id.btnBatal);

        final TextView textView = root.findViewById(R.id.text_tambah_kendaraan);
        tambahKendaraanViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata();
            }
        });

        return root;
    }

    private void savedata() {
        String namaKendaraan = nama.getText().toString();
        String jenisKendaraan = jenis.getText().toString();
        String hargaSewa = harga.getText().toString();
        String lokasiKendaran = lokasi.getText().toString();
        String deskripsiKendaraan = deskripsi.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nama_kendaraan", namaKendaraan);
        hashMap.put("jenis", jenisKendaraan);
        hashMap.put("harga", hargaSewa);
        hashMap.put("lokasi", lokasiKendaran);
        hashMap.put("deskripsi", deskripsiKendaraan);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String userid = firebaseUser.getUid();
        mReference = FirebaseDatabase.getInstance().getReference("Kendaraan").child(userid);

        mReference.setValue(hashMap).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {


                CariKendaraanFragment fragment = new CariKendaraanFragment();
                fragmentTransaction.add(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

                Toast.makeText(getContext(), "Berhasil gan!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Gagal gan!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}