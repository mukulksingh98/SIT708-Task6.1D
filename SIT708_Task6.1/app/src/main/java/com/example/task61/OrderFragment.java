package com.example.task61;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class OrderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference myRef;
    String receiverName;

    String selectedTime;

    String location;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, null);


        Bundle bundle = new Bundle();

        //set up variables for items in view
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        Spinner timeSpinner = view.findViewById(R.id.timeSpinner);
        EditText receiverNameEditText = view.findViewById(R.id.receiverNameEditText);
        Button nextButton = view.findViewById(R.id.nextButton);
        Button locationButton = view.findViewById(R.id.locationButton);

        //connect to the firebase user authenticator
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        myRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());


        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //get users name from firebase database
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Log.d("firebase name", String.valueOf(task.getResult().child("fullName").getValue()));
                    receiverName = String.valueOf(task.getResult().child("fullName").getValue());
                    receiverNameEditText.setText(receiverName);

                }
            }
        });

        //create adapter for array to show variables from strings.xml file
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.timeArray, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        //setting spinners to use OnItemSelectedListener functions
        timeSpinner.setOnItemSelectedListener(this);

        //open google maps to allow user to select current location
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment locationFragment = new LocationFragment();

                activity.getSupportFragmentManager().beginTransaction()
                        /*.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )*/
                        .replace(R.id.locationFrameLayout, locationFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        //when next button is selected, send data to new fragment to continue order process
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.v("name", receiverName);

                getParentFragmentManager().setFragmentResultListener("location", getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // We use a String here, but any type that can be put in a Bundle is supported
                        location = bundle.getString("location");
                        // Do something with the result
                        Log.v("location rec", location);
                    }
                });

                bundle.putString("name", receiverName);
                int month = datePicker.getMonth()+1;
                String date = String.valueOf(datePicker.getDayOfMonth()+"/"+String.valueOf(month)+"/"+datePicker.getYear());

                //add one week to date for return date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                final Calendar calender = Calendar.getInstance();
                calender.set(Calendar.YEAR,datePicker.getYear());
                calender.set(Calendar.MONTH,datePicker.getMonth());
                calender.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                //Log.v("date", "vale" + sdf.format(calender.getTime()));
                calender.add(Calendar.DAY_OF_MONTH,7);
                Date oneWeek = calender.getTime();
                //Log.v("date in 1 week", "vale " + sdf.format(oneWeek));
                String oneWeekDate = sdf.format(oneWeek);

                bundle.putString("date",date);
                //Log.v("Date", String.valueOf(datePicker.getDayOfMonth())+"/"+datePicker.getMonth()+"/"+datePicker.getYear());
                bundle.putString("time", selectedTime);
                bundle.putString("oneWeekDate", oneWeekDate);
                bundle.putString("location", location);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment orderConfirmFragment = new OrderConfirmFragment();

                orderConfirmFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        /*.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )*/
                        .replace(R.id.orderConfirmFrameLayout, orderConfirmFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //when the item is selected on the spinner, store the result for later use when button is pressed
        if (adapterView.getId() == R.id.timeSpinner) {
            selectedTime = adapterView.getItemAtPosition(i).toString();
            Log.v("Time Spinner", selectedTime);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}