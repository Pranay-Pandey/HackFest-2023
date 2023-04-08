package com.example.ismmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Registrations extends AppCompatActivity {

    CategoryAdapter mypersonAdapter;
    private TextView name;

    private String admission_no;
    private TextView Admission_no;
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
            Toast.makeText(this, "Null Admission no", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "not null"+admission_no, Toast.LENGTH_SHORT).show();
            loadpersons(admission_no);
        }
    }

    private void loadpersons(String admission_no) {
        FirebaseFirestore.getInstance().collection("SAC").whereEqualTo("admission_no", admission_no).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Toast.makeText(Registrations.this, "Received", Toast.LENGTH_SHORT).show();
                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                        Toast.makeText(Registrations.this, "documentSnapshotList= "+documentSnapshotList.toString(), Toast.LENGTH_SHORT).show();

                        for (DocumentSnapshot doc:documentSnapshotList){
                            person person = doc.toObject(com.example.ismmoney.person.class);
                            mypersonAdapter.add(person, doc.getId().toString());
                        }
                    }
                });
    }
}