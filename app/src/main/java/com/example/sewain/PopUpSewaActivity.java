package com.example.sewain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sewain.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PopUpSewaActivity extends AppCompatActivity  implements User.UserValueListener {

    Button tambah, kurang, sewa, batal;
    TextView namaKendaraan, namaPemilik, jenisKendaraan, lamaSewa, hargaSewa, totalSewa, namaPenyewa;
    ImageView fotoKtp;
    int lama=1;
    int total;
    String HargaSewa, urlKTP, idUser, key;

    FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private DatabaseReference mReference;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_sewa);
        progress = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setIndeterminate(false);
        progress.show();

        tambah = findViewById(R.id.btn_tambah_sewa);
        kurang = findViewById(R.id.btn_kurang_sewa);
        sewa = findViewById(R.id.btnSewa);
        batal = findViewById(R.id.btnBatal);
        lamaSewa = findViewById(R.id.txt_lama_pinjam_sewa);
        hargaSewa = findViewById(R.id.txtHargaSewa);
        totalSewa = findViewById(R.id.txt_total_sewa);
        namaKendaraan = findViewById(R.id.txtNamaKendaraanSewa);
        namaPemilik = findViewById(R.id.txtNamaPemilikKendaraanSewa);
        jenisKendaraan = findViewById(R.id.txtJenisKendaraanSewa);
        namaPenyewa = findViewById(R.id.txtNamaPenyewa);
        fotoKtp = findViewById(R.id.foto_ktp_sewa);

        User.getCurrentUser(this);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
            }
        });
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lama += 1;
                lamaSewa.setText(lama + "");
                int harga = Integer.parseInt(HargaSewa);
                total = lama * harga;
                totalSewa.setText("Total Harga Sewa : Rp." + total);
            }
        });
        kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lama > 1) {
                    lama -= 1;
                    lamaSewa.setText(lama + "");
                    int harga = Integer.parseInt(HargaSewa);
                    total = lama * harga;
                    totalSewa.setText("Total Harga Sewa : Rp." + total);
                }
            }
        });

        Intent intent = getIntent();
        HargaSewa = intent.getStringExtra("hargaSewa");
        final String IdKendaraan = intent.getStringExtra("idKendaraan");
        final String NamaPemilik = intent.getStringExtra("namaPemilik");
        final String JenisKendaraan = intent.getStringExtra("jenisKendaraan");
        final String NamaKendaraan = intent.getStringExtra("namaKendaraan");

        totalSewa.setText("Total Harga Sewa : Rp." + HargaSewa);
        hargaSewa.setText("Harga Sewa: "+HargaSewa);
        namaKendaraan.setText("Nama Kendaraan: "+NamaKendaraan);
        namaPemilik.setText("Pemilik: "+NamaPemilik);
        jenisKendaraan.setText("Jenis Kendaraan: "+JenisKendaraan);


        sewa.setOnClickListener(view -> {

            if (urlKTP.equalsIgnoreCase("Default")) {
                Toast.makeText(this, "Silahkan lengkapi data foto KTP anda!", Toast.LENGTH_SHORT).show();
            }
            else {
                InsertFirebase(IdKendaraan, idUser, lama, total);
            }
        });



    }

    @Override
    public User onUserChange(User user) {
        urlKTP = user.geturlKTP();
        idUser = user.getId();
        namaPenyewa.setText("Nama Penyewa: "+user.getUsername());
        Glide.with(this).load(user.geturlKTP()).into(fotoKtp);
        progress.hide();
        progress.dismiss();
        return user;
    }


    private void InsertFirebase(String idKendaraan, String idUser, int lama,
                                int total) {

        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setIndeterminate(false);
        progress.show();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        key = FirebaseDatabase.getInstance().getReference("Sewa Kendaraan").push().getKey();
        mReference = FirebaseDatabase.getInstance().getReference("Sewa Kendaraan").child(key);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("UserId Penyewa", idUser);
        hashMap.put("Id Kendaraan", idKendaraan);
        hashMap.put("Lama Sewa", String.valueOf(lama));
        hashMap.put("Total Harga Sewa", String.valueOf(total));

        mReference.setValue(hashMap).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(this, "Anda berhasil menyewa kendaraan ! " , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Anda gagal menyewa kendaraan ! " , Toast.LENGTH_SHORT).show();
            }
            progress.hide();
            progress.dismiss();
        });
    }

}
