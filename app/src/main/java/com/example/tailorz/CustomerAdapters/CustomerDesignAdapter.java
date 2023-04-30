package com.example.tailorz.CustomerAdapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tailorz.Helpers.Prefs;
import com.example.tailorz.R;
import com.example.tailorz.SQLDatabase.DataBaseHelper;
import com.example.tailorz.TailorModels.TailorDesignModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class CustomerDesignAdapter extends RecyclerView.Adapter<CustomerDesignAdapter.CustomerDesignViewHolder> {

    Context context;
    ArrayList<TailorDesignModel> list;
    ItemClickListener item_click;


    public CustomerDesignAdapter(Context context, ArrayList<TailorDesignModel> list, ItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.item_click = clickListener;
    }

    @NonNull
    @Override
    public CustomerDesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customers_singledesign, parent, false);
        return new CustomerDesignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerDesignViewHolder holder, int position) {

        TailorDesignModel customerDesign = list.get(position);
        DataBaseHelper db = new DataBaseHelper(context);
        Prefs prefs = new Prefs(context);
        holder.CustomerDesignName.setText(customerDesign.getDesign_name());
        Glide.with(context)
                .load(customerDesign.getDesign_url())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(holder.CustomerDesignImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_click.onItemClick(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CustomerDesignViewHolder extends RecyclerView.ViewHolder{

        ImageView CustomerDesignImage, favorites_btn;
        TextView CustomerDesignName;


        public CustomerDesignViewHolder(@NonNull View itemView) {
            super(itemView);

            CustomerDesignImage = itemView.findViewById(R.id.CustomerDesignImage);
            CustomerDesignName = itemView.findViewById(R.id.CustomerDesignName);
            favorites_btn = itemView.findViewById(R.id.favorites_btn);

        }
    }

    public interface ItemClickListener{
        void onItemClick(TailorDesignModel designModel);
    }

}
