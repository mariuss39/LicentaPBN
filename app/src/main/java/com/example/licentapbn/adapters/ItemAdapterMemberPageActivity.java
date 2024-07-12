package com.example.licentapbn.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapterMemberPageActivity extends RecyclerView.Adapter<ItemAdapterMemberPageActivity.ItemHolder>{

    Context context;
    List<Item> items;
    List<Item> itemsCopy;

    public ItemAdapterMemberPageActivity(Context context, List<Item> items) {
        this.context = context;
        this.itemsCopy=items;
        this.items =this.itemsCopy;
    }
    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged(); // Notifică RecyclerView despre schimbările făcute în listă
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_member_page_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvname.setText(items.get(position).getName());
        holder.tvid.setText("Id:"+items.get(position).getId());
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
        TextView tvname,tvid;
        ImageView imageView;
        CardView item_cardview;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvname= itemView.findViewById(R.id.tv_name_item_memberPage);
            tvid= itemView.findViewById(R.id.tv_id_recycler_memberpage);
            item_cardview=itemView.findViewById(R.id.item_cardview_memberPage);
            imageView=itemView.findViewById(R.id.recyclerMemberPageImage);
        }

    }
}
