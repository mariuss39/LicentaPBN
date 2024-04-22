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
    RecyclerView recyclerView;
    FirebaseSingleton firebaseSingleton;
    MemberAdapter memberAdapter;
    Button b11, b12;
    List<Member> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        firebaseSingleton = FirebaseSingleton.getInstance();
        b11 = findViewById(R.id.button11);
        memberAdapter = new MemberAdapter(getApplicationContext(), members);
        b12 = findViewById(R.id.button12);
        recyclerView = findViewById(R.id.recycle_view_members);
        firebaseSingleton.attachMemberDataChangeEventListener(memberDataChangeCallback());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(memberAdapter);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member m1 = new Member("john");
                firebaseSingleton.insert(m1);
                Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
            }
        });
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private Callback<List<Member>> memberDataChangeCallback() {
        return new Callback<List<Member>>() {
            @Override
            public void runResultOnUiThread(List<Member> result) {
                if (result != null) {
                    members.clear();
                    members.addAll(result);
                    memberAdapter.notifyDataSetChanged();
                }
            }
        };
    }
}