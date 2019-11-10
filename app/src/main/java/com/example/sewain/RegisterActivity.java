package com.example.sewain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, noHp;
    Button btnDaftar;
    TextView txtMasuk;

    TextInputEditText password, repassword;

    FirebaseAuth mAuth;
    DatabaseReference mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.edtNama);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPass);
        repassword = findViewById(R.id.edtRePass);
        btnDaftar = findViewById(R.id.btnSelanjutnya);
        txtMasuk = findViewById(R.id.txtMasuk);

        mAuth = FirebaseAuth.getInstance();

        btnDaftar.setOnClickListener(view -> {

            String txt_Username = username.getText().toString();
            String txt_Email = email.getText().toString();
            String txt_Password = password.getText().toString();
            String txt_RePassword = repassword.getText().toString();

            if (TextUtils.isEmpty(txt_Username) || TextUtils.isEmpty(txt_Email) || TextUtils.isEmpty(txt_Password)) {
                Toast.makeText(RegisterActivity.this, "Lengkapi semua field Fergusso!", Toast.LENGTH_SHORT).show();
            } else if (!txt_Password.equals(txt_Password)){
                Toast.makeText(RegisterActivity.this, "Password harus sama ya bro!", Toast.LENGTH_SHORT).show();
            }
            else {
                register(txt_Username, txt_Email, txt_Password);
            }
        });


    }

    private void register(final String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        mReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username);

                        mReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Tidak bisa register dengan Email dan Password ini Fergusso!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
