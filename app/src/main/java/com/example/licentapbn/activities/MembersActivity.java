package com.example.licentapbn.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.datatype.ItemAdapter;
import com.example.licentapbn.datatype.Member;
import com.example.licentapbn.datatype.MemberAdapter;
import com.example.licentapbn.datatype.MemberWithItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MembersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    List<Item> items=new ArrayList<>();
    MemberAdapter memberAdapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    List<MemberWithItems> membersWithItems=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        initializeComponents();
        membersWithItemsDataChangedListen();
    }
    private void initializeComponents(){
        getSupportActionBar().setTitle("Search members");
        recyclerView=findViewById(R.id.recycle_view_members);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetching_data));
        progressDialog.setCancelable(false);
        progressDialog.show();
        firestore= FirebaseFirestore.getInstance();
        memberAdapter=new MemberAdapter(MembersActivity.this,membersWithItems,items);
        recyclerView.setAdapter(memberAdapter);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


    }
    private void membersWithItemsDataChangedListen() {
        firestore.collection("members").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot membersResult, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    return;

                }
                membersWithItems.clear();

                for (QueryDocumentSnapshot memberDocument : membersResult) {

                    String memberPhoneNumber =(String) memberDocument.get("phoneNumber");
                    String memberName = (String) memberDocument.get("name");
                    String memberImageURl= (String) memberDocument.get("imageUrl");

                    firestore.collection("items")
                            .whereEqualTo("memberId", memberDocument.getId())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot itemsResult) {
                                    List<Item> itemsAux = new ArrayList<>();
                                    for (QueryDocumentSnapshot itemDocument : itemsResult) {
                                        String itemName = (String) itemDocument.get("name");
                                        String imageUrl= (String) itemDocument.get("imageUrl");
                                        Item item = new Item( itemName, imageUrl);
                                        itemsAux.add(item);
                                    }
                                    MemberWithItems memberWithItems = new MemberWithItems(memberName, memberPhoneNumber, itemsAux,memberImageURl);
                                    membersWithItems.add(memberWithItems);
                                    memberAdapter.notifyDataSetChanged();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                }
                            });
                }
            }
        });
    }
}