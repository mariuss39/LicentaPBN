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
    List<Item> items = new ArrayList<>();
    List<Member> members=new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextView tv3;
    ItemAdapter itemAdapter;
    MemberAdapter memberAdapter;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

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
        memberAdapter=new MemberAdapter(ItemsActivity.this,members);
        itemsDataChangdListener();
        membersDataChangdListener();
    }

    private void itemsDataChangdListener() {
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
    private void membersDataChangdListener() {
        firestore.collection("members").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        members.add(dc.getDocument().toObject(Member.class));
                    } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                        Toast.makeText(getApplicationContext(),"proba",Toast.LENGTH_SHORT).show();
                        String documentId = dc.getDocument().getId();
                        Member modifiedMember = dc.getDocument().toObject(Member.class);
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).getId().equals(documentId)) {
                                members.set(i, modifiedMember);
                                break;
                            }
                        }
                    } else if (dc.getType() == DocumentChange.Type.REMOVED) {
                        String documentId = dc.getDocument().getId();
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).getId().equals(documentId)) {
                                members.remove(i);
                                break;
                            }
                        }
                    }
                    memberAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}



