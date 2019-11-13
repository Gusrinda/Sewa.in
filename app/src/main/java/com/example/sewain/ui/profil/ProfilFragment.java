package com.example.sewain.ui.profil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sewain.LoginActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.sewain.LoginActivity;
import com.example.sewain.MainActivity;
import com.example.sewain.Model.User;
import com.example.sewain.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class ProfilFragment extends Fragment implements User.UserValueListener {

    private ProfilViewModel profilViewModel;
    ImageView GambarProfile;
    TextView Nama, Email, ID;
    Button Logout;
    View root;
    GoogleSignInClient mGoogleSignInClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        root = inflater.inflate(R.layout.fragment_profil, container, false);


        GambarProfile = root.findViewById(R.id.img_profile);
        Nama = root.findViewById(R.id.txt_Nama);
        Email = root.findViewById(R.id.txt_Email);
        ID = root.findViewById(R.id.txt_ID);
        Logout = root.findViewById(R.id.btn_keluar);


        User.getCurrentUser(this);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_keluar) {
                    AuthUI.getInstance()
                            .signOut(getContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);//
                                    Toast.makeText(getContext(), "Anda berhasil keluar Fergusso!", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Nama.setText(personName);
            Email.setText(personEmail);
            ID.setText(personId);
            Glide.with(this).load(String.valueOf(personPhoto)).into(GambarProfile);
        }
        
        return root;
    }

    @Override
    public User onUserChange(User user) {
        Nama.setText(user.getUsername());
        Email.setText(user.getEmail());
        ID.setText(user.getId());
        return user;
    }
}