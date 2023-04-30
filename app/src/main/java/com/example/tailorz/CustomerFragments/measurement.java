package com.example.tailorz.CustomerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.CustomerActivities.TakeUserMeasurement;
import com.example.tailorz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link measurement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class measurement extends Fragment {

    FloatingActionButton measurementCameraBtn;

    public measurement() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static measurement newInstance(String param1, String param2) {
        return new measurement();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measurement, container, false);
        measurementCameraBtn = view.findViewById(R.id.measurementCameraBtn);
        measurementCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), TakeUserMeasurement.class));
            }
        });

        return view;
    }
}