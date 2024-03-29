package com.example.sewain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    DatabaseReference mReference;
    Uri photoUriAkun = null;
    Uri photoUriKtp = null;
    EditText mNama, mEmail;
    ImageView mFoto, mKtp;
    FirebaseAuth mAuth;
    String username, email, idUser;
    Button btnFoto, btnKtp, btnSimpan, btnBatal;
    ProgressDialog progress;

    private static final int PICK_PHOTO = 1;
    private static final int PICK_CAMERA = 100;
    private int check_foto = 1;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mNama = findViewById(R.id.edtNama);
        mEmail = findViewById(R.id.edtEmail);
        mFoto = findViewById(R.id.imgFoto);
        mKtp = findViewById(R.id.imgKTPEdit);
        btnFoto = findViewById(R.id.ambilFotoAkun);
        btnKtp = findViewById(R.id.ambilFotoKtp);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatalEdt);
        progress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        idUser = intent.getStringExtra("idUser");
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(idUser);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = String.valueOf(dataSnapshot.child("username").getValue());
                email = String.valueOf(dataSnapshot.child("email").getValue());
                photoUriAkun = Uri.parse(String.valueOf(dataSnapshot.child("urlPhoto").getValue()));
                photoUriKtp = Uri.parse(String.valueOf(dataSnapshot.child("urlKTP").getValue()));

                mNama.setText(username);
                mEmail.setText(email);
                Glide.with(EditActivity.this).load(photoUriAkun).into(mFoto);
                Glide.with(EditActivity.this).load(photoUriKtp).into(mKtp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_foto = 1;
                getPhotoFrom();
            }
        });

        btnKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_foto = 2;
                getPhotoFrom();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(photoUriAkun.toString().equals("default") || photoUriKtp.toString().equals("default")){
                    Toast.makeText(EditActivity.this, "Lengkapi semua field Fergusso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditActivity.this, photoUriAkun.toString(), Toast.LENGTH_SHORT).show();
                    uploadFotoFirebase();
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        final StorageReference filePathAkun = mStorage.child("images/user/"+idUser+"/PP-"+username);
        final StorageReference filePathKtp = mStorage.child("images/user/"+idUser+"/KTP-"+username);


        filePathKtp.putFile(photoUriKtp);
        filePathAkun.putFile(photoUriAkun).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePathAkun.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUri = uri;
                        String urlGambarAkun = downloadUri.toString();
                        String urlKTP = "https://firebasestorage.googleapis.com/v0/b/sewain-fbed3.appspot.com/o/images%2Fuser%2F"+idUser+"%2FKTP-"+username+"?alt=media";

                        mReference = FirebaseDatabase.getInstance().getReference("Users").child(idUser);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", idUser);
                        hashMap.put("username", username);
                        hashMap.put("email", email);
                        hashMap.put("urlPhoto", urlGambarAkun);
                        hashMap.put("urlKTP", urlKTP);

                        mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
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
//
//
//        String urlKTP = "https://firebasestorage.googleapis.com/v0/b/sewain-fbed3.appspot.com/o/images%2Fuser%2F"+idUser+"%2FKTP-"+username+"?alt=media";
//        String urlPhoto = "https://firebasestorage.googleapis.com/v0/b/sewain-fbed3.appspot.com/o/images%2Fuser%2F"+idUser+"%2FPP-"+username+"?alt=media";
//
//        mReference = FirebaseDatabase.getInstance().getReference("Users").child(idUser);
//
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("id", idUser);
//        hashMap.put("username", username);
//        hashMap.put("email", email);
//        hashMap.put("urlPhoto", urlPhoto);
//        hashMap.put("urlKTP", urlKTP);
//
//        mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//                progress.hide();
//            }
//        });
    }

    private void getPhotoFrom() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO && check_foto == 1) {
            if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                photoUriAkun = data.getData();
                try {
                    Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUriAkun);
//                    Picasso.get().load(photoUriAkun).fit().centerCrop().into(mFoto);
                    Glide.with(EditActivity.this).load(photoUriAkun).into(mFoto);
//                    imgAkun.setImageBitmap(bitmapImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PICK_PHOTO && check_foto == 2) {
            if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                photoUriKtp = data.getData();
                try {
                    Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUriKtp);
//                    imgKtp.setImageBitmap(bitmapImg);
//                    Picasso.get().load(photoUriKtp).fit().centerCrop().into(mKtp);
                    Glide.with(EditActivity.this).load(photoUriKtp).into(mKtp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}