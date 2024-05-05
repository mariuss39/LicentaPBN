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

//    private void itemsDataChangdListen() {
//        firestore.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
////                    if (progressDialog.isShowing()) {
////                        progressDialog.dismiss();
////                    }
//                    Log.e("firestore errot", error.getMessage());
//                    return;
//                }
//                if (value != null) {
//                    Log.e("JAIL","jail1");
//                    for (DocumentSnapshot doc : value.getDocuments()) {
//                        // Convertim fiecare document Firestore într-un obiect Java și îl adăugăm în lista items
//                        Item item = doc.toObject(Item.class);
//                        items.add(item);
//                        Log.e("JAIL", String.valueOf(items.size()));
//
//                    }
//                }else{
//                for (DocumentChange dc : value.getDocumentChanges()) {
//                    if (dc.getType() == DocumentChange.Type.ADDED) {
//                        items.add(dc.getDocument().toObject(Item.class));
//                    } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
//                        String documentId = dc.getDocument().getId();
//                        Item modifiedItem = dc.getDocument().toObject(Item.class);
//                        for (int i = 0; i < items.size(); i++) {
//                            if (items.get(i).getId().equals(documentId)) {
//                                items.set(i, modifiedItem);
//                                break;
//                            }
//                        }
//                    } else if (dc.getType() == DocumentChange.Type.REMOVED) {
//                        String documentId = dc.getDocument().getId();
//                        for (int i = 0; i < items.size(); i++) {
//                            if (items.get(i).getId().equals(documentId)) {
//                                items.remove(i);
//                                break;
//                            }
//                        }
//                    }
////                    memberAdapter.notifyDataSetChanged();
////                    if (progressDialog.isShowing()) {
////                        progressDialog.dismiss();
////                    }
//                }
//                }
//            }
//        });
//    }
//    private void membersDataChangeListen() {
//        firestore.collection("members").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
////                    if (progressDialog.isShowing()) {
////                        progressDialog.dismiss();
////                    }
//                    Log.e("firestore errot", error.getMessage());
//                    return;
//                }
//                if (value != null) {
//                    Log.e("JAILMEMBERU","jail1");
//                    for (DocumentSnapshot doc : value.getDocuments()) {
//                        // Convertim fiecare document Firestore într-un obiect Java și îl adăugăm în lista items
//                        Member member = doc.toObject(Member.class);
//                        members.add(member);
//                        Log.e("JAILMEMBru", String.valueOf(members.size()));
//                        }
//                    }else{
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                members.add(dc.getDocument().toObject(Member.class));
//                            } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
//                                String documentId = dc.getDocument().getId();
//                                Member modifiedMembers = dc.getDocument().toObject(Member.class);
//                                for (int i = 0; i < items.size(); i++) {
//                                    if (members.get(i).getId().equals(documentId)) {
//                                        members.set(i, modifiedMembers);
//                                        break;
//                                    }
//                                }
//                            } else if (dc.getType() == DocumentChange.Type.REMOVED) {
//                                String documentId = dc.getDocument().getId();
//                                for (int i = 0; i < members.size(); i++) {
//                                    if (members.get(i).getId().equals(documentId)) {
//                                        members.remove(i);
//                                        break;
//                                    }
//                                }
//                            }
////                            memberAdapter.notifyDataSetChanged();
////                            if (progressDialog.isShowing()) {
////                                progressDialog.dismiss();
////                            }
//                        }
//                    }
//            }
//        });
//    }
}