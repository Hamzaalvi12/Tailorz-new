package com.example.tailorz.CustomerActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tailorz.CustomerAdapters.CustomerDesignAdapter;
import com.example.tailorz.CustomerAdapters.MoreDesignAdapter;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Customer_desingDetails extends AppCompatActivity {

    ImageView design_image, TailorRating, backBtn;
    CircleImageView TailorProfileImage;
    TextView tailorName, TailorCategory, DesignName;
    Button hireMe;
    Prefs prefs;
    String tailorName_txt, tailorCategory_txt, designUrl, tailorProfileImageUrl, designName, designId;
    CustomerTailorModel customerTailorModel;

    //More Designs Implementation
    RecyclerView recyclerView;
    ArrayList<TailorDesignModel> list;
    MoreDesignAdapter moreDesignAdapter;
    DatabaseReference database;
    ContentLoadingProgressBar moreDesignLoadingBar;
    TailorDesignModel tailorDesignModel;
    ImageView customer_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_desing_details);

        design_image = findViewById(R.id.details_image);
        TailorRating = findViewById(R.id.tailorRating);
        TailorProfileImage = findViewById(R.id.TailorImageCard);
        tailorName = findViewById(R.id.tailorName);
        TailorCategory = findViewById(R.id.tailorCategory);
        hireMe = findViewById(R.id.hireMe_Btn);
        backBtn = findViewById(R.id.back_arrow);
        DesignName = findViewById(R.id.DesignName);
        prefs = new Prefs(getApplicationContext());
        moreDesignLoadingBar = findViewById(R.id.moreDesingsLoading);
        recyclerView = findViewById(R.id.moreDesign_recyclerView);
        customer_profile = findViewById(R.id.customer_profile);



        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(customer_profile);

        customer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_desingDetails.this, Customer_profile.class));
            }
        });

        //More Design Declaration

        GetMoreDesignsFromDataBase();

        tailorName_txt = getIntent().getStringExtra("tailorName");
        designUrl = getIntent().getStringExtra("designUrl");
        designId = getIntent().getStringExtra("designId");
        designName = getIntent().getStringExtra("designName");

        hireMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HireMeActivity.class);
                intent.putExtra("HireDesignUrl", designUrl);
                intent.putExtra("HireDesignName",designName);
                intent.putExtra("HireTailorName", tailorName_txt);
                intent.putExtra("HireDesignID", designId);
                Log.d("Tailor NAME ::::", tailorName_txt);
                startActivity(intent);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_desingDetails.this, CustomerMain.class));
            }
        });

        Glide.with(this)
                .load(designUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(design_image);

        tailorName.setText(tailorName_txt);
        DesignName.setText(designName);

        FirebaseDatabase.getInstance().getReference().child("Users").child(tailorName_txt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerTailorModel  = snapshot.getValue(CustomerTailorModel.class);
                assert customerTailorModel != null;
                TailorCategory.setText(customerTailorModel.getTailor_category());
                Glide.with(getApplicationContext())
                        .load(customerTailorModel.getProfile_image_url())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.imagenotfound)
                        .into(TailorProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }// ON CREATE

    public void GetMoreDesignsFromDataBase() {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();

        moreDesignAdapter = new MoreDesignAdapter(getApplicationContext(), list, new MoreDesignAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TailorDesignModel designModel) {
                Intent intent = new Intent(getApplicationContext(), MoreDesignActivity.class);
                intent.putExtra("moreDesignUrl", designModel.getDesign_url());
                intent.putExtra("moreDesignName", designModel.getDesign_name());
                intent.putExtra("moreTailorName", tailorName_txt);
                intent.putExtra("moreDesignId", designModel.getDesign_id());
                Log.d("Tailor NAME ::::", tailorName_txt);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(moreDesignAdapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                database = FirebaseDatabase.getInstance().getReference("Design");
                DatabaseReference tailorRef = database.child(tailorName_txt);
                tailorRef.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            tailorDesignModel = dataSnapshot.getValue(TailorDesignModel.class);
                            list.add(tailorDesignModel);

                        }
                        moreDesignAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        },300);
    }


}//END