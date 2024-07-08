package com.example.licentapbn.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentapbn.R;
import com.example.licentapbn.datatype.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{

    Context context;
    List<Item> items;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    List<Item> itemsCopy;
    List<String> memberIdAndNameForReservation=new ArrayList<>();
    String tvDateInformationForReservation;
    String currentUseriD;


//    public ItemAdapter(Context context, List<Item> items) {
//        this.context = context;
//        this.itemsCopy=items;
////        this.items =this.itemsCopy;
//    }

//    public ItemAdapter(Context context, List<Item> items, String currentUseriD) {
//        this.context = context;
//        this.items = items;
//        this.itemsCopy = items;
//        this.items =this.itemsCopy;
//        this.currentUseriD = currentUseriD;
//    }

    public ItemAdapter(Context context, List<Item> items, String currentUseriD, List<String> memberIdAndNameForReservation, String tvDateInformationForReservation) {
        this.context = context;
        this.items = items;
        this.itemsCopy = items;
        this.memberIdAndNameForReservation = memberIdAndNameForReservation;
        this.tvDateInformationForReservation = tvDateInformationForReservation;
        this.currentUseriD = currentUseriD;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvItemName.setText(items.get(position).getName());
        holder.invisibleLayout.setVisibility(View.GONE);
        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);

        holder.item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).isExpanded()) {
                    holder.invisibleLayout.setVisibility(View.GONE);
                    items.get(position).setExpanded(false);

                } else {
                    holder.invisibleLayout.setVisibility(View.VISIBLE);
                    items.get(position).setExpanded(true);
                }
            }
        });

        if(items.get(position).isFree()) {
            holder.tvItemStatus.setText("Available");
            holder.tvItemStatus.setTextColor(Color.GREEN);}
        else{
            holder.tvItemStatus.setText("Unavailable");
            holder.tvItemStatus.setTextColor(Color.RED);
        }
        if(items.get(position).isReserved()){
            holder.tvReservationStatusItem.setText("Reserved");
            holder.tvReservationStatusItem.setTextColor(Color.parseColor("#ffb703"));
        }else{
            holder.tvReservationStatusItem.setText("Available");
            holder.tvReservationStatusItem.setTextColor(Color.GREEN);
        }
        if(items.get(position).isStatusVisible()){
            holder.tvItemStatus.setVisibility(View.VISIBLE);
        }else{
            holder.tvItemStatus.setVisibility(View.GONE);
        }
        if(items.get(position).isReservationVisible()) {
            holder.tvReservationStatusItem.setVisibility(View.VISIBLE);
        }else{
            holder.tvReservationStatusItem.setVisibility(View.GONE);
        }
        if(items.get(position).isCancelReserveButtonVisible()){
            holder.btnCancelReserve.setVisibility(View.VISIBLE);
        }
        else{
            holder.btnCancelReserve.setVisibility(View.GONE);
        }
        if(items.get(position).isReserveButtonVisible()){
            holder.btnReserve.setVisibility(View.VISIBLE);
        }
        else{
            holder.btnReserve.setVisibility(View.GONE);
        }

        holder.btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obține ID-ul elementului
                String itemId = items.get(position).getId();

                // Numele cheii și valoarea de adăugat
                String key = tvDateInformationForReservation; // Înlocuiește cu cheia relevantă
                List<String> value = memberIdAndNameForReservation; // Înlocuiește cu valoarea relevantă
                // Actualizează documentul
                firestore.collection("items").document(itemId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Obține mapa actuală
                            Map<String, List<String>> reservationsMap = (Map<String, List<String>>) document.get("reservationsMap");
                            if (reservationsMap == null) {
                                reservationsMap = new HashMap<>();
                            }
                            List<String> reservationsHistory = (List<String>) document.get("reservationsHistory");
                            if (reservationsHistory == null) {
                                reservationsHistory = new ArrayList<>();
                            }
                            String reservationEntry =itemId+"-"+memberIdAndNameForReservation.get(0) + "-" +memberIdAndNameForReservation.get(1)+"-"+"reservation"+"-"+new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date())+"-"+tvDateInformationForReservation;

                            // Adaugă noua intrare în lista de istoric
                            reservationsHistory.add(reservationEntry);
                            // Obține lista pentru cheia specifică sau creează una nouă

                            reservationsMap.put(key, value);
                            firestore.collection("items").document(itemId).update("reservationsHistory", reservationsHistory)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(v.getContext(), "Reservation added", Toast.LENGTH_SHORT).show();
                                    });

                            // Trimite actualizarea la Firestore
                            firestore.collection("items").document(itemId).update("reservationsMap", reservationsMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(v.getContext(), "Reservation added", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(v.getContext(), "Error adding reservation", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(v.getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                        items.get(position).setReserved(true);
                        items.get(position).setReservationVisible(true);
                        items.get(position).setReserveButtonVisible(false);
                        items.get(position).setCancelReserveButtonVisible(true);

                    } else {
                        Toast.makeText(v.getContext(), "Failed to get document", Toast.LENGTH_SHORT).show();
                    }
                });
                DocumentReference docRef =firestore.collection("members").document(memberIdAndNameForReservation.get(1)).collection("reservations").document(tvDateInformationForReservation);
                docRef.update("reservationsMap", FieldValue.arrayUnion(itemId))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Elementul "itemId" a fost adăugat cu succes în array
                                    Log.d("revev", "Elementul a fost adăugat cu succes în array");
                                } else {
                                    // Eroare la adăugarea elementului în array
                                    Log.e("revev", "Eroare la adăugarea elementului în array", task.getException());
                                    // Tratează eroarea aici, de exemplu:
                                    if (task.getException() instanceof FirebaseFirestoreException) {
                                        FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) task.getException();
                                        if (firestoreException.getCode() == FirebaseFirestoreException.Code.NOT_FOUND) {
                                            // Documentul nu există, trebuie să-l creăm
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("reservationsMap", Arrays.asList(itemId));
                                            docRef.set(data)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("revev", "Documentul a fost creat și elementul a fost adăugat cu succes în array");
                                                                setItems(items);
                                                            } else {
                                                                Log.e("revev", "Eroare la crearea documentului și adăugarea elementului în array", task.getException());
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        });

                firestore.collection("members").document(memberIdAndNameForReservation.get(1)).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Obține lista actuală de istoric de rezervări
                            List<String> reservationsHistory = (List<String>) document.get("reservationsHistory");
                            if (reservationsHistory == null) {
                                reservationsHistory = new ArrayList<>();
                            }

                            // Construiește o nouă intrare pentru istoricul de rezervări
                            String reservationEntry = itemId +"-" + "reservation" + "-" + new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()) + "-" + tvDateInformationForReservation;

                            // Adaugă noua intrare în lista de istoric
                            reservationsHistory.add(reservationEntry);

                            // Actualizează documentul cu noul istoric de rezervări
                            firestore.collection("members").document(memberIdAndNameForReservation.get(1)).update("reservationsHistory", reservationsHistory)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(v.getContext(), "Reservation added", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(v.getContext(), "Error adding reservation", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(v.getContext(), "Member document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Failed to get member document", Toast.LENGTH_SHORT).show();
                    }
                });
                }
        });

                holder.btnCancelReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = tvDateInformationForReservation; // Înlocuiește cu cheia relevantă
                        List<String> value = memberIdAndNameForReservation; // Înlocuiește cu valoarea relevantă
                        // Obține ID-ul elementului
                        String itemId = items.get(position).getId();

                        // Numele cheii și valoarea de șters

                        // Actualizează documentul
                        firestore.collection("items").document(itemId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Obține mapa actuală
                                    Map<String, List<String>> reservationsMap = (Map<String, List<String>>) document.get("reservationsMap");
                                    if (reservationsMap != null) {
                                        // Șterge lista pentru cheia specifică
                                        reservationsMap.remove(key);

                                        // Trimite actualizarea la Firestore
                                        firestore.collection("items").document(itemId).update("reservationsMap", reservationsMap)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(v.getContext(), "Reservation deleted", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(v.getContext(), "Error deleting reservation", Toast.LENGTH_SHORT).show();
                                                });
                                        List<String> reservationsHistory = (List<String>) document.get("reservationsHistory");
                                        if (reservationsHistory == null) {
                                            reservationsHistory = new ArrayList<>();
                                        }
                                        String reservationEntry =itemId+"-"+memberIdAndNameForReservation.get(0) + "-" +memberIdAndNameForReservation.get(1)+"-"+"cancel_reservation"+"-"+new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date())+"-"+tvDateInformationForReservation;

                                        // Adaugă noua intrare în lista de istoric
                                        reservationsHistory.add(reservationEntry);
                                        // Obține lista pentru cheia specifică sau creează una nouă

                                        reservationsMap.put(key, value);
                                        firestore.collection("items").document(itemId).update("reservationsHistory", reservationsHistory)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(v.getContext(), "Reservation added", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(v.getContext(), "No reservations to delete", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(v.getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                                }
                                items.get(position).setReserved(false);
                                String azi = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                                if(tvDateInformationForReservation.equals(azi)){
                                    items.get(position).setReservationVisible(false);
                                }
                                else{
                                    items.get(position).setReservationVisible(true);
                                }
                                items.get(position).setReserveButtonVisible(true);
                                items.get(position).setCancelReserveButtonVisible(false);
                                setItems(items);

                            } else {
                                Toast.makeText(v.getContext(), "Failed to get document", Toast.LENGTH_SHORT).show();
                            }
                        });
                        DocumentReference docRef =firestore.collection("members").document(memberIdAndNameForReservation.get(1)).collection("reservations").document(tvDateInformationForReservation);
                        docRef.update("reservationsMap", FieldValue.arrayRemove(itemId))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Elementul "itemId" a fost eliminat cu succes din array
                                            Log.d("revev", "Elementul a fost eliminat cu succes din array");
                                            Toast.makeText(v.getContext(), "Reservation deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Eroare la eliminarea elementului din array
                                            Log.e("revev", "Eroare la eliminarea elementului din array", task.getException());
                                            Toast.makeText(v.getContext(), "Error deleting reservation", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        firestore.collection("members").document(memberIdAndNameForReservation.get(1)).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Obține lista actuală de istoric de rezervări
                                    List<String> reservationsHistory = (List<String>) document.get("reservationsHistory");
                                    if (reservationsHistory == null) {
                                        reservationsHistory = new ArrayList<>();
                                    }

                                    // Construiește o nouă intrare pentru istoricul de rezervări
                                    String reservationEntry = itemId +"-" + "cancel_reservation" + "-" + new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()) + "-" + tvDateInformationForReservation;

                                    // Adaugă noua intrare în lista de istoric
                                    reservationsHistory.add(reservationEntry);

                                    // Actualizează documentul cu noul istoric de rezervări
                                    firestore.collection("members").document(memberIdAndNameForReservation.get(1)).update("reservationsHistory", reservationsHistory)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(v.getContext(), "Reservation added", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(v.getContext(), "Error adding reservation", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(v.getContext(), "Member document does not exist", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(v.getContext(), "Failed to get member document", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    public void setItems(List<Item> itemss) {
        itemsCopy=items;
        this.items = itemss;
        notifyDataSetChanged(); // Notifică RecyclerView despre schimbările făcute în listă
    }
    public void filterItemsByFree(boolean isFree) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : items) {
            if (item.isFree()==isFree) {
                filteredList.add(item);
            }
        }
        // Actualizează lista de elemente din adapter cu lista filtrată
        items = filteredList;
        notifyDataSetChanged();
    }
    public void updateDate(String newDate) {
        this.tvDateInformationForReservation = newDate;
        setItems(items);
        notifyDataSetChanged(); // Notifică RecyclerView despre modificare
    }
    public void filterItemsByReserved(boolean isReserved) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : items) {
            if (item.isReserved()==!isReserved) {
                filteredList.add(item);
            }
        }
        // Actualizează lista de elemente din adapter cu lista filtrată
        setItems(filteredList);
    }
    public void clearFilter() {
        items = new ArrayList<>(itemsCopy); // Creează o nouă listă din copia originală
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView tvItemName,tvItemStatus,tvReservationStatusItem;
        ImageView imageView;
        CardView item_cardview;
        ConstraintLayout invisibleLayout;
        Button btnReserve;
        Button btnCancelReserve;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationStatusItem=itemView.findViewById(R.id.tv_reservation_status_item);
            tvItemName= itemView.findViewById(R.id.tv_name_item);
            tvItemStatus = itemView.findViewById(R.id.tv_status_item);
            item_cardview=itemView.findViewById(R.id.item_cardview);
            invisibleLayout=itemView.findViewById(R.id.invisible_constraint_layout);
            imageView=itemView.findViewById(R.id.recyclerImage);
            btnReserve=itemView.findViewById(R.id.tv_reserve);
            btnCancelReserve=itemView.findViewById(R.id.tv_cancel_reserve);

        }

    }
}
