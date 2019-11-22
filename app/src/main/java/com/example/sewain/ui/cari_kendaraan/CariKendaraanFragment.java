package com.example.sewain.ui.cari_kendaraan;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sewain.Adapter.KendaraanAdapter;
import com.example.sewain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CariKendaraanFragment extends Fragment {

    private CariKendaraanViewModel cariKendaraanViewModel;
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    private TextView namaKendaraan, hargaSewa, lokasi;
    private EditText alamatKendaraan;
    private TextView latti, longi;
    private Button btnKamera, btnTandai, btnSimpan;
    private ImageView gambar;
    private String userid;
    private Bitmap foto;
    private AppCompatSpinner spinnerJenis;

    FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private ProgressDialog mProgress;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference myRef;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cari_kendaraan, container, false);
        final TextView textView = root.findViewById(R.id.text_cari_kendaraan);

        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        namaKendaraan = root.findViewById(R.id.txtNamaKendaraan);
        hargaSewa = root.findViewById(R.id.txtHargwaSewa);
        lokasi = root.findViewById(R.id.txtLokasi);
        mProgress = new ProgressDialog(this.getContext());

        mProgress.setMessage("Please Wait");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setIndeterminate(false);
        mProgress.show();
        ArrayList<CariKendaraanViewModel> listKendaraan = new ArrayList<>();
        // Write a message to the database
        myRef = FirebaseDatabase.getInstance().getReference("Kendaraan");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mNamaKendaraan, mHargaSewa, mLokasi, idUser, idKendaraan, foto, mJenis, mDeskripsi, mLatit, mLongit;
                String url="https://firebasestorage.googleapis.com/v0/b/sewain-fbed3.appspot.com/o/Kendaraan";//Retrieved url as mentioned above
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mNamaKendaraan = (String) snapshot.child("Nama Kendaraan").getValue();
                    mHargaSewa = (String) snapshot.child("Harga Sewa").getValue();
                    mLokasi = (String) snapshot.child("Lokasi Kendaraan").getValue();
                    idUser = (String) snapshot.child("User Id").getValue();
                    idKendaraan = (String) snapshot.child("Kendaraan Id").getValue();
                    mJenis = (String) snapshot.child("Jenis Kendaraan").getValue();
                    mDeskripsi = (String) snapshot.child("Deskripsi Kendaraan").getValue();
                    mLatit = (String) snapshot.child("Latittude").getValue();
                    mLongit = (String) snapshot.child("LOngitude").getValue();
                    foto = url+"%2F"+idUser+"%2F"+idKendaraan+"?alt=media";
                    listKendaraan.add(new CariKendaraanViewModel(idUser, idKendaraan, mNamaKendaraan, mJenis, mHargaSewa, mLatit, mLongit, mDeskripsi, mLokasi, foto));
                }
                mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                if (getActivity()!=null){
                    mAdapter = new KendaraanAdapter(getContext(), listKendaraan);
                }
                mRecyclerView.setAdapter(mAdapter);
                mProgress.hide();
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return root;
    }
}