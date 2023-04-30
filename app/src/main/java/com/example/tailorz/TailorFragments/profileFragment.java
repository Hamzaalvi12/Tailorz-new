package com.example.tailorz.TailorFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.example.tailorz.Register.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {

    ImageView logout_btn, edit_btn, userProfileImage;
    TextView telephone_txt,whatsapp_txt, address_txt;
    TextView profileUsername;
    Prefs prefs;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityResultLauncher<String> launcher;

    public profileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        return new profileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        //Initialize Views
        InitViews(view);


        // To select image from gallery
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
               // userProfileImage.setImageURI(result);
                UploadImage(result);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Login.class));
                requireActivity().finish();
            }
        });

        return  view;
    }

    //To Initialize View
    void InitViews(View v) {
        //Initialize all view Here
        logout_btn = v.findViewById(R.id.logout_btn);
        edit_btn = v.findViewById(R.id.edit_btn);
        telephone_txt = v.findViewById(R.id.profilePhone);
        whatsapp_txt = v.findViewById(R.id.profileWhatsapp);
        address_txt = v.findViewById(R.id.profileAddress);
        profileUsername = v.findViewById(R.id.profileUsername);
        userProfileImage = v.findViewById(R.id.userProfileImage);
        prefs = new Prefs(requireActivity().getApplicationContext());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        //setting user info
        telephone_txt.setText(prefs.getUserTelephone());
        whatsapp_txt.setText(prefs.getUserWhatsapp());
        address_txt.setText(prefs.getUserAddress());
        profileUsername.setText(prefs.getUserName());
        Glide.with(requireActivity().getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(userProfileImage);
    }

    void UploadImage(Uri uri){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(uri != null){

                    final StorageReference storageReference = storage.getReference()
                            .child("TailorProfileImages").child(prefs.getUserName());
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("profile_image_url", uri.toString());

                                    database.getReference().child("Users").child(prefs.getUserName())
                                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getContext(), "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show();
                                                    prefs.setUserProfileImage(uri.toString());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Profile Image Cannot be Updated!!", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Cannot Upload Image", Toast.LENGTH_SHORT).show();
                                    Log.d("PROFILE UPLOAD ERROR", "CANNOT GET DOWNLOAD URL");

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Profile Image Cannot Be Uploaded!!", Toast.LENGTH_SHORT).show();
                                    Log.d("PROFILE PUT ERROR", "CANNOT PUT FILE TO FIREBASE");

                                }
                            });
                }else{
                    Toast.makeText(getContext(), "Please Select a Profile Image", Toast.LENGTH_SHORT).show();
                }
            }
        },300);
    }
}