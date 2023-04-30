package com.example.tailorz.TailorFragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.example.tailorz.Helpers.GenerateDesignID;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class designFragment extends Fragment {

    Button addDesignBtn;
    CircularProgressButton uploadImgBtn;
    ActivityResultLauncher<String> launcher;
    ImageView galleryImg;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri uri;
    EditText designName;
    Prefs prefs;


    // TODO: Rename parameter arguments, choose names that match

    public designFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_design, container, false);

        addDesignBtn = view.findViewById(R.id.addDesignBtn);
        galleryImg = view.findViewById(R.id.galleryImg);
        uploadImgBtn = view.findViewById(R.id.uploadImageBtn);
        prefs = new Prefs(requireActivity().getApplicationContext());
        designName = view.findViewById(R.id.designNametxt);

        String username = prefs.getUserName();
        String email = prefs.getUserEmail();
        String category = prefs.getUserCategory();

        Log.d("USERNAME :: ", username);
        Log.d("EMAIL :: ", email);
        Log.d("CATEGORY :: ", category);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                galleryImg.setImageURI(result);
                uri = result;
            }
        });

        addDesignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             uploadImgBtn.startAnimation();
                GenerateDesignID generateDesignID = new GenerateDesignID();
                String design_id = generateDesignID.generateRandomID(10);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final String designName_txt = designName.getText().toString(); // take Design Name
                        if(designName_txt.isEmpty() && uri == null){
                            Toast.makeText(getContext(), "Please Enter Design Name Or Select an Image", Toast.LENGTH_SHORT).show();
                        }else{
                            final StorageReference storageReference = storage.getReference()
                                    .child("Design").child(username).child(designName_txt);
                            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.getReference().child("Design")
                                                    .child(username)
                                                    .child( designName_txt)
                                                    .child("Design_url")
                                                    .setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            database.getReference().child("Design")
                                                    .child(username)
                                                    .child(designName_txt)
                                                    .child("Design_name")
                                                    .setValue(designName_txt);

                                            database.getReference().child("Design")
                                                    .child(username)
                                                    .child(designName_txt)
                                                    .child("Design_id")
                                                    .setValue(design_id);

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("Design_name", designName_txt);
                                            map.put("Design_url", uri.toString());
                                            map.put("Design_id", design_id);
                                            map.put("tailor_username", prefs.getUserName());

                                            //Upload to All Designs Also
                                            FirebaseDatabase.getInstance().getReference().child("AllDesigns").push()
                                                    .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Your Design Has Been Uploaded and will be Visible to Customers", Toast.LENGTH_SHORT).show();
                                                            Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                            uploadImgBtn.doneLoadingAnimation(R.color.white, bitmap);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("ERROR", e.toString());
                                                            Toast.makeText(getContext(), "Cannot Upload Design!!", Toast.LENGTH_SHORT).show();
                                                            Drawable drawable = getResources().getDrawable(R.drawable.cancelbutton);
                                                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                            uploadImgBtn.doneLoadingAnimation(R.color.white, bitmap);

                                                        }
                                                    });
                                        }
                                    });
                                }
                            });



                        } // else condition

                    }
                },300);
            }
        });

        return view;
    }
}