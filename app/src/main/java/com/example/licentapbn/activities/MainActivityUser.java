package com.example.licentapbn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.licentapbn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityUser extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CardView profileCardview;
    CardView membersCardview;
    CardView qrScannerCardview;
    CardView manualScanCardView;
    CardView itemsCardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        initializeComponents();
        setCardviewListeners();
        if(firebaseUser==null){
            Intent loginActivityIntent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivityIntent);
            finish();
        }
    }

    private void setCardviewListeners() {
        profileCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivityIntent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(profileActivityIntent);

            }
        });
        membersCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent membersActivityIntent=new Intent(getApplicationContext(), MembersActivity.class);
                startActivity(membersActivityIntent);
            }
        });
        manualScanCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manualScanActivityIntent=new Intent(getApplicationContext(), ManualScanActivity.class);
                startActivity(manualScanActivityIntent);
            }
        });
        qrScannerCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrScannerActivityIntent=new Intent(getApplicationContext(), ScanQRActivity.class);
                startActivity(qrScannerActivityIntent);
            }
        });
        itemsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemsActivityIntent=new Intent(getApplicationContext(), ItemsActivity.class);
                startActivity(itemsActivityIntent);
            }
        });
    }

    void initializeComponents(){
        getSupportActionBar().setTitle(R.string.dashboard);
        firebaseAuth=FirebaseAuth.getInstance();
        profileCardview=findViewById(R.id.profile_cardview_4th_cardview);
        membersCardview=findViewById(R.id.SearchMembers_cardview_2nd_card);
        itemsCardview=findViewById(R.id.SearchItems_cardview_first_card);
        manualScanCardView=findViewById(R.id.manual_search_cardview_3rd_carview);
        firebaseUser= firebaseAuth.getCurrentUser();
        qrScannerCardview=findViewById(R.id.qr_scanner_cardview_5h_cardview);
    }
}