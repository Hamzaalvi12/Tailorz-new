package com.example.tailorz.CustomerFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.CustomerActivities.Customer_desingDetails;
import com.example.tailorz.CustomerAdapters.CustomerDesignAdapter;
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


public class Customer_design_fragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    CustomerDesignAdapter customerDesignAdapter;
    ArrayList<TailorDesignModel> list;
    Prefs prefs;

    public Customer_design_fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Customer_design_fragment newInstance(String param1, String param2) {
        return new Customer_design_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_design_fragment, container, false);

        prefs = new Prefs(requireActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.Customer_Designs_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        customerDesignAdapter = new CustomerDesignAdapter(getContext(), list, new CustomerDesignAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TailorDesignModel designModel) {
                Intent intent = new Intent(view.getContext(), Customer_desingDetails.class);
                intent.putExtra("designUrl", designModel.getDesign_url());
                intent.putExtra("designName", designModel.getDesign_name());
                intent.putExtra("tailorName", designModel.getTailor_username());
                intent.putExtra("designId", designModel.getDesign_id());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(customerDesignAdapter);
        GetDataFromDatabase();
        return  view;
    }

    private void GetDataFromDatabase() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                database = FirebaseDatabase.getInstance().getReference("AllDesigns");
                //DatabaseReference tailorRef = database.child(prefs.getUserName());

                database.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            TailorDesignModel tailorDesignModel = dataSnapshot.getValue(TailorDesignModel.class);
                            list.add(tailorDesignModel);
                        }
                        customerDesignAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },300);
    }


}