package com.example.licentapbn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.licentapbn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText tiet_username;
    TextInputEditText tiet_password;
    Button button_login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent loginSuccessfulIntent=new Intent(getApplicationContext(),MainActivityUser.class);
            startActivity(loginSuccessfulIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();
        tiet_username.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String username = String.valueOf(tiet_username.getText());
                if (!isValidUsername(username)) {
                    tiet_username.setError(getString(R.string.invalid_email_format));
                } else {
                    tiet_username.setError(null);
                }
            }
        });
        tiet_password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = String.valueOf(tiet_password.getText());
                if (password.length() < 6) {
                    tiet_password.setError(getString(R.string.password_must_be_at_least_6_characters));
                } else {
                    tiet_password.setError(null);
                }
            }
        });

        tiet_password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                button_login.performClick();
                return true;
            }
            return false;
        });

        button_login.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String username,password;
            username=String.valueOf(tiet_username.getText());
            password=String.valueOf(tiet_password.getText());
            if(TextUtils.isEmpty(username)){
                Toast.makeText(getApplicationContext(), R.string.email_can_t_be_null,Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(getApplicationContext(), R.string.password_can_t_be_null,Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),R.string.welcome,Toast.LENGTH_SHORT).show();
                            Intent loginSuccessfulIntent=new Intent(getApplicationContext(),MainActivityUser.class);
                            startActivity(loginSuccessfulIntent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    private boolean isValidUsername(String username) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    void initializeComponents(){
        getSupportActionBar().hide();
        getWindow().setNavigationBarColor(Color.BLACK);
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