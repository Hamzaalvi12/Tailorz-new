package com.example.tailorz.Helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tailorz.CustomerModels.NotificationModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notifications {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    NotificationModel notificationModel = new NotificationModel();

    public void sendNotifications(String heading, String description, Context context){
        Prefs prefs = new Prefs(context);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        notificationModel.setDate(currentDate);
        notificationModel.setTime(currentTime);
        notificationModel.setHeading(heading);
        notificationModel.setDescription(description);
        notificationModel.setUserName(prefs.getUserName());

            database.getReference().child("Notifications").push().setValue(notificationModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Notification Send Failed", Toast.LENGTH_SHORT).show();

                        }
                    });

    }

}
