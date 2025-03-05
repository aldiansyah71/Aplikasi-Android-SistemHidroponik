package com.example.sistemhidroponik;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private ImageView icBackDaftar;
    private TextView tvtoLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @SuppressLint({"MissingInflatedId", "ObsoleteSdkInt"})
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

        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        icBackDaftar = findViewById(R.id.icBackDaftar);
        tvtoLogin = findViewById(R.id.tvtoLogin);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi DatabaseReference
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sistem-hidroponik2-default-rtdb.firebaseio.com/");

        tvtoLogin.setOnClickListener(view -> {
            Intent tvLogin = new Intent(getApplicationContext(), Login.class);
            startActivity(tvLogin);
        });

        icBackDaftar.setOnClickListener(view -> {
            Intent backRegist = new Intent(getApplicationContext(), Login.class);
            startActivity(backRegist);
        });

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong!!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mendaftarkan pengguna dengan Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Registrasi berhasil, simpan data pengguna di Realtime Database
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference userRef = database.child("users").child(user.getUid());

                            userRef.child("username").setValue(username);
                            userRef.child("email").setValue(email);
                            userRef.child("password").setValue(password);  // Simpan password yang dienkripsi dalam implementasi nyata

                            Toast.makeText(getApplicationContext(), "Register Berhasil", Toast.LENGTH_SHORT).show();
                            Intent register = new Intent(getApplicationContext(), Login.class);
                            startActivity(register);
                            finish();
                        } else {
                            // Jika registrasi gagal, tampilkan pesan kesalahan
                            Toast.makeText(getApplicationContext(), "Register Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
