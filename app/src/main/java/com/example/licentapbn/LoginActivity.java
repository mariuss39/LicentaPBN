package com.example.licentapbn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText tiet_username;
    TextInputEditText tiet_password;
    Button button_login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent loginSuccessfulIntent=new Intent(getApplicationContext(),MainActivityUser.class);
            startActivity(loginSuccessfulIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String username,password;
                username=String.valueOf(tiet_username.getText());
                password=String.valueOf(tiet_password.getText());
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(),"Email can't be null",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Password can't be null",Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_SHORT).show();
                                    Intent loginSuccessfulIntent=new Intent(getApplicationContext(),MainActivityUser.class);
                                    startActivity(loginSuccessfulIntent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    void initializeComponents(){
        firebaseAuth=FirebaseAuth.getInstance();
        CardView cardView = findViewById(R.id.card_view_login_form_login);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.login_cardview_slideup_login);
        cardView.startAnimation(animation);
        tiet_username=findViewById(R.id.et_username_login);
        tiet_password=findViewById(R.id.et_password_login);
        button_login=findViewById(R.id.button_login);
        progressBar=findViewById(R.id.progress_bar_loginActivity);
    }
}