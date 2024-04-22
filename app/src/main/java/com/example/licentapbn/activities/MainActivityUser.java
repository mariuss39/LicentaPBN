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
    Button logoutButton;
    CardView profileCardview;
    CardView membersCardview;
    CardView itemsCardview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        initializeComponents();
        if(firebaseUser==null){
            Intent loginActivityIntent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivityIntent);
            finish();
        }
        profileCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"ceva",Toast.LENGTH_SHORT).show();
                Intent profileActivityIntent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(profileActivityIntent);

            }
        });
        membersCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"ceva",Toast.LENGTH_SHORT).show();
                Intent membersActivityIntent=new Intent(getApplicationContext(), MembersActivity.class);
                startActivity(membersActivityIntent);

            }
        });
        itemsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"ceva",Toast.LENGTH_SHORT).show();
                Intent itemsActivityIntent=new Intent(getApplicationContext(), ItemsActivity.class);
                startActivity(itemsActivityIntent);

            }
        });
    }
    void initializeComponents(){
        firebaseAuth=FirebaseAuth.getInstance();
        profileCardview=findViewById(R.id.profile_cardview_4th_cardview);
        membersCardview=findViewById(R.id.SearchMembers_cardview_2nd_card);
        itemsCardview=findViewById(R.id.SearchItems_cardview_first_card);
        firebaseUser= firebaseAuth.getCurrentUser();
    }
}