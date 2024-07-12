package com.example.licentapbn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.adapters.ItemAdapterMemberPageActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MemberPageActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvPhoneNumber;
    RecyclerView recyclerView;
    TextView tvEmail;
    ItemAdapterMemberPageActivity itemAdapterMemberPageActivity;
    List<Item> takenItems=new ArrayList<>();
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    ImageView imageViewMemberPicture;
    String intentPhoneNumber;
    String intentName;
    String intentEmail;
    String intentImageUrl;
    String intentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);
        initializeComponents();
       itemsDataChangdListen();
    }

    private void initializeComponents() {
        firestore= FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        itemAdapterMemberPageActivity=new ItemAdapterMemberPageActivity(MemberPageActivity.this,takenItems);
        progressDialog.setCancelable(false);
        progressDialog.show();
        tvName=findViewById(R.id.tv_name_MemberPageActivity);
        tvPhoneNumber=findViewById(R.id.tv_phoneNumber_memberPageActivity);
        tvEmail=findViewById(R.id.tv_email_profileActivity);
        imageViewMemberPicture=findViewById(R.id.MemberPageMemberImage);
        recyclerView=findViewById(R.id.recyclerview_items_taken_MemberPageActivity);
        recyclerView.setAdapter(itemAdapterMemberPageActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if(intent != null) {
            intentEmail=intent.getStringExtra("email");
            intentID=intent.getStringExtra("id");
            tvEmail.setText(intentEmail);
            intentPhoneNumber= intent.getStringExtra("phoneNumber");
            tvPhoneNumber.setText(intentPhoneNumber);
            intentName=intent.getStringExtra("name");
            getSupportActionBar().setTitle(intentName);
            tvName.setText(intentName);
            intentImageUrl=intent.getStringExtra("imageUrl");
            Glide.with(this).load(intentImageUrl).apply(new RequestOptions().centerCrop()).into(imageViewMemberPicture);
        }
    }

    private void itemsDataChangdListen() {
        firestore.collection(getString(R.string.items)).whereEqualTo("memberId",intentID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            return;
                        }
                        if (value != null) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    takenItems.add(dc.getDocument().toObject(Item.class));
                                } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                    String documentId = dc.getDocument().getId();
                                    Item modifiedItem = dc.getDocument().toObject(Item.class);
                                    for (int i = 0; i < takenItems.size(); i++) {
                                        if (takenItems.get(i).getId().equals(documentId)) {
                                            takenItems.set(i, modifiedItem);
                                            break;
                                        }
                                    }
                                } else if (dc.getType() == DocumentChange.Type.REMOVED) {
                                    String documentId = dc.getDocument().getId();
                                    for (int i = 0; i < takenItems.size(); i++) {
                                        if (takenItems.get(i).getId().equals(documentId)) {
                                            takenItems.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        itemAdapterMemberPageActivity.notifyDataSetChanged();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}