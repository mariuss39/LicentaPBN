package com.example.licentapbn.datatype;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licentapbn.R;

public class MemberHolder extends RecyclerView.ViewHolder {
    TextView tv1;

    public MemberHolder(@NonNull View itemView) {
        super(itemView);
        tv1=itemView.findViewById(R.id.tv_name);
    }
}

