package com.example.licentapbn.datatype;

import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    Context context;
    List<Item> items;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tv1.setText(items.get(position).getName());
        holder.tvItemWeight.setText("Weight: "+items.get(position).getWeight());
        holder.tvItemSize.setText("Size: "+items.get(position).getSize());
        if(items.get(position).isFree()){
            holder.tv2.setText("Availabe");
            holder.tv2.setTextColor(Color.GREEN);
        }else{
            holder.tv2.setTextColor(Color.RED);
            holder.tv2.setText("Unavailabe");
        }
        holder.tv3.setText(items.get(position).getDescription());
        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);
        holder.item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).isExpanded()) {
                    holder.invisibleLayout.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                    holder.tvItemSize.setVisibility(View.GONE);
                    holder.tvItemWeight.setVisibility(View.GONE);
                    items.get(position).setExpanded(false);

                } else {
                    holder.invisibleLayout.setVisibility(View.VISIBLE);
                    holder.tv3.setVisibility(View.VISIBLE);
                    holder.tvItemSize.setVisibility(View.VISIBLE);
                    holder.tvItemWeight.setVisibility(View.VISIBLE);
                    items.get(position).setExpanded(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tvItemWeight,tvItemSize;
        ImageView imageView;
        CardView item_cardview;
        ConstraintLayout invisibleLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv_name_item);
            tv2 = itemView.findViewById(R.id.tv_status_item);
            tv3=itemView.findViewById(R.id.tv_details);
            tvItemWeight = itemView.findViewById(R.id.tv_item_weight);
            tvItemSize=itemView.findViewById(R.id.tv_item_size);
            item_cardview=itemView.findViewById(R.id.item_cardview);
            invisibleLayout=itemView.findViewById(R.id.invisible_constraint_layout);
            imageView=itemView.findViewById(R.id.recyclerImage);
        }

    }
}
