package com.example.sewain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sewain.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RegisterNextActivity extends AppCompatActivity implements User.UserValueListener{

    StorageReference mStorage;
    ImageView imgAkun, imgKtp;
    Button ambilAkun, ambilKTP, btnTambah, btnSkip;
    TextView txtMasuk;
    Uri photoUriAkun = null;
    Uri photoUriKtp = null;
    ProgressDialog progress;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    String UID, NamaUser, Email;

    private static final int PICK_PHOTO = 1;
    private static final int PICK_CAMERA = 100;
    private int check_foto = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next);

        progress = new ProgressDialog(this);

        imgAkun = findViewById(R.id.imgAkun);
        imgKtp = findViewById(R.id.imgKTP);
        ambilAkun = findViewById(R.id.ambilFotoAkun);
        ambilKTP = findViewById(R.id.ambilFotoKtp);
        btnTambah = findViewById(R.id.btnTambahData);
        btnSkip = findViewById(R.id.btnSkip);
        txtMasuk = findViewById(R.id.txtMasuk);

        mStorage = FirebaseStorage.getInstance().getReference();


        User.getCurrentUser(this);


        ambilAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_foto = 1;
                getPhotoFrom();
            }
        });

        ambilKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_foto = 2;
                getPhotoFrom();
            }
        });


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterNextActivity.this, MainActivity.class));
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (photoUriAkun != null && photoUriKtp != null) {
                    uploadFotoFirebase();
                    startActivity(new Intent(RegisterNextActivity.this, MainActivity.class));
                } else if (photoUriAkun == null && photoUriKtp != null) {
                    Toast.makeText(RegisterNextActivity.this, "Tolong isi Gambar akun !", Toast.LENGTH_SHORT).show();
                } else if (photoUriAkun != null && photoUriKtp == null) {
                    Toast.makeText(RegisterNextActivity.this, "Tolong isi Gambar KTP !", Toast.LENGTH_SHORT).show();
                } else if (photoUriAkun == null && photoUriKtp == null) {
                    Toast.makeText(RegisterNextActivity.this, "Tolong isi kedua field atau tekan tombol SKIP !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void uploadFotoFirebase() {
        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setIndeterminate(false);
        progress.show();

        final StorageReference filePathAkun = mStorage.child("images/user/"+UID+"/PP-"+NamaUser);
        final StorageReference filePathKtp = mStorage.child("images/user/"+UID+"/KTP-"+NamaUser);


        filePathKtp.putFile(photoUriKtp);
        filePathAkun.putFile(photoUriAkun).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePathAkun.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUri = uri;
                        String urlGambarAkun = downloadUri.toString();

                        mReference = FirebaseDatabase.getInstance().getReference("Users").child(UID);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", UID);
                        hashMap.put("username", NamaUser);
                        hashMap.put("email", Email);
                        hashMap.put("urlPhoto", urlGambarAkun);

                        mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(RegisterNextActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                progress.hide();
                            }
                        });
                    }
                });
            }
        });
    }

    private void getPhotoFrom() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO && check_foto == 1){
            if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                photoUriAkun = data.getData();
                try {
                    Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUriAkun);
                    Picasso.get().load(photoUriAkun).fit().centerCrop().into(imgAkun);
//                    imgAkun.setImageBitmap(bitmapImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PICK_PHOTO && check_foto == 2){
            if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                photoUriKtp = data.getData();
                try {
                    Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUriKtp);
//                    imgKtp.setImageBitmap(bitmapImg);
                    Picasso.get().load(photoUriKtp).fit().centerCrop().into(imgKtp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public User onUserChange(User user) {
        UID = user.getId();
        NamaUser = user.getUsername();
        Email = user.getEmail();
        return null;
    }
}
