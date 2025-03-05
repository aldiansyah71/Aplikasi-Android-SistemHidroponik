package com.example.sistemhidroponik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class History extends AppCompatActivity {

    private ImageView icBackhistory;
    private CalendarView calendarView;
    private TextView textViewDate;
    private Button buttonRefresh;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private String startDate;
    private String endDate;
    private HistoryAdapter historyAdapter;
    private List<HistoryItem> historyItemList;
    private static final String TAG = "HistoryActivity";

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
        setContentView(R.layout.activity_history);

        // Inisialisasi view
        calendarView = findViewById(R.id.calendarView);
        textViewDate = findViewById(R.id.textViewDate);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        icBackhistory = findViewById(R.id.icBackhistory);
        recyclerView = findViewById(R.id.recyclerView);

        // Inisialisasi RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyItemList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyItemList);
        recyclerView.setAdapter(historyAdapter);

        icBackhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backdashboard = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backdashboard);
            }
        });

        // Inisialisasi Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Menangani pemilihan tanggal di CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month = month + 1; // Bulan dimulai dari 0
            String date = year + "-" + String.format(Locale.getDefault(), "%02d", month) + "-" + String.format(Locale.getDefault(), "%02d", dayOfMonth);
            if (startDate == null) {
                startDate = date;
                textViewDate.setText("Start Date: " + startDate);
            } else if (endDate == null) {
                endDate = date;
                textViewDate.setText("Start Date: " + startDate + "\nEnd Date: " + endDate);
            } else {
                startDate = date;
                endDate = null;
                textViewDate.setText("Start Date: " + startDate);
            }
        });

        // Menangani klik tombol Refresh
        buttonRefresh.setOnClickListener(view -> {
            if (startDate != null && endDate != null) {
                fetchDataForDateRange(startDate, endDate);
            } else {
                Toast.makeText(History.this, "Please select both start and end dates", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDataForDateRange(String start, String end) {
        mDatabase.child("sistemhidroponik").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyItemList.clear();
                boolean dataExists = false;

                // Loop through all dates in the range
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    long startDateMillis = sdf.parse(start).getTime();
                    long endDateMillis = sdf.parse(end).getTime();

                    for (long dateMillis = startDateMillis; dateMillis <= endDateMillis; dateMillis += 86400000) {
                        String date = sdf.format(dateMillis);
                        if (dataSnapshot.child(date).exists()) {
                            dataExists = true;
                            DataSnapshot dateSnapshot = dataSnapshot.child(date);

                            for (DataSnapshot snapshot : dateSnapshot.getChildren()) {
                                String time = snapshot.getKey();
                                String kelarutan = snapshot.child("kelarutan").getValue(String.class);
                                String kelembapan = snapshot.child("Kelembapan").getValue(String.class);
                                String pH = snapshot.child("pH").getValue(String.class);
                                String suhuAir = snapshot.child("SuhuAir").getValue(String.class);
                                String suhuLingkungan = snapshot.child("SuhuLingkungan").getValue(String.class);
                                String pHdown = snapshot.child("pHdown").getValue(String.class);
                                String pHup = snapshot.child("pHup").getValue(String.class);
                                String pompaNutrisi = snapshot.child("PompaNutrisi").getValue(String.class);
                                String waterHeater = snapshot.child("WaterHeater").getValue(String.class);
                                String fan = snapshot.child("Fan").getValue(String.class);

                                // Create a HistoryItem only with non-null values
                                HistoryItem historyItem = new HistoryItem(date, time, kelarutan, kelembapan, pH, suhuAir, suhuLingkungan, pHdown, pHup, pompaNutrisi, waterHeater, fan);
                                historyItemList.add(historyItem);
                            }
                        }
                    }

                    if (!dataExists) {
                        Toast.makeText(History.this, "No data available for the selected date range.", Toast.LENGTH_SHORT).show();
                    }

                    historyAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(History.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(History.this, "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
