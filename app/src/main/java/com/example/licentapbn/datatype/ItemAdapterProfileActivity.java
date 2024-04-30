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
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAdapterProfileActivity extends RecyclerView.Adapter<ItemAdapterProfileActivity.ItemHolder>{

    Context context;
    List<Item> items;
    List<Item> itemsCopy;

    public ItemAdapterProfileActivity(Context context, List<Item> items) {
        this.context = context;
        this.itemsCopy=items;
        this.items =this.itemsCopy;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_recycler_view_profileactivity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tv1.setText(items.get(position).getName());
        for(Item item: items){
            item.setExpanded(false);
        }
        holder.tvItemWeight.setText("Weight: "+items.get(position).getWeight());
        holder.tvItemSize.setText("Size: "+items.get(position).getSize());
        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);
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
    public void filterItemsByOwner(FirebaseUser firebaseUser){
        List<Item> filteredList = new ArrayList<>();
        for (Item item : items) {
            if (item.getMemberId().equals(firebaseUser.getUid())) {
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
        if(items!=null) {
            return items.size();
        }else{
            return 0;
        }
    }



    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv1,tvItemWeight,tvItemSize;
        ImageView imageView;
        CardView item_cardview;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv_name_item);
            tvItemWeight = itemView.findViewById(R.id.tv_item_weight);
            tvItemSize=itemView.findViewById(R.id.tv_item_size);
            item_cardview=itemView.findViewById(R.id.item_cardview);
            imageView=itemView.findViewById(R.id.recyclerImage);
        }

    }
}
