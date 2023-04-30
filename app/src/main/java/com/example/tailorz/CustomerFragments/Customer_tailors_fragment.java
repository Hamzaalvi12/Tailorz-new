package com.example.tailorz.CustomerFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.CustomerAdapters.CustomerDesignAdapter;
import com.example.tailorz.CustomerAdapters.CustomerTailorAdapter;
import com.example.tailorz.CustomerModels.CustomerTailorModel;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.example.tailorz.TailorModels.TailorDesignModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Customer_tailors_fragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference database;
    CustomerTailorAdapter customerTailorAdapter;
    ArrayList<CustomerTailorModel> list;
    Prefs prefs;

    public Customer_tailors_fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Customer_tailors_fragment newInstance(String param1, String param2) {
        return new Customer_tailors_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_customer_tailors_fragment, container, false);

        prefs = new Prefs(requireActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.Customer_Tailors_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        customerTailorAdapter = new CustomerTailorAdapter(getContext(), list);
        recyclerView.setAdapter(customerTailorAdapter);
        GetDataFromDatabase();

        return  view;
    }

    private void GetDataFromDatabase() {

        ArrayList<CustomerTailorModel> tempList = new ArrayList<>();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                database = FirebaseDatabase.getInstance().getReference("Users");

                database.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            CustomerTailorModel customerTailorModel = dataSnapshot.getValue(CustomerTailorModel.class);
                           String category = String.valueOf(dataSnapshot.child("category").getValue());
                           if(category.equals("Tailor")){
                               list.add(customerTailorModel);
                           }

                        }
                        customerTailorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        },300);
    }
}