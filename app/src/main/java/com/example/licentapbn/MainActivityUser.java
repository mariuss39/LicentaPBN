package com.example.licentapbn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityUser extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button logoutButton;
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
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginActivityIntent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginActivityIntent);
                finish();
            }
        });
    }
    void initializeComponents(){
        firebaseAuth=FirebaseAuth.getInstance();
        logoutButton=findViewById(R.id.logout_button_MainActivityUser);
        firebaseUser= firebaseAuth.getCurrentUser();
    }
}