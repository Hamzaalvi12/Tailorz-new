package com.example.tailorz.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.example.tailorz.CustomerActivities.CustomerMain;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.example.tailorz.TailorActivities.TailorMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private  static final String DATABASE_URL = "https://tailorz-2ed0b-default-rtdb.firebaseio.com/";
    EditText userName_txt;
    EditText password_txt;
    CircularProgressButton loginBtn;
    Prefs prefs;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView clickHereTxt = findViewById(R.id.clickheretxt);
        userName_txt= findViewById(R.id.userNametxt);
        password_txt = findViewById(R.id.passwordtxt);
        loginBtn= findViewById(R.id.loginBtn);
        prefs = new Prefs(getApplicationContext()); // initialize the preference object.

        clickHereTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticateUser();
            }
        });
    } // ON CREATE

    void AuthenticateUser() {
        loginBtn.startAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String userName = userName_txt.getText().toString();
                final String password = password_txt.getText().toString();

                if(userName.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
                    recreate();
                }else{
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userName)){
                                final String databasePass = snapshot.child(userName).child("password").getValue(String.class);
                                assert databasePass != null;
                                if(databasePass.equals(password)){
                                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                                    //getting user data which user has logged in and sharing it to other screens using shared preferences
                                    final String category = snapshot.child(userName).child("category").getValue(String.class);
                                    final String username = snapshot.child(userName).child("username").getValue(String.class);
                                    final String email = snapshot.child(userName).child("email").getValue(String.class);
                                    final String address = snapshot.child(userName).child("address").getValue(String.class);
                                    final String telephone = snapshot.child(userName).child("telephoneNumber").getValue(String.class);
                                    final String whatsapp = snapshot.child(userName).child("whatsappNumber").getValue(String.class);
                                    String profile_img = snapshot.child(userName).child("profile_image_url").getValue(String.class);
                                    String tailorCategory = snapshot.child(userName).child("tailor_category").getValue(String.class);
                                    String UserGender = snapshot.child(userName).child("gender").getValue(String.class);



                                    //Setting User Data to prefs objects.
                                    prefs.setUserEmail(email);
                                    prefs.setUserName(username);
                                    prefs.setUserCategory(category);
                                    prefs.setUserWhatsapp(whatsapp);
                                    prefs.setUserAddress(address);
                                    prefs.setUserTelephone(telephone);
                                    prefs.setUserProfileImage(profile_img);
                                    prefs.setTailorCategory(tailorCategory);
                                    prefs.setUserGender(UserGender);

                                    Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                    loginBtn.doneLoadingAnimation(R.color.white, bitmap);

                                    //Choosing if the user is Customer or Tailor
                                    assert category != null;
                                    if(category.equals("Customer")){
                                        startActivity(new Intent(Login.this, CustomerMain.class));
                                        finish();
                                    }else if(category.equals("Tailor")){
                                        startActivity(new Intent(Login.this, TailorMain.class));
                                        finish();
                                    }

                                }else{
                                    Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                            }
                            else{
                                Toast.makeText(Login.this, "User Does Not Exists", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        },300);
    }

} //END