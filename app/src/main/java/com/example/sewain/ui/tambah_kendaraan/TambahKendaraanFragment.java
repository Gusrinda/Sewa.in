package com.example.sewain.ui.tambah_kendaraan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import com.example.sewain.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class TambahKendaraanFragment extends Fragment {
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    private TextInputEditText namaKendaraan, hargaSewa, deskripsiKendaraan;
    private EditText alamatKendaraan;
    private TextView latti, longi;
    private Button btnKamera, btnTandai;
    private ImageView gambar;
    private AppCompatSpinner spinnerJenis;


    private StorageReference mStorage;
    private DatabaseReference mReference;
    private ProgressDialog mProgress;

    private Geocoder geocoder;
    List<Address> addresses;

    String Latitude = "777";
    String Longitude = "777";

    private TambahKendaraanViewModel tambahKendaraanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tambahKendaraanViewModel = ViewModelProviders.of(this).get(TambahKendaraanViewModel.class);

        View root = inflater.inflate(R.layout.fragment_tambah_kendaraan, container, false);
        final TextView textView = root.findViewById(R.id.text_tambah_kendaraan);

        namaKendaraan = root.findViewById(R.id.edtNamaKendaraan);
        hargaSewa = root.findViewById(R.id.edtHargaSewa);
        deskripsiKendaraan = root.findViewById(R.id.edtDeskripsi);
        alamatKendaraan = root.findViewById(R.id.edtAlamatKendaraan);

        latti = root.findViewById(R.id.latti);
        longi = root.findViewById(R.id.longi);

        btnKamera = root.findViewById(R.id.ambilFoto);
        btnTandai = root.findViewById(R.id.tandaiLokasi);

        gambar = root.findViewById(R.id.fotoKendaraan);


        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this.getContext());

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


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



        return root;
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