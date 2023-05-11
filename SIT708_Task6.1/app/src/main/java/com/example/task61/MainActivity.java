package com.example.task61;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.task61.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends MenuActivity {


    ActivityMainBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference myRef;

    TruckAdaptor truckAdaptor;

    ArrayList<TruckModel> truckList;

    RecyclerView.LayoutManager truckLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Creating binding for using items from view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //connect to the firebase user authenticator
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //set up recyclerview for truck list
        truckList = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference("trucks");
        truckLayoutManager = new LinearLayoutManager(this);
        truckAdaptor = new TruckAdaptor(this, truckList);

        binding.trucksRecyclerView.setAdapter(truckAdaptor);
        binding.trucksRecyclerView.setLayoutManager(truckLayoutManager);

        //create listener for the data stored in 'trucks' array variable in database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                truckList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //for each set of data within 'trucks', add to the arraylist for display in recyclerview
                    TruckModel truck = dataSnapshot.getValue(TruckModel.class);
                    truckList.add(truck);
                }
                truckAdaptor.notifyDataSetChanged();

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