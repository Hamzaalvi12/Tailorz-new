package com.example.tailorz.CustomerFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.CustomerActivities.CompleteOrdersActivity;
import com.example.tailorz.CustomerActivities.HireMeActivity;
import com.example.tailorz.CustomerAdapters.OrdersAdapter;
import com.example.tailorz.CustomerModels.OrderModel;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.example.tailorz.TailorAdapters.TailorDesignAdapter;
import com.example.tailorz.TailorModels.TailorDesignModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Orders_fragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    OrdersAdapter ordersAdapter;
    ArrayList<OrderModel> list;
    Prefs prefs;


    public Orders_fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Orders_fragment newInstance(String param1, String param2) {
        return new Orders_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_fragment, container, false);
        prefs = new Prefs(requireActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.ordersRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(getContext(), list, new OrdersAdapter.ItemClickListener() {
            @Override
            public void onItemClick(OrderModel orderModel) {
                Intent intent = new Intent(view.getContext(), CompleteOrdersActivity.class);
                intent.putExtra("orderID", orderModel.getOrderID());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(ordersAdapter);
        GetDataFromDatabase();

        return view;
    }

    private void GetDataFromDatabase() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                database = FirebaseDatabase.getInstance().getReference("Orders");

                database.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);

                            assert orderModel != null;
                            if(Objects.equals(orderModel.getCustomerName(), prefs.getUserName())){
                                list.add(orderModel);
                            }

                        }
                        ordersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        },300);
    }
}