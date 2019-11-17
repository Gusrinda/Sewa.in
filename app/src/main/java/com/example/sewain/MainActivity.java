package com.example.sewain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sewain.Model.User;
import com.example.sewain.ui.cari_kendaraan.CariKendaraanFragment;
import com.example.sewain.ui.chat_list.chat_list;
import com.example.sewain.ui.pesan.PesanFragment;
import com.example.sewain.ui.profil.ProfilFragment;
import com.example.sewain.ui.tambah_kendaraan.TambahKendaraanFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, User.UserValueListener {
    private  BottomNavigationView nav;
    ImageView GambarProfile;
    TextView Nama, Email, ID;
    Button Logout;

    //hanya untuk menambahkan commit

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GambarProfile = findViewById(R.id.img_profile);
        Nama = findViewById(R.id.txt_Nama);
        Email = findViewById(R.id.txt_Email);
        ID = findViewById(R.id.txt_ID);
        nav = findViewById(R.id.nav_view);

        nav.setOnNavigationItemSelectedListener(this);
        nav.setSelectedItemId(R.id.navigation_cari);

        Logout = findViewById(R.id.btn_keluar);

//        User.getCurrentUser(this);
//
//        Logout.setOnClickListener(view -> {
//            switch (view.getId()) {
//
//                case R.id.btn_keluar:
//                    signOut();
//                    break;
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Berhasil Logout", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public User onUserChange(User user) {
        Nama.setText(user.getUsername());
        Email.setText(user.getEmail());
        ID.setText(user.getId());
        return user;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {

            case R.id.navigation_tambah:
                fragment = new TambahKendaraanFragment();
                break;

            case R.id.navigation_cari:
                fragment = new CariKendaraanFragment();
                break;

            case R.id.navigation_pesan:
                fragment = new PesanFragment();
                break;

            case R.id.navigation_akun:
                fragment = new ProfilFragment();
                break;


            case R.id.navigation_chat:
                fragment = new chat_list();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
