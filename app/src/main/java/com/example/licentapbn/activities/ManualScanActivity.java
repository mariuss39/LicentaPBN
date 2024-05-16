package com.example.licentapbn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licentapbn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManualScanActivity extends AppCompatActivity {
    Button button_manual_scan;
    TextInputEditText tiet_manual_scan;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    String insertedId;
    TextView tv_item_manual_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_scan);
        initializaComponents();
        button_manual_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertedId= String.valueOf(tiet_manual_scan.getText());
                tiet_manual_scan.setText("");
                tiet_manual_scan.clearFocus();
                InputMethodManager keyboardManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboardManager.hideSoftInputFromWindow(tiet_manual_scan.getWindowToken(), 0);
                if(!insertedId.equals("")){
                    updateItem();
                }
            }
        });
    }

    private void updateItem() {
        firestore.collection("items").document(insertedId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tv_item_manual_scan.setText("Item scanned: "+document.getString("name"));
                        Toast.makeText(getApplicationContext(), R.string.item_scanned_successfully,Toast.LENGTH_SHORT).show();
                        firestore.collection("items").document(insertedId).update("memberId", firebaseUser.getUid())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                        } else {
                                        }
                                    }
                                });
                        firestore.collection("members").document(firebaseUser.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String memberName = document.getString("name");
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("memberName", memberName);
                                                if(memberName.equals("Storage")){
                                                    updates.put("free",true);
                                                }else{
                                                    updates.put("free",false);
                                                }
                                                firestore.collection("items").document(insertedId).update(updates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Actualizarea a fost finalizată cu succes
                                                                } else {
                                                                    // A apărut o eroare la actualizare
                                                                }
                                                            }
                                                        });
                                            } else {
                                            }
                                        } else {
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid id",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Invalid id2",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializaComponents() {
        getSupportActionBar().setTitle(R.string.manual_scan_action_bar);
        button_manual_scan=findViewById(R.id.button_manual_scan);
        tiet_manual_scan=findViewById(R.id.tiet_manual_scan);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        tv_item_manual_scan=findViewById(R.id.tv_manual_scan);
    }
}