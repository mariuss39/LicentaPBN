package com.example.licentapbn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityUser extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button logoutButton;
    CardView profile_cardview;
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
        profile_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"ceva",Toast.LENGTH_SHORT).show();
                Intent profileActivityIntent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(profileActivityIntent);

            }
        });
    }
    void initializeComponents(){
        firebaseAuth=FirebaseAuth.getInstance();
        profile_cardview=findViewById(R.id.profile_cardview_4th_cardview);
        firebaseUser= firebaseAuth.getCurrentUser();
    }
}