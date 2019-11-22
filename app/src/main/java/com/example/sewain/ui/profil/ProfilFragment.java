package com.example.sewain.ui.profil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sewain.EditActivity;
import com.example.sewain.LoginActivity;
import com.example.sewain.Model.User;
import com.example.sewain.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class ProfilFragment extends Fragment implements User.UserValueListener {

    private ProfilViewModel profilViewModel;
    ImageView GambarProfile;
    TextView Nama, Email, ID;



    Button Logout, Edit;
    View root;
    ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        root = inflater.inflate(R.layout.fragment_profil, container, false);
        progress = new ProgressDialog(this.getContext());

        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setIndeterminate(false);
            progress.show();

        GambarProfile = root.findViewById(R.id.img_profile);
        Nama = root.findViewById(R.id.txt_Nama);
        Email = root.findViewById(R.id.txt_Email);
        ID = root.findViewById(R.id.txt_ID);
        Logout = root.findViewById(R.id.btn_keluar);
        Edit = root.findViewById(R.id.btn_edit);

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

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("idUser", ID.getText().toString());
                startActivity(intent);//
            }
        });
        return root;

    }

    @Override
    public User onUserChange(User user) {
        Nama.setText(user.getUsername());
        Email.setText(user.getEmail());
        ID.setText(user.getId());
        if(!user.getUrlPhoto().equalsIgnoreCase("default")){
            Picasso.get().load(user.getUrlPhoto()).fit().centerCrop().into(GambarProfile);
        }
        progress.hide();
        progress.dismiss();
        return user;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}