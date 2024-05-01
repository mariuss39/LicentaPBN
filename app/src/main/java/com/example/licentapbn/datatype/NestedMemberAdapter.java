package com.example.licentapbn.datatype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;

import java.util.List;

public class NestedMemberAdapter extends RecyclerView.Adapter<NestedMemberAdapter.NestedViewHolder> {

        private List<Item> items;
        Context context;

        public NestedMemberAdapter(Context context,List<Item> items){
            this.context=context;
            this.items = items;
        }

        @NonNull
        @Override
        public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.members_recyclerview_nested_item_members_activity , parent , false);
            return new NestedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
            holder.nested_tv_item_name.setText(items.get(position).getName());
            Glide.with(context).load(items.get(position).getImageUrl()).into(holder.nested_imageview_item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class NestedViewHolder extends RecyclerView.ViewHolder{
            private TextView nested_tv_item_name;
            private ImageView nested_imageview_item;
            public NestedViewHolder(@NonNull View itemView) {
                super(itemView);
                nested_tv_item_name = itemView.findViewById(R.id.tv_item_name_nested_cardview);
                nested_imageview_item=itemView.findViewById(R.id.imageview_item_nested_cardview);
            }
        }
    }

