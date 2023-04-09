package com.example.ismmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registrations extends AppCompatActivity {

    CategoryAdapter mypersonAdapter;
    private TextView name;

    private String admission_no;
    private TextView Admission_no;
    private EditText room_no_text;
    private Button bookRoomBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrations);

        mypersonAdapter = new CategoryAdapter(this);
        RecyclerView personsRecyclerView = findViewById(R.id.recycler1);
        personsRecyclerView.setAdapter(mypersonAdapter);
        personsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        name = findViewById(R.id.admno);
        Admission_no = findViewById(R.id.phno);
        bookRoomBut = findViewById(R.id.bookRoombut);
        room_no_text = findViewById(R.id.room_no_request);

        admission_no = "";
        Intent intent = getIntent();
        admission_no = intent.getStringExtra("admission_no");
        name.setText(intent.getStringExtra("name"));
        Admission_no.setText(admission_no);


//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        Toast.makeText(this, "got Shared Preference = "+sharedPref.toString(), Toast.LENGTH_SHORT).show();
//        admission_no = sharedPref.getString("admission_no", "25HE256");
//        Toast.makeText(this, "admission no = "+admission_no, Toast.LENGTH_SHORT).show();
        if (admission_no==""){
//            Toast.makeText(this, "Null Admission no", Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(this, "not null"+admission_no, Toast.LENGTH_SHORT).show();
            loadpersons(admission_no);
        }

        bookRoomBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room_no_text.getText().toString()==""){
                    Toast.makeText(Registrations.this, "Enter Room no", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> key = new HashMap<>();
                    int min = 10;
                    int max = 100;
                    int num = (int) (Math.random() * (max - min + 1) + min);
                    key.put("room_no",room_no_text.getText().toString());
                    key.put("random_code", String.valueOf(num));

                    db.collection("SAC_users").document(admission_no)
                            .set(key)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                    Toast.makeText(Registrations.this, "Room booked", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Registrations.this, String.format("Your random number is = %s", num), Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error writing document", e);
                                    Toast.makeText(Registrations.this, "Error occured = "+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }

    private void loadpersons(String admission_no) {
        FirebaseFirestore.getInstance().collection("SAC").whereEqualTo("admission_no", admission_no).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Toast.makeText(Registrations.this, "Received", Toast.LENGTH_SHORT).show();
                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
//                        Toast.makeText(Registrations.this, "documentSnapshotList= "+documentSnapshotList.toString(), Toast.LENGTH_SHORT).show();

                        for (DocumentSnapshot doc:documentSnapshotList){
                            person person = doc.toObject(com.example.ismmoney.person.class);
                            mypersonAdapter.add(person, doc.getId().toString());
                        }
                    }
                });
    }
}