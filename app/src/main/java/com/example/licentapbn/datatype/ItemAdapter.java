package com.example.licentapbn.datatype;

import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.Collection;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{

    Context context;
    List<Item> items;
    List<Item> itemsCopy;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.itemsCopy=items;
        this.items =this.itemsCopy;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tv1.setText(items.get(position).getName());
        holder.invisibleLayout.setVisibility(View.GONE);
        holder.tvItemWeight.setText("Weight: "+items.get(position).getWeight());
        holder.tvItemSize.setText("Size: "+items.get(position).getSize());
        holder.tvMemberName.setText("Taken by: "+items.get(position).getMemberName());
        holder.tv3.setText(items.get(position).getDescription());
        for(Item item: items){
            item.setExpanded(false);
        }
        if(!items.get(position).getMemberName().equals("Storage")) {
            holder.tvMemberName.setVisibility(View.VISIBLE);
        }

        if(items.get(position).isFree()){
            holder.tv2.setText("Availabe");
            holder.tv2.setTextColor(Color.GREEN);
        }else{
            holder.tv2.setTextColor(Color.RED);
            holder.tv2.setText("Unavailabe");
        }

        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);
        holder.item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).isExpanded()) {
                    holder.invisibleLayout.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                    holder.tvItemSize.setVisibility(View.GONE);
                    holder.tvItemWeight.setVisibility(View.GONE);
                    holder.tvMemberName.setVisibility(View.GONE);
                    items.get(position).setExpanded(false);

                } else {
                    holder.invisibleLayout.setVisibility(View.VISIBLE);
                    holder.tv3.setVisibility(View.VISIBLE);
                    holder.tvItemSize.setVisibility(View.VISIBLE);
                    if(!items.get(position).getMemberName().equals("Storage")) {
                        holder.tvMemberName.setVisibility(View.VISIBLE);
                    }
                    holder.tvItemWeight.setVisibility(View.VISIBLE);
                    items.get(position).setExpanded(true);
                }
            }
        });

    }
    public void filterItemsByFree(boolean isFree) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : items) {
            if (item.isFree()==isFree) {
                filteredList.add(item);
            }
        }
        // Actualizează lista de elemente din adapter cu lista filtrată
        items = filteredList;
        notifyDataSetChanged();
    }
    public void clearFilter() {
        items = itemsCopy; // Lista filtrată devine lista completă de elemente
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tvItemWeight,tvItemSize,tvMemberName;
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
            tvMemberName=itemView.findViewById(R.id.tv_owner_name);
            item_cardview=itemView.findViewById(R.id.item_cardview);
            invisibleLayout=itemView.findViewById(R.id.invisible_constraint_layout);
            imageView=itemView.findViewById(R.id.recyclerImage);
        }

    }
}
