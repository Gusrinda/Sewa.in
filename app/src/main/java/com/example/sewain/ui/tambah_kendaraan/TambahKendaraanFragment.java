package com.example.sewain.ui.tambah_kendaraan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sewain.MainActivity;
import com.example.sewain.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.graphics.Bitmap.Config.RGB_565;


public class TambahKendaraanFragment extends Fragment {
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    private TextInputEditText namaKendaraan, hargaSewa, deskripsiKendaraan;
    private EditText alamatKendaraan;
    private TextView latti, longi;
    private Button btnKamera, btnTandai, btnSimpan;
    private ImageView gambar;
    private String userid;
    private Bitmap foto;
    private String key;
    private AppCompatSpinner spinnerJenis;

    FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private DatabaseReference mReference;
    private ProgressDialog mProgress;

    private Geocoder geocoder;
    List<Address> addresses;

    String Latitude = "777";
    String Longitude = "777";
    private TambahKendaraanViewModel tambahKendaraanViewModel;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tambahKendaraanViewModel = ViewModelProviders.of(this).get(TambahKendaraanViewModel.class);

        View root = inflater.inflate(R.layout.fragment_tambah_kendaraan, container, false);
        final TextView textView = root.findViewById(R.id.text_tambah_kendaraan);

        namaKendaraan = root.findViewById(R.id.edtNamaKendaraan);
        hargaSewa = root.findViewById(R.id.edtHargaSewa);
        deskripsiKendaraan = root.findViewById(R.id.edtDeskripsi);
        alamatKendaraan = root.findViewById(R.id.edtAlamatKendaraan);
        spinnerJenis = root.findViewById(R.id.SpinnerJenis);

        latti = root.findViewById(R.id.latti);
        longi = root.findViewById(R.id.longi);

        btnKamera = root.findViewById(R.id.ambilFoto);
        btnTandai = root.findViewById(R.id.tandaiLokasi);
        btnSimpan = root.findViewById(R.id.btnSimpan);

