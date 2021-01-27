package com.abhishek.mandi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.mandi.Modal.Record;
import com.abhishek.mandi.R;

import java.util.List;

public class MandiAdapter extends RecyclerView.Adapter<MandiAdapter.MandiViewHolder> {

    private Context context;
    private List<Record> mandiList;

    public MandiAdapter(Context context, List<Record> mandiList) {
        this.context = context;
        this.mandiList = mandiList;
    }

    @NonNull
    @Override
    public MandiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MandiViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_row, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MandiViewHolder holder, int position) {
        Record actor = mandiList.get(position);
        holder.commodity.setText(actor.getCommodity());
        holder.modal_price.setText("\u20B9" + actor.getModalPrice());
        holder.market.setText(actor.getMarket() + ", " + actor.getDistrict() + ", " + actor.getState() + ".");
        //holder.district.setText(actor.getDistrict());
        //holder.state.setText(actor.getState());
        holder.min_price.setText("\u20B9" + actor.getMinPrice());
        holder.max_price.setText("\u20B9" + actor.getMaxPrice());
        holder.date.setText(actor.getArrivalDate());

    }

    public void getAllMandi(List<Record> mandiList) {
        this.mandiList = mandiList;
    }

    @Override
    public int getItemCount() {
        return mandiList.size();
    }

    public static class MandiViewHolder extends RecyclerView.ViewHolder {
        public TextView commodity, modal_price, market, district, state, min_price, max_price, date;

        public MandiViewHolder(@NonNull View itemView) {
            super(itemView);
            commodity = itemView.findViewById(R.id.commodity);
            modal_price = itemView.findViewById(R.id.modal_price);
            market = itemView.findViewById(R.id.market);
            //district=itemView.findViewById(R.id.district);
            //state=itemView.findViewById(R.id.state);
            min_price = itemView.findViewById(R.id.min_price);
            max_price = itemView.findViewById(R.id.max_price);
            date = itemView.findViewById(R.id.date);
        }
    }
}
