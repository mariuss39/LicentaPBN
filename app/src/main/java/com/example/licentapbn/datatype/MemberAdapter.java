package com.example.licentapbn.datatype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licentapbn.R;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    Context context;
    List<Member> members;

    public MemberAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberHolder(LayoutInflater.from(context).inflate(R.layout.members_recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        holder.tv1.setText(members.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
    public class MemberHolder extends RecyclerView.ViewHolder {
        TextView tv1;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv_name);
        }
    }
}