        gambar = root.findViewById(R.id.fotoKendaraan);


        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this.getContext());

        tambahKendaraanViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }

        btnKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        btnTandai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                    onGps();
                }
                else {

                    getLocation();
                    getAlamat();
                }
            }
        });


        btnSimpan.setOnClickListener(view -> {

            String txt_NamaKendaraan = namaKendaraan.getText().toString();
            String txt_JenisKendaraan = spinnerJenis.getSelectedItem().toString();
            String txt_HargaSewa = hargaSewa.getText().toString();
            String txt_AlamatKendaraan = alamatKendaraan.getText().toString();
            String txt_Latti = latti.getText().toString();
            String txt_Longi = longi.getText().toString();
            String txt_DeskripsiKendaraan = deskripsiKendaraan.getText().toString();
            Bitmap img_Kendaraan = ((BitmapDrawable)gambar.getDrawable()).getBitmap();
            Bitmap emptyBitmap = Bitmap.createBitmap(gambar.getWidth(), gambar.getHeight(),RGB_565);

            if (TextUtils.isEmpty(txt_NamaKendaraan) || TextUtils.isEmpty(txt_HargaSewa) ||
                    TextUtils.isEmpty(txt_AlamatKendaraan) || TextUtils.isEmpty(txt_Latti) ||
                    TextUtils.isEmpty(txt_Longi) || TextUtils.isEmpty(txt_DeskripsiKendaraan) ||
                    img_Kendaraan.sameAs(emptyBitmap) || TextUtils.isEmpty(txt_JenisKendaraan) ||
                    foto==null) {
                Toast.makeText(getContext(), "Lengkapi semua field Fergusso!", Toast.LENGTH_SHORT).show();
            }
            else {
                InsertFirebase(txt_NamaKendaraan, txt_JenisKendaraan, txt_HargaSewa, txt_AlamatKendaraan, txt_Latti,
                        txt_Longi, txt_DeskripsiKendaraan, img_Kendaraan);
            }
        });

        return root;
    }

    private void InsertFirebase(String txt_namaKendaraan, String txt_jenisKendaraan, String txt_hargaSewa,
                                String txt_alamatKendaraan, String txt_latti, String txt_longi,
                                String txt_deskripsiKendaraan, Bitmap img_kendaraan) {

            mProgress.setMessage("Please Wait");
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setCancelable(false);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setIndeterminate(false);
            mProgress.show();



        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        userid = firebaseUser.getUid();
        key = FirebaseDatabase.getInstance().getReference("Kendaraan").push().getKey();
        mReference = FirebaseDatabase.getInstance().getReference("Kendaraan").child(key);

        String urlFotoKendaraan = "https://firebasestorage.googleapis.com/v0/b/sewain-fbed3.appspot.com/o/Kendaraan%2F"+userid+"%2F"+key+"?alt=media";

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User Id", userid);
        hashMap.put("Nama Kendaraan", txt_namaKendaraan);
        hashMap.put("Jenis Kendaraan", txt_jenisKendaraan);
        hashMap.put("Harga Sewa", txt_hargaSewa);
        hashMap.put("Lokasi Kendaraan", txt_alamatKendaraan);
        hashMap.put("Lattitude", txt_latti);
        hashMap.put("Longitude", txt_longi);
        hashMap.put("Deskripsi Kendaraan", txt_deskripsiKendaraan);
        hashMap.put("Kendaraan Id", key);
        hashMap.put("Foto Kendaraan", urlFotoKendaraan);

        mReference.setValue(hashMap).addOnCompleteListener(task1 -> {
        if (task1.isSuccessful()) {
            uploadImage(foto);
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this.getContext(), "Sukses menambah kendaraan ! " , Toast.LENGTH_SHORT).show();
         }else{
            Toast.makeText(this.getContext(), "Gagal menambah kendaraan ! " , Toast.LENGTH_SHORT).show();
        }
         mProgress.hide();
        mProgress.dismiss();
         });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            foto = (Bitmap) data.getExtras().get("data");
            gambar.setImageBitmap(foto);
        }
    }

    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sewain-fbed3.appspot.com");
        StorageReference imagesRef = storageRef.child("Kendaraan").child(userid).child(key);

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                // Do what you want
            }
        });
    }

    private void getAlamat() {
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        double dLat = Double.parseDouble(Latitude);
        double dLong = Double.parseDouble(Longitude);

        try {

            addresses = geocoder.getFromLocation(dLat, dLong, 1);

            String jalan  = addresses.get(0).getAddressLine(0);

            alamatKendaraan.setText(jalan);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {

        if(ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null){
                double lat = LocationGps.getLatitude();
                double longit = LocationGps.getLongitude();

                Latitude = String.valueOf(lat);
                Longitude = String.valueOf(longit);

                latti.setText( "Lat = " +  Latitude);
                longi.setText( "Long = " +  Longitude);
            } else if (LocationNetwork != null){
                double lat = LocationNetwork.getLatitude();
                double longit = LocationNetwork.getLongitude();

                Latitude = String.valueOf(lat);
                Longitude = String.valueOf(longit);

                latti.setText( "Lat = " +  Latitude);
                longi.setText( "Long = " +  Longitude);
            } else if (LocationPassive != null){
                double lat = LocationPassive.getLatitude();
                double longit = LocationPassive.getLongitude();

                Latitude = String.valueOf(lat);
                Longitude = String.valueOf(longit);

                latti.setText( "Lat = " +  Latitude);
                longi.setText( "Long = " +  Longitude);
            } else {
                Toast.makeText(this.getContext(), "Cant Get Location ! " , Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void onGps() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

    builder.setMessage("Enable GPS !").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    });

    final AlertDialog alertDialog=builder.create();
    alertDialog.show();

    }


}