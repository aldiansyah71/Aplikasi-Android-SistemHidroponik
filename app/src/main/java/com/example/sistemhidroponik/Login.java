package com.example.sistemhidroponik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister, btnLogin;
    private TextView tvLupapassword;
    private ImageView icBack;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @SuppressLint({"ObsoleteSdkInt", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_login);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvLupapassword = findViewById(R.id.tvLupapassword);
        icBack = findViewById(R.id.icBack);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi DatabaseReference
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sistem-hidroponik2-default-rtdb.firebaseio.com/");

        tvLupapassword.setOnClickListener(view -> {
            Intent tvLupapassword = new Intent(getApplicationContext(), LupaPassword.class);
            startActivity(tvLupapassword);
        });

        icBack.setOnClickListener(view -> {
            Intent back = new Intent(getApplicationContext(), Started.class);
            startActivity(back);
        });

        btnRegister.setOnClickListener(view -> {
            Intent register = new Intent(getApplicationContext(), Register.class);
            startActivity(register);
        });

        btnLogin.setOnClickListener(v -> {
            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Masukkan Data Akun Anda", Toast.LENGTH_SHORT).show();
                return;
            }

            // Login dengan Firebase Authentication
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Login berhasil, periksa data di Realtime Database
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference userRef = database.child("users").child(user.getUid());

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        Intent masuk = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(masuk);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Data Belum Terdaftar", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
