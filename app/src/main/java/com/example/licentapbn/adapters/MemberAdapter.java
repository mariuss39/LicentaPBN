package com.example.licentapbn.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.licentapbn.activities.MemberPageActivity;
import com.example.licentapbn.datatype.MemberWithItems;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    Context context;
    List<MemberWithItems> membersWithItems=new ArrayList<>();

    public MemberAdapter(Context context, List<MemberWithItems> members) {
        this.context = context;
        this.membersWithItems = members;

    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberHolder(LayoutInflater.from(context).inflate(R.layout.members_recyclerview_item_visible_cardview_members_activity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        MemberWithItems memberWithItems=membersWithItems.get(position);
        holder.tv_name_member.setText(memberWithItems.getName());
        holder.tv_phoneNumber_member.setText(memberWithItems.getPhoneNumber());
        Glide.with(context).load(memberWithItems.getImageUrl()).into(holder.member_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide o nouă activitate când elementul este apăsat
                Intent memberPageIntent = new Intent(context, MemberPageActivity.class);
                memberPageIntent.putExtra("phoneNumber",membersWithItems.get(position).getPhoneNumber());
                memberPageIntent.putExtra("name",membersWithItems.get(position).getName());
                memberPageIntent.putExtra("imageUrl",membersWithItems.get(position).getImageUrl());
                context.startActivity(memberPageIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return membersWithItems.size();
    }
    public class MemberHolder extends RecyclerView.ViewHolder {
        CardView member_cardview_clickable;
        TextView tv_name_member;
        TextView tv_phoneNumber_member;
        ImageView member_image;


        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            member_cardview_clickable=itemView.findViewById(R.id.item_cardview);
            tv_name_member=itemView.findViewById(R.id.tv_name_member_membersActivity);
            tv_phoneNumber_member=itemView.findViewById(R.id.tv_phoneNumber_member_membersActivity);
            member_image=itemView.findViewById(R.id.imageview_members_picture_MembersActivity);
        }
    }
}

