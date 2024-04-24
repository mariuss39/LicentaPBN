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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.datatype.ItemAdapter;
import com.example.licentapbn.datatype.Member;
import com.example.licentapbn.datatype.MemberAdapter;
import com.example.licentapbn.firebase.Callback;
import com.example.licentapbn.firebase.FirebaseSingleton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.DocumentType;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    Button b11;
    List<Item> items = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextView tv3;
    ConstraintLayout layout;
    ItemAdapter itemAdapter;
    FirebaseFirestore firestore;
    String exapandableceva;
    boolean visibility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("fetching data..");
        progressDialog.setCancelable(false);

        progressDialog.show();
        b11 = findViewById(R.id.button22);
        recyclerView=findViewById(R.id.recyclerview_items_container);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firestore=FirebaseFirestore.getInstance();
        itemAdapter=new ItemAdapter(ItemsActivity.this,items);
        recyclerView.setAdapter(itemAdapter);
        
        dataChangdListener();

        Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "S-a bagat inoel in lista", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dataChangdListener() {
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

    public void expandCardView(View view) {
        TransitionManager.beginDelayedTransition(layout,new AutoTransition());
        if(tv3.getVisibility()==View.GONE){
            tv3.setVisibility(View.VISIBLE);
        }
        else{
            tv3.setVisibility(View.GONE);
        }

    }
}



