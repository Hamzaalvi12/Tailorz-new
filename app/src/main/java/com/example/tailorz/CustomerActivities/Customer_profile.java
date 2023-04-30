package com.example.tailorz.CustomerActivities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

public class Customer_profile extends AppCompatActivity {

    ImageView logoutBtn,edit_profile_btn,customerProfileImage,back_btn;
    TextView UserNametxt,gendertxt,emailtxt,addresstxt,telephonetxt,whatsapptxt;
    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        logoutBtn = findViewById(R.id.logout_btn);
        edit_profile_btn = findViewById(R.id.edit_profile_btn);
        UserNametxt = findViewById(R.id.UserNametxt);
        gendertxt = findViewById(R.id.gendertxt);
        emailtxt = findViewById(R.id.emailtxt);
        addresstxt = findViewById(R.id.addresstxt);
        telephonetxt = findViewById(R.id.telephonetxt);
        whatsapptxt = findViewById(R.id.whatsapptxt);
        customerProfileImage = findViewById(R.id.customerProfileImage);
        back_btn = findViewById(R.id.back_btn);
        prefs = new Prefs(getApplicationContext());

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        UserNametxt.setText(prefs.getUserName());
        emailtxt.setText(prefs.getUserEmail());
        telephonetxt.setText(prefs.getUserTelephone());
        whatsapptxt.setText(prefs.getUserWhatsapp());
        addresstxt.setText(prefs.getUserAddress());
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(customerProfileImage);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        //Helpers
        // To select image from gallery
         launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                // userProfileImage.setImageURI(result);
                UploadImage(result);
            }
        });

        //FUNCTIONS
        logOut();
        EditBtn();

    }// ON CREATE

    public void logOut(){
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_profile.this, Login.class));
                finish();
            }
        });
    }

    public void EditBtn(){
        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });
    }

    void UploadImage(Uri uri){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(uri != null){

                    final StorageReference storageReference = storage.getReference()
                            .child("CustomerProfileImages").child(prefs.getUserName());
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
                                                    Toast.makeText(getApplicationContext(), "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show();
                                                    prefs.setUserProfileImage(uri.toString());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Profile Image Cannot be Updated!!", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Cannot Upload Image", Toast.LENGTH_SHORT).show();
                                    Log.d("PROFILE UPLOAD ERROR", "CANNOT GET DOWNLOAD URL");

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Profile Image Cannot Be Uploaded!!", Toast.LENGTH_SHORT).show();
                            Log.d("PROFILE PUT ERROR", "CANNOT PUT FILE TO FIREBASE");

                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select a Profile Image", Toast.LENGTH_SHORT).show();
                }
            }
        },300);
    }

}//END