package com.example.task61;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TruckAdaptor extends RecyclerView.Adapter<TruckAdaptor.MyViewHolder> {

    Context context;
    ArrayList<TruckModel> truckList;

    public TruckAdaptor(Context context, ArrayList<TruckModel> truckList) {
        this.context = context;
        this.truckList = truckList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.truckrecyclerview_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //set text and images on view
        holder.titleTextView.setText(truckList.get(position).getModel());
        Glide.with(holder.truckImageView).load(truckList.get(position).getImageUrl()).into(holder.truckImageView);
        holder.descriptionTextView.setText(truckList.get(position).getDescription());
        holder.costTextView.setText("$" + truckList.get(position).getCost() + " Per Week");
        holder.capacityTextView.setText(truckList.get(position).getCapacity() + " Tonnes");

    }

    @Override
    public int getItemCount() {
        return truckList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, descriptionTextView, costTextView, capacityTextView;
        ImageView truckImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //set items in view to variables
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            capacityTextView = itemView.findViewById(R.id.capacityTextView);
            truckImageView = itemView.findViewById(R.id.truckImageView);

        }
    }

}
