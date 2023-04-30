package com.example.tailorz.CustomerActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tailorz.CustomerAdapters.NotificationAdapter;
import com.example.tailorz.CustomerModels.NotificationModel;
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

public class CustomerNotification_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel> list;
    Prefs prefs;
    ImageView back_arrow,customer_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_notification);

        prefs = new Prefs(getApplicationContext());

        back_arrow = findViewById(R.id.backBtn);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        customer_profile = findViewById(R.id.customer_profile);
        customer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerNotification_Activity.this, Customer_profile.class));
            }
        });
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(customer_profile);




        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getApplicationContext(), list, new NotificationAdapter.ItemClickListener() {
            @Override
            public void onItemClick(NotificationModel notificationModel) {
                Toast.makeText(CustomerNotification_Activity.this, "Okay Very Good!!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(notificationAdapter);
        GetDataFromDatabase();
    }

    private void GetDataFromDatabase() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                database = FirebaseDatabase.getInstance().getReference("Notifications");
                //DatabaseReference tailorRef = database.child(prefs.getUserName());

                database.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);

                            assert notificationModel != null;
                            if(Objects.equals(notificationModel.getUserName(), prefs.getUserName())){
                                list.add(notificationModel);
                            }

                        }
                        notificationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },300);

    }
}