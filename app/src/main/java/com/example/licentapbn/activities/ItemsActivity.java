package com.example.licentapbn.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.datatype.ItemAdapter;
import com.example.licentapbn.datatype.Member;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.DocumentType;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    List<Item> items = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Button button_filter_available_items;
    Button button_reset_filter_items;
    Button button_filter_unavailable_items;
    ItemAdapter itemAdapter;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        initializeComponents();
        itemsDataChangdListen();
        setFilterButtonsOnClickListeners();

    }

    private void setFilterButtonsOnClickListeners() {
        button_filter_available_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdapter.clearFilter();
                itemAdapter.filterItemsByFree(true);
            }
        });
        button_reset_filter_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdapter.clearFilter();
            }
        });
        button_filter_unavailable_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdapter.clearFilter();
                itemAdapter.filterItemsByFree(false);
            }
        });
    }

    public void initializeComponents(){
        button_filter_available_items=findViewById(R.id.button_filter_availabe_items);
        button_reset_filter_items=findViewById(R.id.button_reset_filter_items);
        button_filter_unavailable_items=findViewById(R.id.button_filter_unavailable_items);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("fetching data..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        recyclerView=findViewById(R.id.recyclerview_items_container);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firestore=FirebaseFirestore.getInstance();
        itemAdapter=new ItemAdapter(ItemsActivity.this,items);
        recyclerView.setAdapter(itemAdapter);
    }
    private void itemsDataChangdListen() {
        firestore.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Log.e("firestore errot", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        items.add(dc.getDocument().toObject(Item.class));
                    } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                        String documentId = dc.getDocument().getId();
                        Item modifiedItem = dc.getDocument().toObject(Item.class);
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getId().equals(documentId)) {
                                items.set(i, modifiedItem);
                                break;
                            }
                        }
                    } else if (dc.getType() == DocumentChange.Type.REMOVED) {
                        String documentId = dc.getDocument().getId();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getId().equals(documentId)) {
                                items.remove(i);
                                break;
                            }
                        }
                    }
                    itemAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}



