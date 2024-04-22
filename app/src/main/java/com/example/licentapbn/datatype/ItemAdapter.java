package com.example.licentapbn.datatype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licentapbn.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    Context context;
    List<Item> items;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tv1.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
