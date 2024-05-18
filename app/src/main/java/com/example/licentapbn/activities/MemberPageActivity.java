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
    TextView tvDate;
    ItemAdapterMemberPageActivity itemAdapterMemberPageActivity;
    List<Item> takenItems=new ArrayList<>();
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    ImageView imageViewMemberPicture;
    String intentPhoneNumber;
    String intentName;
    String intentImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);
        initializeComponents();
       itemsDataChangdListen();

        tvDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if ((new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date())).equals(tvDate.getText().toString())) {

                    for (Item item : takenItems) {
                        item.setStatusVisible(false);
                    }
                } else {
                    for (Item item : takenItems) {
                        item.setStatusVisible(true);
                    }
                }
                itemAdapterMemberPageActivity.notifyDataSetChanged();
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker=MaterialDatePicker.Builder.datePicker().setTitleText("Select date").
                        setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String selectedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(selection));
                        tvDate.setText(selectedDate);
                        String todayDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                        if (selectedDate.equals(todayDate)) {
                            tvDate.setText(todayDate);
                            itemAdapterMemberPageActivity.clearFilter();
                        } else {
                            //
                            firestore.collection("members")
                                    .whereEqualTo("phoneNumber", intentPhoneNumber)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // Obține ID-ul documentului
                                                String memberDocumentId = document.getId();
                                                firestore.collection("members").document(memberDocumentId).collection("reservations")
                                                        .document(String.valueOf(tvDate.getText()))
                                                        .get()
                                                        .addOnCompleteListener(subDocumentTask -> {
                                                            if (subDocumentTask.isSuccessful()) {
                                                                DocumentSnapshot subDocument = subDocumentTask.getResult();
                                                                if (subDocument.exists()) {
                                                                    List<String> reserveditemsIdList = (List<String>) subDocument.get("reservedItemsId");
                                                                    List<Item> filteredItems = new ArrayList<>();
                                                                    for (Item item : takenItems) {
                                                                        if (reserveditemsIdList.contains(item.getId())) {
                                                                            filteredItems.add(item);
                                                                            Log.e("TTTT", item.getId());

                                                                        }
                                                                    }
//                                                                itemAdapterMemberPageActivity.setItems(filteredItems);
                                                                    Log.e("TTTT", " EXISTA DATA1");
                                                                    itemAdapterMemberPageActivity.setItems(filteredItems);
                                                                    Log.e("TTTT", " EXISTA DATA2");

                                                                } else {
                                                                    List<Item> filteredItems = new ArrayList<>();
                                                                    itemAdapterMemberPageActivity.setItems(filteredItems);
                                                                    // Documentul nu există
                                                                    Log.e("TTTT", "NU EXISTA DATA");
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Error getting the information for this day", Toast.LENGTH_LONG).show();
                                                                // Tratează cazul în care nu poți obține documentul
                                                                Log.e("TTTT", "NU pot obtine doc");

                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                        }
                                    });
                            //
                        }
                    }
                });

                materialDatePicker.show(getSupportFragmentManager(),"tag");
            }
        });

    }

    private void initializeComponents() {
        firestore= FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        tvDate=findViewById(R.id.tvDate);
        itemAdapterMemberPageActivity=new ItemAdapterMemberPageActivity(MemberPageActivity.this,takenItems);
        progressDialog.setCancelable(false);
        progressDialog.show();
        tvName=findViewById(R.id.tv_name_MemberPageActivity);
        tvPhoneNumber=findViewById(R.id.tv_phoneNumber_memberPageActivity);
        imageViewMemberPicture=findViewById(R.id.MemberPageMemberImage);
        recyclerView=findViewById(R.id.recyclerview_items_taken_MemberPageActivity);
        recyclerView.setAdapter(itemAdapterMemberPageActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
        Intent intent = getIntent();
        if(intent != null) {
             intentPhoneNumber= intent.getStringExtra("phoneNumber");
            tvPhoneNumber.setText(intentPhoneNumber);
            intentName=intent.getStringExtra("name");
            tvName.setText(intentName);
            intentImageUrl=intent.getStringExtra("imageUrl");
            Glide.with(this).load(intentImageUrl).apply(new RequestOptions().centerCrop()).into(imageViewMemberPicture);
        }
    }

    private void itemsDataChangdListen() {
        firestore.collection(getString(R.string.items)).whereEqualTo("memberName",tvName.getText())
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