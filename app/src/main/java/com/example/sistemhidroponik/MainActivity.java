package com.example.sistemhidroponik;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.WindowManager;
import com.example.sistemhidroponik.api.ApiClient;
import com.example.sistemhidroponik.api.BlynkApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.os.Handler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView icBack2;
    private TextView tvHistory ;
    private TextView tvTDS, tvKelembapan, tvPH, tvSuhuair, tvSuhuudara;
    private TextView tvStatusPHUp, tvStatusPHDown, tvStatusTDS, tvStatusAirPanas, tvStatusFan;
    private Button btnPHUp, btnPHDown, btnTDS, btnAirPanas, btnFan;
    private static final String AUTH_TOKEN = "x7wvlS6-87GefvZ7biKz6fG00Oji7Y7v";
    private static final long INTERVAL = 1000; // Interval in milliseconds
    private Handler handler;
    private DatabaseReference mDatabase;

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
        setContentView(R.layout.activity_main);

        // Inisialisasi Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tvTDS = findViewById(R.id.tvTDS);
        tvKelembapan = findViewById(R.id.tvKelembapan);
        tvPH = findViewById(R.id.tvPH);
        tvSuhuair = findViewById(R.id.tvSuhuair);
        tvSuhuudara = findViewById(R.id.tvSuhuudara);

        tvHistory = findViewById(R.id.tvHistory);

        tvStatusPHUp = findViewById(R.id.tvStatusPHUp);
        tvStatusPHDown = findViewById(R.id.tvStatusPHDown);
        tvStatusTDS = findViewById(R.id.tvStatusTDS);
        tvStatusAirPanas = findViewById(R.id.tvStatusAirPanas);
        tvStatusFan = findViewById(R.id.tvStatusFan); // Added for Fan status

        btnPHUp = findViewById(R.id.btnPHUp);
        btnPHDown = findViewById(R.id.btnPHDown);
        btnTDS = findViewById(R.id.btnTDS);
        btnAirPanas = findViewById(R.id.btnAirPanas);
        btnFan = findViewById(R.id.btnfan); // Added for Fan button

        icBack2 = findViewById(R.id.icBack2);

        tvHistory.setOnClickListener(view -> {
            Intent tvhistory = new Intent(getApplicationContext(), History.class);
            startActivity(tvhistory);
        });

        icBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backMainactivty = new Intent(getApplicationContext(), Login.class);
                startActivity(backMainactivty);
            }
        });

        handler = new Handler();

        btnPHUp.setOnClickListener(view -> toggleButtonState("V6", tvStatusPHUp));
        btnPHDown.setOnClickListener(view -> toggleButtonState("V5", tvStatusPHDown));
        btnTDS.setOnClickListener(view -> toggleButtonState("V7", tvStatusTDS));
        btnAirPanas.setOnClickListener(view -> toggleButtonState("V8", tvStatusAirPanas));
        btnFan.setOnClickListener(view -> toggleButtonState("V10", tvStatusFan)); // Added for Fan button

        // Start fetching data immediately
        fetchData();

        // Schedule fetching data with interval
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData();
                // Schedule the next execution after INTERVAL milliseconds
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any scheduled callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }

    private void fetchData() {
        BlynkApi blynkApi = ApiClient.getClient().create(BlynkApi.class);

        // Fetch data for pin V3 (Kelarutan)
        Call<String> callTDS = blynkApi.getPinValue(AUTH_TOKEN, "V3");
        callTDS.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    tvTDS.setText(pinValue);
                    saveDataToFirebase("kelarutan", pinValue);
                } else {
                    tvTDS.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvTDS.setText("Error: " + t.getMessage());
            }
        });

        // Fetch data for pin V1 (Kelembapan)
        Call<String> callKelembapan = blynkApi.getPinValue(AUTH_TOKEN, "V1");
        callKelembapan.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    tvKelembapan.setText(pinValue);
                    saveDataToFirebase("Kelembapan", pinValue);
                } else {
                    tvKelembapan.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvKelembapan.setText("Error: " + t.getMessage());
            }
        });

        // Fetch data for pin V4 (pH)
        Call<String> callPH = blynkApi.getPinValue(AUTH_TOKEN, "V4");
        callPH.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    tvPH.setText(pinValue);
                    saveDataToFirebase("pH", pinValue);
                } else {
                    tvPH.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvPH.setText("Error: " + t.getMessage());
            }
        });

        // Fetch data for pin V2 (Suhu Air)
        Call<String> callSuhuAir = blynkApi.getPinValue(AUTH_TOKEN, "V2");
        callSuhuAir.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    tvSuhuair.setText(pinValue);
                    saveDataToFirebase("SuhuAir", pinValue);
                } else {
                    tvSuhuair.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvSuhuair.setText("Error: " + t.getMessage());
            }
        });

        // Fetch data for pin V0 (Suhu Lingkungan)
        Call<String> callSuhuUdara = blynkApi.getPinValue(AUTH_TOKEN, "V0");
        callSuhuUdara.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    tvSuhuudara.setText(pinValue);
                    saveDataToFirebase("SuhuLingkungan", pinValue);
                } else {
                    tvSuhuudara.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvSuhuudara.setText("Error: " + t.getMessage());
            }
        });

        // Fetch data for pin V10 (Fan status)
        Call<String> callFanStatus = blynkApi.getPinValue(AUTH_TOKEN, "V10");
        callFanStatus.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    String fanStatus = pinValue.equals("1") ? "ON" : "OFF";
                    tvStatusFan.setText(fanStatus);
                    saveDataToFirebase("FanStatus", fanStatus);
                } else {
                    tvStatusFan.setText("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvStatusFan.setText("Error: " + t.getMessage());
            }
        });
    }

    private void toggleButtonState(String pin, TextView statusTextView) {
        BlynkApi blynkApi = ApiClient.getClient().create(BlynkApi.class);

        Call<String> call = blynkApi.getPinValue(AUTH_TOKEN, pin);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String pinValue = response.body();
                    String newStatus = pinValue.equals("1") ? "0" : "1";

                    // Send the new status to Blynk
                    Call<Void> updateCall = blynkApi.updatePinValue(AUTH_TOKEN, pin, newStatus);
                    updateCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                String status = newStatus.equals("0") ? "ON" : "OFF";
                                statusTextView.setText(status);
                                saveDataToFirebase(pinToActuatorName(pin), status);
                            } else {
                                statusTextView.setText("Failed to update status");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            statusTextView.setText("Error: " + t.getMessage());
                        }
                    });
                } else {
                    statusTextView.setText("Failed to fetch status");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                statusTextView.setText("Error: " + t.getMessage());
            }
        });
    }

    private String pinToActuatorName(String pin) {
        switch (pin) {
            case "V5":
                return "pHdown";
            case "V6":
                return "pHup";
            case "V7":
                return "PompaNutrisi";
            case "V8":
                return "WaterHeater";
            case "V10":
                return "Fan"; // Added for Fan
            default:
                return "Unknown";
        }
    }

    private void saveDataToFirebase(String key, String value) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        mDatabase.child("sistemhidroponik").child(date).child(timestamp).child(key).setValue(value);
    }
}
