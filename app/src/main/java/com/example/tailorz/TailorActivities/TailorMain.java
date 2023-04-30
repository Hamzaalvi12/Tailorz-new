package com.example.tailorz.TailorActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.example.tailorz.R;
import com.example.tailorz.TailorFragments.contactInfoFragment;
import com.example.tailorz.TailorFragments.designFragment;
import com.example.tailorz.TailorFragments.homefragment;
import com.example.tailorz.TailorFragments.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TailorMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        replaceFragment(new homefragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new homefragment());
                    break;
                case R.id.design:
                    replaceFragment(new designFragment());
                    break;
                case R.id.contactInfo:
                    replaceFragment(new contactInfoFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new profileFragment());
            }
            return true;
        });


    } // ON CREATE

    private void replaceFragment(Fragment fragment)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }//REPLACE FRAGMENT

}// END