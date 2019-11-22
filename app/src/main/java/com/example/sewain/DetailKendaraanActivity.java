package com.example.sewain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailKendaraanActivity extends AppCompatActivity {

    private Context mContext;
    TextView namaKendaraan, jenisKendaraan, hargaSewa, deskripsiKendaraan, namaPemilik;
    ImageView fotoKendaraan;
    Button btnPilih, btnPesan;
    int total, harga;
    String HargaSewa, IdPemilik, IdKendaraan, FotoKendaraan, DeskripsiKendaraan, NamaKendaraan, JenisKendaraan;
    private DatabaseReference myRef;
    private RelativeLayout mRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kendaraan);

        namaKendaraan = findViewById(R.id.txt_nama_detail);
        jenisKendaraan = findViewById(R.id.txt_jenis_detail);
        hargaSewa = findViewById(R.id.txt_harga_detail);
        deskripsiKendaraan = findViewById(R.id.txt_deskripsi_detail);
        namaPemilik = findViewById(R.id.txt_pemilik_detail);
        fotoKendaraan = findViewById(R.id.foto_kendaraan_detail);
        btnPilih = findViewById(R.id.btnPilih);
        btnPesan = findViewById(R.id.btnKirimPesan);
        mContext = getApplicationContext();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailKendaraanActivity.this, PopUpSewaActivity.class);
                intent.putExtra("hargaSewa", HargaSewa);
                intent.putExtra("idKendaraan", IdKendaraan);
                intent.putExtra("namaPemilik", namaPemilik.getText().toString());
                intent.putExtra("jenisKendaraan", jenisKendaraan.getText().toString());
                intent.putExtra("namaKendaraan", namaKendaraan.getText().toString());
                startActivity(intent);
//
//                // Initialize a new instance of LayoutInflater service
//                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//                // Inflate the custom layout/view
//                View customView = inflater.inflate(R.layout.activity_pop_up_sewa,null);
//                mPopupWindow = new PopupWindow(
//                        customView,
//                        LayoutParams.WRAP_CONTENT,
//                        LayoutParams.WRAP_CONTENT
//                );
//
//                // Set an elevation value for popup window
//                // Call requires API level 21
//                if(Build.VERSION.SDK_INT>=21){
//                    mPopupWindow.setElevation(5.0f);
//                }
//                mRelativeLayout.setAlpha(0.5f);
//
//                Button batal = (Button) customView.findViewById(R.id.btnBatal);
//                batal.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mRelativeLayout.setAlpha(1f);
//                        mPopupWindow.dismiss();
//                    }
//                });
//                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IdPemilik!=null){
                    Intent intent = new Intent(DetailKendaraanActivity.this, PesanActivity.class);
                    intent.putExtra("userid", IdPemilik);
                    startActivity(intent);
                }else{
                    Snackbar.make(findViewById(R.id.layout_login), "Kendaraan ini tidak bisa dipesan!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        NamaKendaraan = intent.getStringExtra("namaKendaraan");
        JenisKendaraan = intent.getStringExtra("jenisKendaraan");
        HargaSewa = intent.getStringExtra("hargaSewa");
        DeskripsiKendaraan = intent.getStringExtra("deskripsiKendaraan");
        FotoKendaraan = intent.getStringExtra("fotoKendaraan");
        IdPemilik = intent.getStringExtra("userId");
        IdKendaraan = intent.getStringExtra("kendaraanId");
        harga = Integer.parseInt(HargaSewa);
        namaKendaraan.setText(NamaKendaraan);
        jenisKendaraan.setText("Jenis Kendaraan : "+JenisKendaraan);
        hargaSewa.setText("Harga Sewa : "+HargaSewa);
        deskripsiKendaraan.setText("Deskripsi : "+DeskripsiKendaraan);
        Glide.with(this).load(FotoKendaraan).into(fotoKendaraan);

        myRef = FirebaseDatabase.getInstance().getReference("Users").child(IdPemilik);
//        System.out.println("ID "+IdPemilik);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String pemilik = String.valueOf(dataSnapshot.child("username").getValue());
                    namaPemilik.setText("Pemilik : "+pemilik);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
