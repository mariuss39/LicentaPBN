package com.example.licentapbn.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.licentapbn.datatype.Item;
import com.example.licentapbn.datatype.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseSingleton {

    public static final String MEMBERS_REFERENCE = "members";
    private final DatabaseReference membersReference;
    public static final String ITEMS_REFERENCE = "items";
    private final DatabaseReference itemsReference;
    public static FirebaseSingleton firebaseSingleton;

    public FirebaseSingleton() {
        membersReference = FirebaseDatabase.getInstance().getReference(MEMBERS_REFERENCE);
        itemsReference=FirebaseDatabase.getInstance().getReference(ITEMS_REFERENCE);
    }
    public static FirebaseSingleton getInstance(){
        if(firebaseSingleton==null){
            synchronized (FirebaseSingleton.class){
                if(firebaseSingleton==null){
                    firebaseSingleton=new FirebaseSingleton();
                }
            }
        }
        return firebaseSingleton;
    }

    //members actions
    public void insert(Member member) {
//        if (member == null || (member.getId() != null && !member.getId().trim().isEmpty())) {
//            return;
//        }
        String id = membersReference.push().getKey();
        member.setId(id);
        membersReference.child(member.getId()).setValue(member);
    }

    public void update(Member member){
        if(member == null || member.getId() == null){
            return;
        }
        membersReference.child(member.getId()).setValue(member);
    }

    public void delete(Member member){
        if(member == null || member.getId() == null || member.getId().trim().isEmpty()){
            return;
        }
        membersReference.child(member.getId()).removeValue();
    }

    //items action
    public void insertItem(Item item) {
//        if (member == null || (member.getId() != null && !member.getId().trim().isEmpty())) {
//            return;
//        }
        String id = itemsReference.push().getKey();
        item.setId(id);
        itemsReference.child(item.getId()).setValue(item);

    }

    public void attachMemberDataChangeEventListener(Callback<List<Member>> callback){
        membersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Member> members = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    Member student = data.getValue(Member.class);
                    if(student!=null){
                        members.add(student);
                    }
                }

                callback.runResultOnUiThread(members);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }
        });
    }
    public void attachItemDataChangeEventListener(Callback<List<Item>> callback){
        itemsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    Item item = data.getValue(Item.class);
                    if(item!=null){
                        items.add(item);
                    }
                }

                callback.runResultOnUiThread(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }
        });
    }

}
