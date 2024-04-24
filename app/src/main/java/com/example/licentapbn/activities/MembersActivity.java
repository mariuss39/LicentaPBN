package com.example.licentapbn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Member;
import com.example.licentapbn.datatype.MemberAdapter;
import com.example.licentapbn.firebase.Callback;
import com.example.licentapbn.firebase.FirebaseSingleton;

import java.util.ArrayList;
import java.util.List;

public class MembersActivity extends AppCompatActivity {
    Button b11, b12;
    List<Member> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        b11 = findViewById(R.id.button11);
        b12 = findViewById(R.id.button12);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
            }
        });
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}