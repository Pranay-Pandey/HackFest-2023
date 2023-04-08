package com.example.ismmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DisplayPage extends AppCompatActivity {

    private TextView name;
    private TextView phone;
    private TextView balance;
    private Button signOutButton;
    private Button registrationsPageBut;
    public String admission_no;
    public String user_name;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        balance = findViewById(R.id.balance);
        floatingActionButton = findViewById(R.id.fab);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        signOutButton = findViewById(R.id.signoutButton);
        registrationsPageBut = findViewById(R.id.registrations);
        admission_no = "";
        user_name = "";

        if (user != null) {
            // User is signed in
            Toast.makeText(this, "This is user= "+user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            phone.setText(user.getPhoneNumber());
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("phone_no", user.getPhoneNumber())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Toast.makeText(DisplayPage.this, document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                                    name.setText(document.getData().get("Name").toString());
                                    balance.setText(document.getData().get("balance").toString());
                                    admission_no = document.getData().get("admission_no").toString();
                                    user_name = document.getData().get("Name").toString();
                                    SharedPreferences sharedPref = DisplayPage.this.getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("Name", document.getData().get("Name").toString());
                                    editor.putString("admission_no", document.getData().get("admission_no").toString());
                                    editor.putString("phone_no", document.getData().get("phone_no").toString());
                                    editor.apply();
                                    Toast.makeText(DisplayPage.this, "Putting admisison no = "+document.getData().get("admission_no").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(DisplayPage.this, "Error getting documents: "+ task.getException(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        } else {
            // No user is signed in
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DisplayPage.this, "Add Money to Your Wallet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DisplayPage.this, Pay.class);
                startActivity(intent);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DisplayPage.this, Login.class);
                startActivity(intent);
            }
        });
        registrationsPageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (admission_no!="") {
                    Intent registrations_intent = new Intent(DisplayPage.this, Registrations.class);
                    registrations_intent.putExtra("admission_no", admission_no);
                    registrations_intent.putExtra("name", user_name);
                    startActivity(registrations_intent);
                }
            }
        });
    }
}