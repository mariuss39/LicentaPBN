package com.example.licentapbn.datatype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tv2.setText(String.valueOf(items.get(position).isFree()));
        holder.tv3.setText(items.get(position).getDescription());
        holder.neexpandatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).isExpanded() == true) {
                    holder.expandableLayout.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                    items.get(position).setExpanded(false);

                } else {
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                    holder.tv3.setVisibility(View.VISIBLE);
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
        TextView tv1,tv2,tv3;
        ConstraintLayout expandableLayout,neexpandatLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv_name_item);
            tv2 = itemView.findViewById(R.id.tv_status_item);
            tv3=itemView.findViewById(R.id.tv_details);
            expandableLayout=itemView.findViewById(R.id.expanda_constraint);
            neexpandatLayout=itemView.findViewById(R.id.constraint_neexpandat);
        }

    }
}
