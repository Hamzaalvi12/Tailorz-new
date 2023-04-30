package com.example.tailorz.CustomerAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tailorz.CustomerModels.NotificationModel;
import com.example.tailorz.R;
import com.example.tailorz.TailorModels.TailorDesignModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    ArrayList<NotificationModel> list;
    NotificationAdapter.ItemClickListener item_click;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list, NotificationAdapter.ItemClickListener item_click) {
        this.context = context;
        this.list = list;
        this.item_click = item_click;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlenotification, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        NotificationModel notificationModel = list.get(position);
        holder.heading.setText(notificationModel.getHeading());
        holder.description.setText(notificationModel.getDescription());
        holder.date.setText(notificationModel.getDate());
        holder.time.setText(notificationModel.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_click.onItemClick(list.get(position));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Notifications")
                        .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                           String key =  dataSnapshot.getRef().getKey();
                            Log.d("NotificationKey :::: ", key);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView heading, description, date,time;
        ImageView deleteBtn;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.notification_heading);
            description = itemView.findViewById(R.id.notification_content);
            deleteBtn = itemView.findViewById(R.id.notification_delete);
            date = itemView.findViewById(R.id.notification_date);
            time = itemView.findViewById(R.id.notification_time);

        }
    }

    public interface ItemClickListener{
        void onItemClick(NotificationModel notificationModel);
    }
}
