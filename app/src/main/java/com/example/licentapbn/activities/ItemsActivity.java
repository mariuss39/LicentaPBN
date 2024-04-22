package com.example.licentapbn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.datatype.ItemAdapter;
import com.example.licentapbn.datatype.Member;
import com.example.licentapbn.datatype.MemberAdapter;
import com.example.licentapbn.firebase.Callback;
import com.example.licentapbn.firebase.FirebaseSingleton;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseSingleton firebaseSingleton;
    ItemAdapter itemAdapter;
    Button b11, b12;
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        recyclerView = findViewById(R.id.recycleview_items);
        firebaseSingleton = FirebaseSingleton.getInstance();
        b11 = findViewById(R.id.button22);
        itemAdapter = new ItemAdapter(getApplicationContext(), items);
        b12 = findViewById(R.id.button222);

        firebaseSingleton.attachItemDataChangeEventListener(itemChangedCallback());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(itemAdapter);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item=new Item("boxa rcf 915");
                firebaseSingleton.insertItem(item);
                Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
            }
        });
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private Callback<List<Item>> itemChangedCallback() {
            return new Callback<List<Item>>() {
                @Override
                public void runResultOnUiThread(List<Item> result) {
                    if (result != null) {
                        items.clear();
                        items.addAll(result);
                        itemAdapter.notifyDataSetChanged();
                    }
                }
            };
    }

}


