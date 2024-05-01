package com.example.licentapbn.datatype;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    Context context;
    List<MemberWithItems> membersWithItems=new ArrayList<>();
    List<Item> itemsOwned=new ArrayList<>();

    public MemberAdapter(Context context, List<MemberWithItems> members,List<Item> items) {
        this.context = context;
        this.membersWithItems = members;
        this.itemsOwned=items;
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
        itemsOwned=memberWithItems.getItemsOwned();
        holder.tv_itemsOwned_member.setText("Items taken: "+itemsOwned.size());
        Glide.with(context).load(memberWithItems.getImageUrl()).into(holder.member_image);
        boolean isExpandable=memberWithItems.isExpandable();
        holder.invisible_layout.setVisibility(isExpandable?View.VISIBLE:View.GONE);
        NestedMemberAdapter nestedMemberAdapter = new NestedMemberAdapter(context,itemsOwned);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(nestedMemberAdapter);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberWithItems.setExpandable(!memberWithItems.isExpandable());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return membersWithItems.size();
    }
    public class MemberHolder extends RecyclerView.ViewHolder {
        CardView member_cardview_clickable;
        private LinearLayout linearLayout;
        RelativeLayout invisible_layout;
        TextView tv_name_member;
        TextView tv_phoneNumber_member;
        TextView tv_itemsOwned_member;
        RecyclerView nestedRecyclerView;
        ImageView member_image;


        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            member_cardview_clickable=itemView.findViewById(R.id.item_cardview);
            linearLayout=itemView.findViewById(R.id.linear_layout);
            invisible_layout=itemView.findViewById(R.id.expandable_layout);
            tv_name_member=itemView.findViewById(R.id.tv_name_member_membersActivity);
            tv_phoneNumber_member=itemView.findViewById(R.id.tv_phoneNumber_member_membersActivity);
            tv_itemsOwned_member=itemView.findViewById(R.id.tv_items_owned_member_membersActivity);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
            member_image=itemView.findViewById(R.id.imageview_members_picture_MembersActivity);


        }
    }
}
