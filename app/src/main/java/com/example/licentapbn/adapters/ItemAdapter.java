package com.example.licentapbn.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;

import java.util.ArrayList;
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
        holder.tvItemName.setText(items.get(position).getName());
        holder.invisibleLayout.setVisibility(View.GONE);
        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);
        holder.item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).isExpanded()) {
                    holder.invisibleLayout.setVisibility(View.GONE);
                    items.get(position).setExpanded(false);

                } else {
                    holder.invisibleLayout.setVisibility(View.VISIBLE);
                    items.get(position).setExpanded(true);
                }
            }
        });

        if(items.get(position).isFree()) {
            holder.tvItemStatus.setText("Available");
            holder.tvItemStatus.setTextColor(Color.GREEN);}
        else{
            holder.tvItemStatus.setText("Unavailable");
            holder.tvItemStatus.setTextColor(Color.RED);
        }
        if(items.get(position).isReserved()){
            holder.tvReservationStatusItem.setText("Reserved");
            holder.tvReservationStatusItem.setTextColor(Color.parseColor("#ffb703"));
        }else{
            holder.tvReservationStatusItem.setText("Available");
            holder.tvReservationStatusItem.setTextColor(Color.GREEN);
        }
        if(items.get(position).isStatusVisible()){
            holder.tvItemStatus.setVisibility(View.VISIBLE);
        }else{
            holder.tvItemStatus.setVisibility(View.GONE);
        }
        if(items.get(position).isReservationVisible()) {
            holder.tvReservationStatusItem.setVisibility(View.VISIBLE);
        }else{
            holder.tvReservationStatusItem.setVisibility(View.GONE);
        }


    }
    public void setItems(List<Item> itemss) {
        this.items = itemss;
        notifyDataSetChanged(); // Notifică RecyclerView despre schimbările făcute în listă
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
        TextView tvItemName,tvItemStatus,tvReservationStatusItem;
        ImageView imageView;
        CardView item_cardview;
        ConstraintLayout invisibleLayout;
        Button btnReserve;
        Button btnCancelReserve;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationStatusItem=itemView.findViewById(R.id.tv_reservation_status_item);
            tvItemName= itemView.findViewById(R.id.tv_name_item);
            tvItemStatus = itemView.findViewById(R.id.tv_status_item);
            item_cardview=itemView.findViewById(R.id.item_cardview);
            invisibleLayout=itemView.findViewById(R.id.invisible_constraint_layout);
            imageView=itemView.findViewById(R.id.recyclerImage);
            btnReserve=itemView.findViewById(R.id.tv_reserve);
            btnCancelReserve=itemView.findViewById(R.id.tv_cancel_reserve);

        }

    }
}
