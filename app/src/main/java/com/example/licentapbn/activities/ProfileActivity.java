package com.example.licentapbn.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.datatype.ItemAdapter;
import com.example.licentapbn.datatype.ItemAdapterProfileActivity;
import com.example.licentapbn.datatype.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.grpc.Context;

public class ProfileActivity extends AppCompatActivity {

    Button button_logout;
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
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();


            }
        });
        button_change_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }
    private void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading image..");
        progressDialog.show();
        if(imageUri!=null){
            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("members_images").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                Log.e("TAGuri", "HELP");
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image uploaded succesfully", Toast.LENGTH_SHORT).show();


                                DocumentReference documentReference = firestore.collection("members").document(firebaseUser.getUid());
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String oldImageUrl = snapshot.getString("imageUrl");
                                            if (!oldImageUrl.equals("")) {
                                                StorageReference storageReference2 = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                                                storageReference2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                    }
                                                });
                                            } else {
                                                Log.e("TAGPHOTO", "E PRIMA POZA");
                                            }
                                        }
                                    }
                                });
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("imageUrl", url);
                                documentReference.update(updates);
                            }
                        });
                    }
            });

        }
    }
    private void openImage(){
        Intent photoSelectIntent=new Intent();
        photoSelectIntent.setType("image/*");
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
        firestore.collection("members").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phoneNumber=documentSnapshot.getString("phoneNumber");
                            tv_name.setText(name);
                            tv_email.setText(email);
                            tv_phoneNumber.setText(phoneNumber);
                            if(documentSnapshot.getString("imageUrl").equals("")){
                                Log.e("TAGPHOTO","fail la initializare");
                            }
                            else{
                                Glide.with(getApplicationContext()).load(documentSnapshot.getString("imageUrl")).into(imageView_profile_picture);
                            }
                        } else {
                            Log.d("TAG", "Document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error getting document", e);
                    }
                });
    }

    private void profileDataChangeListen() {
        firestore.collection("members").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.err.println("A apărut o eroare: " + error);
                    return;
                }
                if (value != null){
                    Log.e("TAG A3","HELEELELELEP");
                    Member modifiedMember=value.toObject(Member.class);
                    if (modifiedMember != null) {
                        tv_name.setText(modifiedMember.getName());
                        if (!modifiedMember.getImageUrl().equals("")) {
                            Glide.with(getApplicationContext()).load(modifiedMember.getImageUrl()).into(imageView_profile_picture);
                        }
                    }
                }
            }
        });
    }

    private void initializeComponents() {
        button_logout=findViewById(R.id.button_logout);
        imageView_profile_picture=findViewById(R.id.profileImage);
        button_change_profile_picture=findViewById(R.id.button_change_profile_image_profileActivity);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        tv_name=findViewById(R.id.tv_name_profileActivity);
        tv_email=findViewById(R.id.tv_email_profileActivity);
        tv_phoneNumber=findViewById(R.id.tv_phoneNumber_profileActivity);
        tvItemsOwned=findViewById(R.id.tv_items_owned);
        //recycler items
        recyclerView=findViewById(R.id.recyclerview_items_ProfileActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapterProfileActivity=new ItemAdapterProfileActivity(ProfileActivity.this,items);
        recyclerView.setAdapter(itemAdapterProfileActivity);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("fetching data..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void itemsDataChangdListen() {
        firestore.collection("items").whereEqualTo("memberId",firebaseUser.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        itemAdapterProfileActivity.notifyDataSetChanged();
                        tvItemsOwned.setText("Items in your possesion: "+items.size());
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
            }
        });
    }
}