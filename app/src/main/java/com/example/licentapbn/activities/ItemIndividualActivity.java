package com.example.licentapbn.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ItemIndividualActivity extends AppCompatActivity {

    Button historyButton;
    String itemId;
    ImageView itemImage;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    TextView tvName,tvId,tvDescription,tvWeight,tvSize,tvTakenby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("veva","A");
        setContentView(R.layout.activity_item_individual);
        initializeComponents();
        Log.e("veva","B");

        Intent intent = getIntent();
        String description = intent.getStringExtra("description");
        String id = intent.getStringExtra("id");
        String size = intent.getStringExtra("size");
        String weight = intent.getStringExtra("weight");
        String name = intent.getStringExtra("name");
        String memberName = intent.getStringExtra("memberName");
        String memberId = intent.getStringExtra("memberId");
        String imageUrl = intent.getStringExtra("imageUrl");
        tvName.setText(name);
        getSupportActionBar().setTitle(name);
        tvId.setText("Id: "+id);
        itemId= id;
        tvDescription.setText(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvDescription.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        tvSize.setText("Size: "+size);
        tvWeight.setText("Weight: "+weight);
        tvTakenby.setText("Taken by: "+memberName);
        Glide.with(this).load(imageUrl).into(itemImage);
        Log.e("veva","c");
        Log.e("veva","d");
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadOptions();
            }
        });

    }
    private void showDownloadOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an option");
        builder.setItems(new CharSequence[]{"Reservations", "Borrows"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                handleReservationsDownload();
                                break;
                            case 1:
                                handleBorrowsDownload();
                                break;
                        }
                    }
                });
        builder.show();
    }
    private void handleBorrowsDownload(){
        firestore.collection("items").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> dataArray = (List<String>) document.get("takingHistory");
                                if (dataArray != null) {
                                    Log.e("ceva","ceva11");
                                    saveBorrowsToFile(dataArray);
                                }
                            }
                        }
                    }
                });
    }
    private void saveBorrowsToFile(List<String> array) {
        Log.e("veva", "ceva10");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : array) {
            // Split each string into its components
            String[] parts = item.split("-");
            if (parts.length == 5) {
                String id = parts[0];
                String action = parts[3];
                String date = parts[4];
                Log.e("ceva", "ceva12");

                // Format the string as desired
                stringBuilder.append("Item id: ").append(id).append("\n");
                Log.e("ceva", "ceva13");
                //stringBuilder.append("Item Name: ").append(items.get(Integer.parseInt(id)).getName()).append("\n");
                Log.e("ceva", "ceva4");
                stringBuilder.append("Action type: ").append(action).append("\n");
                stringBuilder.append("Date initialized: ").append(date).append("\n");
                stringBuilder.append("---------------\n");
                Log.e("ceva", "ceva5");
            } else {
                stringBuilder.append(item).append("\n");
            }
        }
        String dataString = stringBuilder.toString();
        LocalDate today = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        try {
            // Create a file in the Downloads directory
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDirectory.exists()) {
                downloadsDirectory.mkdirs();
                Log.e("ceva", "ceva1");
            }

            String fileName = "borrows_" + today.toString() + ".txt";
            File file = new File(downloadsDirectory, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dataString.getBytes());
            fos.close();
            Log.e("ceva", "ceva2");
            Toast.makeText(this, "File saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            openFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
            Log.e("ceva", "ceva3");
        }
    }
    private void handleReservationsDownload(){
        firestore.collection("items").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> dataArray = (List<String>) document.get("reservationsHistory");
                        if (dataArray != null) {

                            saveReservationsToFile(dataArray);
                        }
                    }
                }
            }
        });


    }
    private void saveReservationsToFile(List<String> array) {
        Log.e("ceva", "ceva10");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : array) {
            // Split each string into its components
            String[] parts = item.split("-");
            if (parts.length == 6) {
                String itemID = parts[0];
                String memberName = parts[1];
                String memberID = parts[2];
                String action = parts[3];
                String dateInitialized = parts[4];
                String forDate = parts[5];
                Log.e("ceva", "ceva12");

                // Format the string as desired
                stringBuilder.append("Item id: ").append(itemID).append("\n");
                stringBuilder.append("Member Name: ").append(memberName).append("\n");
                Log.e("ceva", "ceva13");
                //stringBuilder.append("Item Name: ").append(items.get(Integer.parseInt(id)).getName()).append("\n");
                Log.e("ceva", "ceva4");
                stringBuilder.append("Action: ").append(action).append("\n");
                stringBuilder.append("Date initialized: ").append(dateInitialized).append("\n");
                stringBuilder.append("For date: ").append(forDate).append("\n");
                stringBuilder.append("---------------\n");
                Log.e("ceva", "ceva5");
            } else {
                stringBuilder.append(item).append("\n");
            }
        }
        String dataString = stringBuilder.toString();
        LocalDate today = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        try {
            // Create a file in the Downloads directory
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDirectory.exists()) {
                downloadsDirectory.mkdirs();
                Log.e("ceva", "ceva1");
            }

            String fileName = "Item_" + tvName.getText() + "_reservations_" + today.toString() + ".txt";
            File file = new File(downloadsDirectory, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dataString.getBytes());
            fos.close();
            Log.e("ceva", "ceva2");
            Toast.makeText(this, "File saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            openFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
            Log.e("ceva", "ceva3");
        }
    }
    private void openFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "text/plain");  // Setează tipul MIME adecvat fișierului tău
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  // Acordă permisiuni de citire

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeComponents() {
        tvName=findViewById(R.id.indivialItemName);
        tvId=findViewById(R.id.indiviaulIdItem);
        historyButton=findViewById(R.id.historyButton);
        tvDescription=findViewById(R.id.individualDescription);
        tvSize=findViewById(R.id.individualsize);
        tvWeight=findViewById(R.id.individualWeight);
        tvTakenby=findViewById(R.id.takenby);
        itemImage=findViewById(R.id.individualImage);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }
}