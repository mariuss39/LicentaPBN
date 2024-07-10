package com.example.licentapbn.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.adapters.ItemAdapterProfileActivity;
import com.example.licentapbn.datatype.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    Button button_logout;
    Button button_history;
    AnstronCoreHelper anstronCoreHelper;
    Button button_change_profile_picture;
    List<Item> items=new ArrayList<>();
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    ItemAdapterProfileActivity itemAdapterProfileActivity;
    ProgressDialog progressDialog;
    TextView tv_name;
    ImageView imageView_profile_picture;
    TextView tv_email;
    RecyclerView recyclerView;
    Uri imageUri;
    TextView tvItemsOwned;
    TextView tv_phoneNumber;
    static final int IMAGE_REQUEST=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeComponents();
        profileDataInitialize();
        profileDataChangeListen();
        itemsDataChangdListen();
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logOutIntent= new Intent(ProfileActivity.this,LoginActivity.class);
                logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logOutIntent);
                finish();
            }
        });
        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadOptions();
            }
        });
        button_change_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
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

    private void handleReservationsDownload() {

        firestore.collection("members").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> dataArray = (List<String>) document.get("reservationsHistory");
                        if (dataArray != null) {
                            Log.e("ceva","ceva11");
                            saveReservationsToFile(dataArray);
                        }
                    }
                }
            }
        });
    }

    private void handleBorrowsDownload() {
        firestore.collection("members").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    private void saveReservationsToFile(List<String> array){
        Log.e("ceva","ceva10");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : array) {
            // Split each string into its components
            String[] parts = item.split("-");
            if (parts.length == 4) {
                String id = parts[0];
                String type = parts[1];
                String action = parts[2];
                String date = parts[3];
                Log.e("ceva","ceva12");

                // Format the string as desired
                stringBuilder.append("Item id: ").append(id).append("\n");
                Log.e("ceva","ceva13");
                //stringBuilder.append("Item Name: ").append(items.get(Integer.parseInt(id)).getName()).append("\n");
                Log.e("ceva","ceva4");
                stringBuilder.append("Action type: ").append(type).append("\n");
                stringBuilder.append("Date initialized: ").append(action).append("\n");
                stringBuilder.append("For date: ").append(date).append("\n");
                stringBuilder.append("---------------\n");
                Log.e("ceva","ceva5");
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
                Log.e("ceva","ceva1");
            }

            String fileName = "reservations_" + today.toString() + ".txt";
            File file = new File(downloadsDirectory, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dataString.getBytes());
            fos.close();
            Log.e("ceva","ceva2");
            Toast.makeText(this, "File saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            openFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
            Log.e("ceva","ceva3");
        }

    }
    private void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.uploading_image));
        progressDialog.show();
        if(imageUri!=null){
            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(getString(R.string.members_images)).
                    child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.image_uploaded_successfully, Toast.LENGTH_SHORT).show();


                                DocumentReference documentReference = firestore.collection(getString(R.string.members)).document(firebaseUser.getUid());
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String oldImageUrl = snapshot.getString(getString(R.string.imageurl));
                                            if (!oldImageUrl.isEmpty()) {
                                                StorageReference storageReference2 = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                                                storageReference2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                                Map<String, Object> updates = new HashMap<>();
                                updates.put(getString(R.string.imageurl), url);
                                documentReference.update(updates);
                            }
                        });
                    }
            });

        }
    }
    private void openImage(){
        Intent photoSelectIntent=new Intent();
        photoSelectIntent.setType(getString(R.string.image));
        photoSelectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoSelectIntent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            uploadImage();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void profileDataInitialize() {
        firestore.collection(getString(R.string.members)).document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString(getString(R.string.name));
                            String email = documentSnapshot.getString(getString(R.string.email_profile));
                            String phoneNumber=documentSnapshot.getString(getString(R.string.phonenumber));
                            tv_name.setText(name);
                            tv_email.setText(email);
                            tv_phoneNumber.setText(phoneNumber);
                            if(documentSnapshot.getString(getString(R.string.imageurl)).equals("")){
                            }
                            else{
                                Glide.with(getApplicationContext()).load(documentSnapshot.getString(getString(R.string.imageurl))).apply(new RequestOptions().centerCrop()).into(imageView_profile_picture);
                            }
                        } else {
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void profileDataChangeListen() {
        firestore.collection(getString(R.string.members)).document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null){
                    Member modifiedMember=value.toObject(Member.class);
                    if (modifiedMember != null) {
                        tv_name.setText(modifiedMember.getName());
                        if (!modifiedMember.getImageUrl().equals("")) {
                            Glide.with(getApplicationContext()).load(modifiedMember.getImageUrl()).apply(new RequestOptions().centerCrop()).into(imageView_profile_picture);
                        }
                    }
                }
            }
        });
    }
    private void initializeComponents() {
        getSupportActionBar().setTitle(R.string.profile);
        anstronCoreHelper=new AnstronCoreHelper(this);
        button_logout=findViewById(R.id.button_logout);
        imageView_profile_picture=findViewById(R.id.profileImage);
        button_change_profile_picture=findViewById(R.id.button_change_profile_image_profileActivity);
        button_history=findViewById(R.id.button_history);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        tv_name=findViewById(R.id.tv_name_profileActivity);
        tv_email=findViewById(R.id.tv_email_profileActivity);
        tv_phoneNumber=findViewById(R.id.tv_phoneNumber_profileActivity);
        tvItemsOwned=findViewById(R.id.tv_items_owned);
        recyclerView=findViewById(R.id.recyclerview_items_ProfileActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapterProfileActivity=new ItemAdapterProfileActivity(ProfileActivity.this,items);
        recyclerView.setAdapter(itemAdapterProfileActivity);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void itemsDataChangdListen() {
        firestore.collection(getString(R.string.items)).whereEqualTo(getString(R.string.memberid),firebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            items.add(dc.getDocument().toObject(Item.class));
                        } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                            String documentId = dc.getDocument().getId();
                            Item modifiedItem = dc.getDocument().toObject(Item.class);
                            for (int i = 0; i < items.size(); i++) {
                                if (items.get(i).getId().equals(documentId)) {
                                    items.set(i, modifiedItem);
                                    break;
                                }
                            }
                        } else if (dc.getType() == DocumentChange.Type.REMOVED) {
                            String documentId = dc.getDocument().getId();
                            for (int i = 0; i < items.size(); i++) {
                                if (items.get(i).getId().equals(documentId)) {
                                    items.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }
                itemAdapterProfileActivity.notifyDataSetChanged();
                tvItemsOwned.setText(getString(R.string.items_in_your_possession)+items.size());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
}