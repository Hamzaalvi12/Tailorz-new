package com.example.tailorz.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.example.tailorz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private EditText email_txt, mobileNo_txt, userName_txt, password_txt, confirmPassword_txt;
    RadioGroup genderRadioGroup, categoryRadioGroup;
    RadioGroup tailorCategoryRadioGroup;
    RadioButton genderRadioBtn, categoryRadioBtn, tailorCategory;

    private  static final String DATABASE_URL = "https://tailorz-2ed0b-default-rtdb.firebaseio.com/";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_txt = findViewById(R.id.emailtxt);
        mobileNo_txt = findViewById(R.id.mobileNotxt);
        userName_txt = findViewById(R.id.usernametxt);
        password_txt = findViewById(R.id.passwordtxt);
        confirmPassword_txt = findViewById(R.id.confirmPasswordtxt);
        genderRadioGroup  = findViewById(R.id.genderRadioGrpId);
        categoryRadioGroup  = findViewById(R.id.categoryRadioGrp);
        tailorCategoryRadioGroup = findViewById(R.id.TailorcategoryRadioGrp);
        // If User Already has an account
        TextView clickHere_txt = findViewById(R.id.clickheretxt);
        // Sign Up Button
        CircularProgressButton signUp_Btn = findViewById(R.id.signUpBtn);

        // If User Clicks on CLick Here (If they already have an account)
        clickHere_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUp.this, Login.class));
            }
        }); // click here btn

        signUp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp_Btn.startAnimation();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Get the inserted values in a string
                        final String email = email_txt.getText().toString();
                        final String phoneNumber = mobileNo_txt.getText().toString();
                        final String password = password_txt.getText().toString();
                        final String conPass = confirmPassword_txt.getText().toString();
                        final String userName = userName_txt.getText().toString();

                        //Check if User Has Filled All The Fields
                        if(email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || userName.isEmpty() || conPass.isEmpty()){
                            Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            recreate();
                        } else if (!password.equals(conPass)) {
                            Toast.makeText(SignUp.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                            recreate();
                        }else{
                            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(userName)){
                                        Toast.makeText(SignUp.this, "User Already Exist!", Toast.LENGTH_SHORT).show();
                                        signUp_Btn.stopAnimation();
                                        recreate();
                                    }
                                    else{
                                        databaseReference.child("Users").child(userName).child("username").setValue(userName);
                                        databaseReference.child("Users").child(userName).child("password").setValue(password);
                                        databaseReference.child("Users").child(userName).child("email").setValue(email);
                                        databaseReference.child("Users").child(userName).child("telephoneNumber").setValue(phoneNumber);
                                        databaseReference.child("Users").child(userName).child("whatsappNumber").setValue(phoneNumber);
                                        databaseReference.child("Users").child(userName).child("address").setValue("");
                                        databaseReference.child("Users").child(userName).child("profile_image_url").setValue("");
                                        databaseReference.child("Users").child(userName).child("tailor_category").setValue("");
//                                        databaseReference.child("Users").child(userName).child("Favorite_design").child("Design_name").setValue("");
//                                        databaseReference.child("Users").child(userName).child("Favorite_design").child("Design_url").setValue("");
//


                                        int selectedId1 = genderRadioGroup.getCheckedRadioButtonId();
                                        int selectedId2 = categoryRadioGroup.getCheckedRadioButtonId();
                                        int selectedId3 = tailorCategoryRadioGroup.getCheckedRadioButtonId();

                                        if (selectedId1 == -1 || selectedId2 == -1 || selectedId3 == -1) {
                                            Toast.makeText(getApplicationContext(), "Please Fill All the Fields", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        genderRadioBtn = findViewById(selectedId1);
                                        categoryRadioBtn = findViewById(selectedId2);
                                        tailorCategory = findViewById(selectedId3);

                                        String genderSelected_txt = genderRadioBtn.getText().toString();
                                        String categorySelected_txt = categoryRadioBtn.getText().toString();
                                        String tailorCategory_txt = tailorCategory.getText().toString();
                                        databaseReference.child("Users").child(userName).child("gender").setValue(genderSelected_txt);
                                        databaseReference.child("Users").child(userName).child("category").setValue(categorySelected_txt);
                                        databaseReference.child("Users").child(userName).child("tailor_category").setValue(tailorCategory_txt);

                                        Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                        signUp_Btn.doneLoadingAnimation(R.color.white, bitmap);

                                        Toast.makeText(SignUp.this, "Your Account Has Been Created Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUp.this, Login.class));
                                        finish();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }, 300);

            }
        }); //sign up btn

    } // On Create

} // END