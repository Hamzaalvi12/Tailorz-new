package com.example.tailorz.CustomerFragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.R;

import java.util.Objects;

public class Customer_home extends Fragment {

    CardView Designs_btn, Tailors_btn;
    public Customer_home() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Customer_home newInstance(String param1, String param2) {
        return new Customer_home();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);
        replaceFragment(new Customer_design_fragment());
        Designs_btn = view.findViewById(R.id.Designs_btn);
        Tailors_btn = view.findViewById(R.id.Tailors_btn);

        Designs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Customer_design_fragment());
                Designs_btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                Tailors_btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            }
        });

        Tailors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Customer_tailors_fragment());
                Tailors_btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                Designs_btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            }
        });

        return  view;
    }

    private void replaceFragment(Fragment fragment)
    {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Customer_menu_frameLayout,fragment);
        fragmentTransaction.commit();

    }//REPLACE FRAGMENT
}