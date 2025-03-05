package com.example.sistemhidroponik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyItemList;

    public HistoryAdapter(List<HistoryItem> historyItemList) {
        this.historyItemList = historyItemList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem historyItem = historyItemList.get(position);

        holder.textViewDate.setText("Date: " + historyItem.getDate());
        holder.textViewTime.setText("Time: " + historyItem.getTime());
        holder.textViewKelarutan.setText("Kelarutan: " + historyItem.getKelarutan());
        holder.textViewKelembapan.setText("Kelembapan: " + historyItem.getKelembapan());
        holder.textViewPH.setText("pH: " + historyItem.getPH());
        holder.textViewSuhuAir.setText("Suhu Air: " + historyItem.getSuhuAir());
        holder.textViewSuhuLingkungan.setText("Suhu Lingkungan: " + historyItem.getSuhuLingkungan());

        if (historyItem.getPHdown() != null) {
            holder.textViewPHdown.setVisibility(View.VISIBLE);
            holder.textViewPHdown.setText("pHdown: " + historyItem.getPHdown());
        } else {
            holder.textViewPHdown.setVisibility(View.GONE);
        }

        if (historyItem.getPHup() != null) {
            holder.textViewPHup.setVisibility(View.VISIBLE);
            holder.textViewPHup.setText("pHup: " + historyItem.getPHup());
        } else {
            holder.textViewPHup.setVisibility(View.GONE);
        }

        if (historyItem.getPompaNutrisi() != null) {
            holder.textViewPompaNutrisi.setVisibility(View.VISIBLE);
            holder.textViewPompaNutrisi.setText("Pompa Nutrisi: " + historyItem.getPompaNutrisi());
        } else {
            holder.textViewPompaNutrisi.setVisibility(View.GONE);
        }

        if (historyItem.getWaterHeater() != null) {
            holder.textViewWaterHeater.setVisibility(View.VISIBLE);
            holder.textViewWaterHeater.setText("Water Heater: " + historyItem.getWaterHeater());
        } else {
            holder.textViewWaterHeater.setVisibility(View.GONE);
        }

        if (historyItem.getFan() != null) {
            holder.textViewFan.setVisibility(View.VISIBLE);
            holder.textViewFan.setText("Fan: " + historyItem.getFan());
        } else {
            holder.textViewFan.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewTime, textViewKelarutan, textViewKelembapan, textViewPH, textViewSuhuAir, textViewSuhuLingkungan, textViewPHdown, textViewPHup, textViewPompaNutrisi, textViewWaterHeater, textViewFan;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewKelarutan = itemView.findViewById(R.id.textViewKelarutan);
            textViewKelembapan = itemView.findViewById(R.id.textViewKelembapan);
            textViewPH = itemView.findViewById(R.id.textViewPH);
            textViewSuhuAir = itemView.findViewById(R.id.textViewSuhuAir);
            textViewSuhuLingkungan = itemView.findViewById(R.id.textViewSuhuLingkungan);
            textViewPHdown = itemView.findViewById(R.id.textViewPHdown);
            textViewPHup = itemView.findViewById(R.id.textViewPHup);
            textViewPompaNutrisi = itemView.findViewById(R.id.textViewPompaNutrisi);
            textViewWaterHeater = itemView.findViewById(R.id.textViewWaterHeater);
            textViewFan = itemView.findViewById(R.id.textViewFan);
        }
    }
}
