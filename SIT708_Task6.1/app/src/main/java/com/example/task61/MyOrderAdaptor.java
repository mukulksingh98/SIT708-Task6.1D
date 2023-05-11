package com.example.task61;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyOrderAdaptor extends RecyclerView.Adapter<MyOrderAdaptor.MyViewHolder> {

    Context context;
    ArrayList<OrderModel> orderList;

    public MyOrderAdaptor(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderrecyclerview_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.titleTextView.setText("Order Placed On " + orderList.get(position).getPickupDate());
        Glide.with(holder.truckImageView).load("https://firebasestorage.googleapis.com/v0/b/task6-1.appspot.com/o/truck1.jpg?alt=media&token=6ceec3d6-2d54-4338-867c-f23a22474351").into(holder.truckImageView);
        holder.descriptionTextView.setText("Order for " + orderList.get(position).getType() + " sharing.");
        //holder.goodsTextView.setText("Transported Goods: " + orderList.get(position).getGoods());
        holder.returnTextView.setText("Return Date: " + orderList.get(position).getDropoffDate());

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //use orderList.get(position) for all the data and pass to fragment
                //fragment is OrderDetailsFragment, fragment_order_details

                Bundle bundle = new Bundle();

                bundle.putString("pickupDate", orderList.get(holder.getAdapterPosition()).getPickupDate());
                bundle.putString("pickupTime", orderList.get(holder.getAdapterPosition()).getPickupTime());
                bundle.putString("dropoffDate", orderList.get(holder.getAdapterPosition()).getDropoffDate());
                bundle.putString("dropoffTime", orderList.get(holder.getAdapterPosition()).getDropoffTime());
                bundle.putString("sender", orderList.get(holder.getAdapterPosition()).getSenderUsername());
                bundle.putString("weight", orderList.get(holder.getAdapterPosition()).getWeight());
                bundle.putString("type", orderList.get(holder.getAdapterPosition()).getType());
                bundle.putString("width", orderList.get(holder.getAdapterPosition()).getWidth());
                bundle.putString("height", orderList.get(holder.getAdapterPosition()).getHeight());
                bundle.putString("length", orderList.get(holder.getAdapterPosition()).getLength());
                bundle.putString("quantity", orderList.get(holder.getAdapterPosition()).getQuantity());
                bundle.putString("goods", orderList.get(holder.getAdapterPosition()).getGoods());
                bundle.putString("location", orderList.get(holder.getAdapterPosition()).getLocation());

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment orderDetailsFragment = new OrderDetailsFragment();

                orderDetailsFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        /*.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )*/
                        .replace(R.id.selectedOrderFrameLayout, orderDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, descriptionTextView, returnTextView, goodsTextView;
        ImageView truckImageView;
        ImageView shareImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            returnTextView = itemView.findViewById(R.id.returnTextView);
            //goodsTextView = itemView.findViewById(R.id.goodsTextView);
            truckImageView = itemView.findViewById(R.id.truckImageView);
            shareImageView = itemView.findViewById(R.id.shareImageView);

        }
    }

}
