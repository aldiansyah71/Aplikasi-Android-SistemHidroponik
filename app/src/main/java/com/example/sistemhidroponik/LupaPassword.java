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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LupaPassword extends AppCompatActivity {
    private EditText etEmail;
    private Button btnVerifEmail;
    private ImageView icBacktologin;
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

        setContentView(R.layout.activity_lupapassword);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi DatabaseReference
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sistem-hidroponik2-default-rtdb.firebaseio.com/");

        etEmail = findViewById(R.id.etEmail);
        btnVerifEmail = findViewById(R.id.btnVerifEmail);
        icBacktologin = findViewById(R.id.icBacktologin);

        // Set listener untuk tombol verifikasi email
        btnVerifEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    // Tampilkan pesan jika alamat email kosong
                    Toast.makeText(LupaPassword.this, "Masukkan alamat email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kirim email reset password ke alamat email yang dimasukkan
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Pesan jika pengiriman email berhasil
                                Toast.makeText(LupaPassword.this, "Silakan periksa email Anda untuk reset password", Toast.LENGTH_SHORT).show();
                            } else {
                                // Pesan jika terjadi kesalahan saat mengirim email
                                Toast.makeText(LupaPassword.this, "Gagal mengirim email reset password", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        icBacktologin.setOnClickListener(view -> {
            Intent backLupapassword = new Intent(getApplicationContext(), Login.class);
            startActivity(backLupapassword);
        });
    }
}
