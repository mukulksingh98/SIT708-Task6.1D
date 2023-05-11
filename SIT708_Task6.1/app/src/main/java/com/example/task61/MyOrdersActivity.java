package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61.databinding.ActivityMainBinding;
import com.example.task61.databinding.ActivityMyOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrdersActivity extends MenuActivity {


    ActivityMyOrdersBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference myRef;

    MyOrderAdaptor myOrderAdaptor;

    ArrayList<OrderModel> orderList;

    RecyclerView.LayoutManager myOrderLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Creating binding for using items from view
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //connect to the firebase user authenticator
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //set up recyclerview for truck list
        orderList = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("orders");
        myOrderLayoutManager = new LinearLayoutManager(this);
        myOrderAdaptor = new MyOrderAdaptor(this, orderList);

        binding.ordersRecyclerView.setAdapter(myOrderAdaptor);
        binding.ordersRecyclerView.setLayoutManager(myOrderLayoutManager);

        //create listener for the data stored in 'trucks' array variable in database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //for each set of data within 'trucks', add to the arraylist for display in recyclerview
                    Log.v("receive from database", dataSnapshot.getValue(OrderModel.class).toString());
                    OrderModel order = dataSnapshot.getValue(OrderModel.class);
                    orderList.add(order);
                }
                myOrderAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //go back to Login Activity if user is not logged in
        if(firebaseUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            //else show items on this activity
            //binding.userDetailsTextView.setText(firebaseUser.getUid());
        }

        binding.floatingActionButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment orderFragment = new OrderFragment();

                activity.getSupportFragmentManager().beginTransaction()
                        /*.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )*/
                        .replace(R.id.newOrderFrameLayout, orderFragment)
                        .addToBackStack(null)
                        .commit();


            }
        }));

    }
}