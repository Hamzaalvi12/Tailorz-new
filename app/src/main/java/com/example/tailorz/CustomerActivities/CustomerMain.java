package com.example.tailorz.CustomerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.tailorz.CustomerFragments.Customer_home;
import com.example.tailorz.CustomerFragments.Favorites_fragment;
import com.example.tailorz.CustomerFragments.Orders_fragment;
import com.example.tailorz.CustomerFragments.measurement;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class CustomerMain extends AppCompatActivity {

    ImageView Customer_notification, Customer_profileBtn;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        Customer_notification = findViewById(R.id.Notification_btn);
        Customer_profileBtn = findViewById(R.id.customer_profile);
        prefs = new Prefs(getApplicationContext());

        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(Customer_profileBtn);

        Customer_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerMain.this, CustomerNotification_Activity.class));
            }
        });

        Customer_profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerMain.this, Customer_profile.class));
            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.Customer_bottom_navigation);
        replaceFragment(new Customer_home());

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.Customer_home:
                    replaceFragment(new Customer_home());
                    break;
                case R.id.measurement:
                    replaceFragment(new measurement());
                    break;
                case R.id.favorites:
                    replaceFragment(new Favorites_fragment());
                    break;
                case R.id.cart:
                    replaceFragment(new Orders_fragment());
            }
            return true;
        });



    }//ON CREATE

    private void replaceFragment(Fragment fragment)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Customer_frame_layout,fragment);
        fragmentTransaction.commit();

    }//REPLACE FRAGMENT
}//END