package com.example.tailorz.TailorFragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class contactInfoFragment extends Fragment {

    EditText shopAddress, telephoneNumber, whatsappNumber;
    CircularProgressButton saveContactInfoBtn;
    FirebaseDatabase database;
    Prefs prefs;
    public contactInfoFragment() {
        // Required empty public constructor
    }

    public static contactInfoFragment newInstance(String param1, String param2) {
        contactInfoFragment fragment = new contactInfoFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact_info, container, false);

        shopAddress = view.findViewById(R.id.addresstxt);
        telephoneNumber = view.findViewById(R.id.telephonetxt);
        whatsappNumber = view.findViewById(R.id.whatsapptxt);
        saveContactInfoBtn = view.findViewById(R.id.saveContactBtn);
        database = FirebaseDatabase.getInstance();
        prefs = new Prefs(requireActivity().getApplicationContext());

        shopAddress.setText(prefs.getUserAddress());
        telephoneNumber.setText(prefs.getUserTelephone());
        whatsappNumber.setText(prefs.getUserWhatsapp());

        onSaveContactBtn();

        return  view;
    }//ON VIEW CREATED

    void onSaveContactBtn(){
        saveContactInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactInfoBtn.startAnimation();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Get Info From Text Boxes
                        final String shopAddress_txt = shopAddress.getText().toString();
                        final String telephoneNumber_txt = telephoneNumber.getText().toString();
                        final String whatsApp_txt = whatsappNumber.getText().toString();

                        //Check if any of them is empty
                        if(!shopAddress_txt.isEmpty() && !telephoneNumber_txt.isEmpty() && !whatsApp_txt.isEmpty()){

                            Map<String, Object> map = new HashMap<>();
                            map.put("telephoneNumber", telephoneNumber_txt);
                            map.put("whatsappNumber", whatsApp_txt);
                            map.put("address", shopAddress_txt);

                            database.getReference().child("Users").child(prefs.getUserName()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Your Contact Information Has Been Updated", Toast.LENGTH_SHORT).show();
                                            Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                            saveContactInfoBtn.doneLoadingAnimation(R.color.white, bitmap);
                                            prefs.setUserWhatsapp(whatsApp_txt);
                                            prefs.setUserAddress(shopAddress_txt);
                                            prefs.setUserTelephone(telephoneNumber_txt);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("ERROR", e.toString());
                                            Toast.makeText(getContext(), "Cannot Upload Contact Information!!", Toast.LENGTH_SHORT).show();
                                            Drawable drawable = getResources().getDrawable(R.drawable.cancelbutton);
                                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                            saveContactInfoBtn.doneLoadingAnimation(R.color.white, bitmap);
                                        }
                                    });

                        }else{
                            Toast.makeText(getContext(), "Please Enter All the Details", Toast.LENGTH_SHORT).show();
                            Drawable drawable = getResources().getDrawable(R.drawable.cancelbutton);
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            saveContactInfoBtn.doneLoadingAnimation(R.color.white, bitmap);

                        }
                    }
                },300);
            }
        });
    }

}// END