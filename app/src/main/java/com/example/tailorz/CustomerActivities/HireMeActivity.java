package com.example.tailorz.CustomerActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.bumptech.glide.Glide;
import com.example.tailorz.CustomerModels.CustomerTailorModel;
import com.example.tailorz.CustomerModels.OrderModel;
import com.example.tailorz.Helpers.GenerateDesignID;
import com.example.tailorz.Helpers.Notifications;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HireMeActivity extends AppCompatActivity {

    ImageView customer_profile,back_arrow,imageView,tailorRating;
    TextView DesignName,DesignID,tailorName,tailorCategory,tailorWhatsapp,tailorTelephone,tailorAddress,
            customerName,CustomerGender,CustomerWhatsapp,CustomerTelephone,CustomerAddress;
    CircleImageView TailorDesignImageCard,TailorImageCard,CustomerImage;
    Prefs prefs;
    String tailorName_txt, designUrl, designName, designId;
    CircularProgressButton ConfirmOrderBtn;
    FirebaseDatabase database;
    OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_me);

        prefs = new Prefs(getApplicationContext());
        orderModel = new OrderModel();
        database = FirebaseDatabase.getInstance();

        //Functions
        SetUserProfile();
        SetBackArrowPressed();
        GetIntentValues();
        SetDesignDetails();
        SetTailorDetails();
        SetUserDetails();
        PlaceOrder();




    }// ON CREATE

    public void SetUserProfile() {
        //Setting Up Customer Profile
        customer_profile = findViewById(R.id.customer_profile);
        customer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HireMeActivity.this, Customer_profile.class));
            }
        });
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(customer_profile);
        //User Profile Done
    }
    public void SetBackArrowPressed() {
        //Setting Back Btn
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void GetIntentValues(){
        //Getting Intent Values
        tailorName_txt = getIntent().getStringExtra("HireTailorName");
        designUrl = getIntent().getStringExtra("HireDesignUrl");
        designId = getIntent().getStringExtra("HireDesignID");
        designName = getIntent().getStringExtra("HireDesignName");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        orderModel.setTailorName(tailorName_txt);
        orderModel.setDesignID(designId);
        orderModel.setDesignName(designName);
        orderModel.setDesignUrl(designUrl);
        orderModel.setCustomerName(prefs.getUserName());
        orderModel.setCustomerAddress(prefs.getUserAddress());
        orderModel.setCustomerGender(prefs.getUserGender());
        orderModel.setCustomerAddress(prefs.getUserAddress());
        orderModel.setCustomerPhone(prefs.getUserTelephone());
        orderModel.setCustomerWhatsapp(prefs.getUserWhatsapp());
        orderModel.setCustomerChestMeasurement("");
        orderModel.setCustomerCollarMeasurement("");
        orderModel.setCustomerShoulderMeasurement("");
        orderModel.setCustomerWaistMeasurement("");
        orderModel.setTotalPrice("");
        orderModel.setOrderDate(currentDate);
        orderModel.setCompletionExpectedBy("");

    }
    public void SetDesignDetails(){
        //Setting Design Details
        imageView = findViewById(R.id.imageView);
        TailorDesignImageCard = findViewById(R.id.TailorDesignImageCard);
        DesignName = findViewById(R.id.DesignName);
        DesignID = findViewById(R.id.DesignID);
        Glide.with(getApplicationContext())
                .load(designUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(imageView);
        Glide.with(getApplicationContext())
                .load(designUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(TailorDesignImageCard);
        DesignName.setText(designName);
        DesignID.setText(designId);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DesignViewActivity.class);
                intent.putExtra("DesignViewUrl", designUrl);
                startActivity(intent);
            }
        });
    }
    public void SetTailorDetails(){
        //Setting TailorDetails
        tailorName = findViewById(R.id.tailorName);
        tailorCategory = findViewById(R.id.tailorCategory);
        tailorWhatsapp = findViewById(R.id.tailorWhatsapp);
        tailorTelephone = findViewById(R.id.tailorTelephone);
        tailorAddress = findViewById(R.id.tailorAddress);
        TailorImageCard = findViewById(R.id.TailorImageCard);
        tailorName.setText(tailorName_txt);

        FirebaseDatabase.getInstance().getReference().child("Users").child(tailorName_txt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CustomerTailorModel customerTailorModel  = snapshot.getValue(CustomerTailorModel.class);
                assert customerTailorModel != null;
                tailorCategory.setText(customerTailorModel.getTailor_category());
                Glide.with(getApplicationContext())
                        .load(customerTailorModel.getProfile_image_url())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.imagenotfound)
                        .into(TailorImageCard);
                tailorAddress.setText(customerTailorModel.getAddress());
                tailorWhatsapp.setText(customerTailorModel.getWhatsappNumber());
                tailorTelephone.setText(customerTailorModel.getTelephoneNumber());

                orderModel.setTailorAddress(customerTailorModel.getAddress());
                orderModel.setTailorPhone(customerTailorModel.getTelephoneNumber());
                orderModel.setTailorWhatsapp(customerTailorModel.getWhatsappNumber());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void SetUserDetails(){
        //Setting Customer Details
        CustomerImage = findViewById(R.id.CustomerImage);
        customerName = findViewById(R.id.customerName);
        CustomerGender = findViewById(R.id.CustomerGender);
        CustomerWhatsapp = findViewById(R.id.CustomerWhatsapp);
        CustomerTelephone = findViewById(R.id.CustomerTelephone);
        CustomerAddress = findViewById(R.id.CustomerAddress);

        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(CustomerImage);
        customerName.setText(prefs.getUserName());
        CustomerWhatsapp.setText(prefs.getUserWhatsapp());
        CustomerTelephone.setText(prefs.getUserTelephone());
        CustomerAddress.setText(prefs.getUserAddress());
        CustomerGender.setText(prefs.getUserGender());
    }
    public void PlaceOrder(){
        //Setting Up Order
        ConfirmOrderBtn = findViewById(R.id.ConfirmOrderBtn);

        ConfirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmOrderBtn.startAnimation();
                GenerateDesignID generateDesignID = new GenerateDesignID();
                String order_ID = generateDesignID.generateOrderID(10);
                orderModel.setOrderID(order_ID);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        database.getReference().child("Orders").child(order_ID).setValue(orderModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Your Order Has Been Successfully Placed", Toast.LENGTH_SHORT).show();
                                        Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                        ConfirmOrderBtn.doneLoadingAnimation(R.color.white, bitmap);
                                        String notificationHeading = "Your Order has Been Placed Successfully Against Order ID : " + order_ID;
                                        String notificationDescription = "You Have Ordered " + orderModel.getDesignName() + " From Tailor : " + orderModel.getTailorName();
                                        Notifications notifications = new Notifications();
                                        notifications.sendNotifications(notificationHeading, notificationDescription, getApplicationContext());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("ERROR", e.toString());
                                        Toast.makeText(getApplicationContext(), "Cannot Place Order!!", Toast.LENGTH_SHORT).show();
                                        Drawable drawable = getResources().getDrawable(R.drawable.cancelbutton);
                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                        ConfirmOrderBtn.doneLoadingAnimation(R.color.white, bitmap);
                                        recreate();
                                    }
                                });
                    }
                },300);

            }
        });
    }
}