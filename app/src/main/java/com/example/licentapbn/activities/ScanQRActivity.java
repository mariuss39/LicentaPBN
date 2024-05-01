package com.example.licentapbn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.health.connect.datatypes.units.Length;
import android.location.SettingInjectorService;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import android.Manifest;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

public class ScanQRActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView tv_scanItems_qr_code;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    private static final String CAMERA_PERMISSION=Manifest.permission.CAMERA;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qractivity);
        scannerView=findViewById(R.id.qr_scanner_view);
        codeScanner=new CodeScanner(this,scannerView);
        tv_scanItems_qr_code=findViewById(R.id.tv_scan_qr_activity);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        checkCameraPermission();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // tv_scanItems_qr_code.setText("Item scanned: "+result.getText());
                        String itemId=result.getText();
                        firestore.collection("items").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        tv_scanItems_qr_code.setText("Item scanned: "+document.getString("name"));
                                        firestore.collection("items").document(itemId).update("memberId", firebaseUser.getUid())
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
                                                                firestore.collection("items").document(itemId).update(updates)
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
                                    }
                                } else {
                                }
                            }
                        });
                    }

                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    private void checkCameraPermission() {
        // Verifică dacă permisiunea pentru camera este acordată
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            codeScanner.startPreview();

        }
    }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permisiunea pentru camera a fost acordată
                    codeScanner.startPreview();
                } else {
                    // Permisiunea a fost refuzată, afișează un mesaj Toast corespunzător
                    Toast.makeText(this, "Camera permission has been denied. Allow permission in your device's settings", Toast.LENGTH_SHORT).show();
                }
            }
        }

}


