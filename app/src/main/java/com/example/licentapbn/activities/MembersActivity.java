package com.example.licentapbn.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.licentapbn.R;
import com.example.licentapbn.adapters.MemberAdapter;
import com.example.licentapbn.datatype.MemberWithItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        getSupportActionBar().setTitle("Members");
        recyclerView=findViewById(R.id.recycle_view_members);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetching_data));
        progressDialog.setCancelable(false);
        progressDialog.show();
        firestore= FirebaseFirestore.getInstance();
        memberAdapter=new MemberAdapter(MembersActivity.this,membersWithItems);
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
                    String memberID =(String) memberDocument.get("id");
                    String memberEmail =(String) memberDocument.get("email");
                    String memberName = (String) memberDocument.get("name");
                    String memberImageURl= (String) memberDocument.get("imageUrl");
                    MemberWithItems memberWithItems = new MemberWithItems(memberID,memberEmail,memberName, memberPhoneNumber,memberImageURl);
                    membersWithItems.add(memberWithItems);
                    memberAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}