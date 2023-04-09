package com.example.ismmoney;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Calendar;
import java.util.Date;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private Context context;
    private List<person> personList;

    private List<String> DocIdlist;

    String doc;

    public CategoryAdapter(Context context) {
        this.context = context;
        personList = new ArrayList<>();
        DocIdlist = new ArrayList<>();
    }

    public void add(person person, String documentSnapshot)
    {
        personList.add(person);
        DocIdlist.add(documentSnapshot);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_text, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        person person = personList.get(position);
        String doc = DocIdlist.get(position);
        holder.name.setText(person.getTime());
        holder.admission_no.setText(person.getCheckout_time());
        holder.time.setText(person.getIn_time());
        holder.roomno.setText("Room no. "+person.getRoom_no());


        if (Objects.equals(person.getStatus(), "0"))
        {
            holder.checkoutBtn.setVisibility(View.VISIBLE);
            holder.outerBox.setBackgroundColor(R.color.white);
        } else if (Objects.equals(person.getStatus(), "1"))
        {
//            holder.outerBox.setBackgroundColor(R.color.green);
        }
        else {
//            holder.outerBox.setBackgroundColor(R.color.colorPrimary);
        }

        holder.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference washingtonRef = db.collection("SAC").document(doc);

                washingtonRef
                        .update("checkout_time", currentTime.toString(),
                                "status", "1")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                holder.checkoutBtn.setVisibility(View.INVISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error updating document "+e, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView admission_no;
        private TextView time;
        private TextView roomno;
        private Button checkoutBtn;
        private ConstraintLayout outerBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomno = itemView.findViewById(R.id.Room_no_Heading);
            name = itemView.findViewById(R.id.firstname);
            admission_no = itemView.findViewById(R.id.lastname);
            time = itemView.findViewById(R.id.age);
            checkoutBtn = itemView.findViewById(R.id.checkoutBtn);
            outerBox = itemView.findViewById(R.id.OuterBox);


        }
    }

}
