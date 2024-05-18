package com.example.licentapbn.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.adapters.ItemAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentChange;
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
import java.util.Map;

public class ItemsActivity extends AppCompatActivity {
    List<Item> items = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Button button_filter_available_items;
    Button button_reset_filter_items;
    Button button_filter_unavailable_items;
    String todayDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
    ItemAdapter itemAdapter;
    FirebaseFirestore firestore;
    TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        initializeComponents();
        itemsDataChangdListen();
        setFilterButtonsOnClickListeners();
        tvDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                List<Item> filteredItems =new ArrayList<>(items);
                //for Today
                if (todayDate.equals(tvDate.getText().toString())) {
                    for (Item item : filteredItems) {
                        if (!item.isFree()){
                            item.setStatusVisible(true);
                            Map<String, List<String>> mapField = item.getReservationsMap();
                            if (mapField != null && mapField.containsKey(todayDate)) {
                                item.setReserved(true);
                                item.setReservationVisible(true);
                            }else{
                                item.setReservationVisible(false);
                            }
                        }else{
                                Map<String, List<String>> mapField = item.getReservationsMap();
                                if(mapField!=null && mapField.containsKey(todayDate)){
                                    item.setReserved(true);
                                    item.setReservationVisible(true);
                                    item.setStatusVisible(false);
                                }else{
                                    item.setReserved(false);
                                    item.setStatusVisible(true);
                                    item.setReservationVisible(false);
                                }
                        }

                    }
                }
                else{
                    for(Item item:filteredItems){
                        Map<String, List<String>> mapField = item.getReservationsMap();
                        if((mapField!=null && mapField.containsKey(tvDate.getText().toString()))){
                            item.setStatusVisible(false);
                            item.setReserved(true);
                            item.setReservationVisible(true);
                        }
                        else{
                            item.setStatusVisible(false);
                            item.setReserved(false);
                            item.setReservationVisible(true);
                        }
                    }
                }
                itemAdapter.setItems(filteredItems);
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
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(),"tag");
            }
        });
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
        getSupportActionBar().setTitle("Search items");
        button_filter_available_items=findViewById(R.id.button_filter_availabe_items);
        button_reset_filter_items=findViewById(R.id.button_reset_filter_items);
        button_filter_unavailable_items=findViewById(R.id.button_filter_unavailable_items);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        recyclerView=findViewById(R.id.recyclerview_items_container);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firestore=FirebaseFirestore.getInstance();
        itemAdapter=new ItemAdapter(ItemsActivity.this,items);
        recyclerView.setAdapter(itemAdapter);
        tvDate=findViewById(R.id.tvDate_itemsactivity);
        tvDate.setText(todayDate);
    }
    private void itemsDataChangdListen() {
        firestore.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    return;
                }
                if(value!=null){
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
                }
                }
                itemAdapter.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
}



