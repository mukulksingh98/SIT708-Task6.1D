package com.example.task61;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailsFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String pickupDate, pickupTime, dropoffDate, dropoffTime, senderUsername, weight, type, width, height, length, quantity, goods, location;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailsFragment newInstance(String param1, String param2) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get items from the MyOrderAdaptor
            pickupDate = getArguments().getString("pickupDate");
            pickupTime = getArguments().getString("pickupTime");
            dropoffDate = getArguments().getString("dropoffDate");
            dropoffTime = getArguments().getString("dropoffTime");
            senderUsername = getArguments().getString("sender");
            weight = getArguments().getString("weight");
            type = getArguments().getString("type");
            height = getArguments().getString("height");
            length = getArguments().getString("length");
            quantity = getArguments().getString("quantity");
            goods = getArguments().getString("goods");
            location = getArguments().getString("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, null);

        //connect to the firebase user authenticator
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ImageView truckImageView = view.findViewById(R.id.truckImageView);
        TextView fromUsernameTextView = view.findViewById(R.id.fromUserNameTextView);
        TextView pickUpTextView = view.findViewById(R.id.pickUpTextView);
        TextView userTextView = view.findViewById(R.id.userTextView);
        TextView dropoffTextView = view.findViewById(R.id.dropoffTextView);
        TextView weightTextView = view.findViewById(R.id.weightTextView);
        TextView goodsTextView = view.findViewById(R.id.goodsTextView);
        TextView widthTextView = view.findViewById(R.id.widthTextView);
        TextView heightTextView = view.findViewById(R.id.heightTextView);
        TextView lengthTextView = view.findViewById(R.id.lengthTextView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);

        Glide.with(truckImageView).load("https://firebasestorage.googleapis.com/v0/b/task6-1.appspot.com/o/truck1.jpg?alt=media&token=6ceec3d6-2d54-4338-867c-f23a22474351").into(truckImageView);
        fromUsernameTextView.setText("From " + senderUsername);
        pickUpTextView.setText("Pick Up Time: " + pickupTime + " on " + pickupDate + " at " + location);
        userTextView.setText("To " + firebaseUser.getEmail());
        dropoffTextView.setText("Drop Off Time: " + dropoffTime + " on " + dropoffDate);
        weightTextView.setText("Weight: " + weight + "kgs");
        goodsTextView.setText("Type: " + goods);
        widthTextView.setText("Width: " + width + "m");
        heightTextView.setText("Height: " + height + "m");
        lengthTextView.setText("Length: " + length + "m");
        quantityTextView.setText("Quantity: " + quantity);

        return view;
    }
}