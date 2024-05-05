package com.example.licentapbn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import android.Manifest;
import com.example.licentapbn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

public class ScanQRActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView tv_scanItems_qr_code;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qractivity);
        initializeComponents();
        checkCameraPermission();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String itemId=result.getText();
                        firestore.collection(getString(R.string.items)).document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        tv_scanItems_qr_code.setText("Item scanned: "+document.getString("name"));
                                        vibrateShort();
                                        firestore.collection(getString(R.string.items)).document(itemId).update(getString(R.string.memberid), firebaseUser.getUid())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                        } else {
                                                        }
                                                    }
                                                });
                                        firestore.collection(getString(R.string.members)).document(firebaseUser.getUid()).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                String memberName = document.getString("name");
                                                                Map<String, Object> updates = new HashMap<>();
                                                                updates.put("memberName", memberName);
                                                                if(memberName.equals(getString(R.string.storage))){
                                                                    updates.put("free",true);
                                                                }else{
                                                                    updates.put("free",false);
                                                                }
                                                                firestore.collection(getString(R.string.items)).document(itemId).update(updates)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
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

                                    } else {
                                        tv_scanItems_qr_code.setText(R.string.invalid_scan_try_again);
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
                tv_scanItems_qr_code.setText("Scan item");
            }
        });
    }

    private void initializeComponents() {
        getSupportActionBar().setTitle("Scan QR");
        scannerView=findViewById(R.id.qr_scanner_view);
        codeScanner=new CodeScanner(this,scannerView);
        tv_scanItems_qr_code=findViewById(R.id.tv_scan_qr_activity);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            codeScanner.startPreview();

        }
    }
    private void vibrateShort() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
        }
    }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    codeScanner.startPreview();
                } else {
                    Toast.makeText(this, R.string.permission_needed_for_camera_access_allow_permission_in_your_device_s_settings, Toast.LENGTH_SHORT).show();
                }
            }
        }

}


