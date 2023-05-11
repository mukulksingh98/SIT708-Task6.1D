package com.example.task61;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.task61.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderConfirmFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String name, time, date, selectedGoods, selectedVehicle, oneWeekDate, location;

    ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");

    public OrderConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderConfirmFragment newInstance(String param1, String param2) {
        OrderConfirmFragment fragment = new OrderConfirmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get items from OrderFragment
            name = getArguments().getString("name");
            date = getArguments().getString("date");
            time = getArguments().getString("time");
            oneWeekDate = getArguments().getString("oneWeekDate");
            location = getArguments().getString("location");
            //Log.v("received", name + " " + date + " " + time + " " + oneWeekDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_confirm, null);

        //connect to the firebase user authenticator
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Spinner goodsSpinner = view.findViewById(R.id.goodsSpinner);
        Spinner vehicleSpinner = view.findViewById(R.id.vehicleSpinner);
        Button createButton = view.findViewById(R.id.createButton);
        EditText weightEditText = view.findViewById(R.id.weightEditText);
        EditText widthEditText = view.findViewById(R.id.widthEditText);
        EditText lengthEditText = view.findViewById(R.id.lengthEditText);
        EditText heightEditText = view.findViewById(R.id.heightEditText);
        EditText quantityEditText = view.findViewById(R.id.quantityEditText);

        //create adapter for array to show variables from strings.xml file
        ArrayAdapter<CharSequence> goodsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.goodsArray, android.R.layout.simple_spinner_item);
        goodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goodsSpinner.setAdapter(goodsAdapter);

        //setting spinners to use OnItemSelectedListener functions
        goodsSpinner.setOnItemSelectedListener(this);

        //create adapter for array to show variables from strings.xml file
        ArrayAdapter<CharSequence> vehicleAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.vehicleArray, android.R.layout.simple_spinner_item);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(vehicleAdapter);

        //setting spinners to use OnItemSelectedListener functions
        vehicleSpinner.setOnItemSelectedListener(this);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get user id and save order to firebase database
                //firebase directory as follows "id">"orders">"order"
                String id = firebaseAuth.getCurrentUser().getUid();

                OrderModel orderModel = new OrderModel(date, time, oneWeekDate, "17:00", "username",
                        weightEditText.getText().toString(), selectedVehicle, widthEditText.getText().toString(),
                        heightEditText.getText().toString(), lengthEditText.getText().toString(),
                        quantityEditText.getText().toString(), selectedGoods, location);
                myRef.child(id).child("orders").child(String.valueOf(System.currentTimeMillis())).setValue(orderModel);

                Toast.makeText(getContext(), "Order Confirmed.",
                        Toast.LENGTH_SHORT).show();

                //close all fragments and go back to activity
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //when the item is selected on the spinner, store the result for later use when button is pressed
        if (adapterView.getId() == R.id.goodsSpinner) {
            selectedGoods = adapterView.getItemAtPosition(i).toString();
            Log.v("Goods Spinner", selectedGoods);
        }
        if (adapterView.getId() == R.id.vehicleSpinner) {
            selectedVehicle = adapterView.getItemAtPosition(i).toString();
            Log.v("Vehicle Spinner", selectedVehicle);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}