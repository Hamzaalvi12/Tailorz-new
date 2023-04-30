package com.example.tailorz.CustomerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tailorz.CustomerModels.CustomerTailorModel;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;


import java.util.ArrayList;

public class CustomerTailorAdapter  extends RecyclerView.Adapter<CustomerTailorAdapter.CustomerTailorViewHolder> {

    Context context;
    ArrayList<CustomerTailorModel> list;

    public CustomerTailorAdapter(Context context, ArrayList<CustomerTailorModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomerTailorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_tailorlayout, parent, false);
        return new CustomerTailorAdapter.CustomerTailorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerTailorViewHolder holder, int position) {

        CustomerTailorModel customerDesign = list.get(position);
        Prefs prefs = new Prefs(context);
        holder.tailorName.setText(customerDesign.getUsername());
        Glide.with(context)
                .load(customerDesign.getProfile_image_url())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(holder.TailorImage);
        holder.tailorCategory.setText(customerDesign.getTailor_category());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CustomerTailorViewHolder extends RecyclerView.ViewHolder{

        ImageView TailorImage;
        TextView tailorName, tailorCategory;

        public CustomerTailorViewHolder(@NonNull View itemView) {
            super(itemView);


            TailorImage = itemView.findViewById(R.id.TailorProfileImage);
            tailorName = itemView.findViewById(R.id.tailorName);
            tailorCategory = itemView.findViewById(R.id.tailorCategory);


        }
    }
}
