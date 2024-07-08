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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                            // Obține lista pentru cheia specifică sau creează una nouă

                            reservationsMap.put(key, value);

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
                    } else {
                        Toast.makeText(v.getContext(), "Failed to get document", Toast.LENGTH_SHORT).show();
                    }
                });
                DocumentReference docRef =firestore.collection("members").document(memberIdAndNameForReservation.get(1)).collection("reservations").document(tvDateInformationForReservation);

                // Adăugați un nou element în array-ul "reservationsMap"
                docRef.set(new HashMap<String, Object>())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Documentul a fost creat sau există deja
                                    Log.d("revev", "Documentul a fost creat sau există deja");

                                    // Adăugați itemId în array-ul "reservationsMap"
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
                                                    }
                                                }
                                            });
                                } else {
                                    // Eroare la crearea documentului
                                    Log.e("revev", "Eroare la crearea documentului", task.getException());
                                }
                            }
                        });

                }

        });
    }
    public void setItems(List<Item> itemss) {
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
        items = filteredList;
        notifyDataSetChanged();
    }
    public void clearFilter() {
        items = itemsCopy; // Lista filtrată devine lista completă de elemente
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
