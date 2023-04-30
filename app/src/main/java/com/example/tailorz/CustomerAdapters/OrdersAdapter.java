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
import com.example.tailorz.CustomerModels.OrderModel;
import com.example.tailorz.R;
import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    Context context;
    ArrayList<OrderModel> list;
    ItemClickListener item_click;

    public OrdersAdapter(Context context, ArrayList<OrderModel> list, ItemClickListener item_click) {
        this.context = context;
        this.list = list;
        this.item_click = item_click;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singleorder, parent, false);
        return new OrdersAdapter.OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        OrderModel orderModel = list.get(position);
        holder.Date.setText("Order date: " + orderModel.getOrderDate());
        holder.orderID.setText("Order ID: " +orderModel.getOrderID());
        holder.tailorName.setText("Tailor name: " +orderModel.getTailorName());
        holder.DesignID.setText("Design ID: " +orderModel.getDesignID());
        holder.DesignName.setText("Design name: " +orderModel.getDesignName());
        Glide.with(context)
                .load(orderModel.getDesignUrl())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(holder.DesignImage);
        holder.itemView.setOnClickListener(v -> item_click.onItemClick(list.get(position)));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder{

        ImageView DesignImage;
        TextView DesignName, DesignID, tailorName, orderID,Date;


        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            DesignImage = itemView.findViewById(R.id.TailorDesignImageCard);
            DesignName = itemView.findViewById(R.id.DesignName);
            DesignID = itemView.findViewById(R.id.DesignID);
            tailorName = itemView.findViewById(R.id.TailorName);
            orderID = itemView.findViewById(R.id.OrderID);
            Date = itemView.findViewById(R.id.OrderDate);

        }
    }

    public interface ItemClickListener{
        void onItemClick(OrderModel orderModel);
    }
}
