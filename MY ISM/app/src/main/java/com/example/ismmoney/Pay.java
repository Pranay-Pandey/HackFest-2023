package com.example.ismmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Pay extends AppCompatActivity implements PaymentResultListener {

    private TextInputEditText amountField;
    private Button payBtn;
    private Button cancelBtn;

    public FirebaseUser user;
    public JSONObject userdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Checkout.preload(getApplicationContext());

        amountField = findViewById(R.id.amt);
        payBtn = findViewById(R.id.payButton);
        cancelBtn = findViewById(R.id.CancelButton);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentNow(amountField.getText().toString());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pay.this, DisplayPage.class);
                startActivity(intent);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        userdetails = new JSONObject();

        if (user != null) {
            // User is signed in
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("phone_no", user.getPhoneNumber())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    try {
                                        userdetails.put("Name", document.getData().get("Name").toString());
                                        userdetails.put("balance", document.getData().get("balance").toString());
                                        userdetails.put("admission_no", document.getData().get("admission_no").toString());
                                        userdetails.put("phone_no", document.getData().get("phone_no").toString());

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

//                                    Toast.makeText(DisplayPage.this, document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
//                                    name.setText(document.getData().get("Name").toString());
//                                    balance.setText(document.getData().get("balance").toString());
                                }
                            } else {
                                Toast.makeText(Pay.this, "Error getting documents: "+ task.getException(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        } else {
            // No user is signed in
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

    }

    private void PaymentNow(String amount) {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("<YOUR_KEY_ID>");
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.background);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Paying to ISM");
            options.put("description", "Adding "+amount+ " to "+ userdetails.get("Name")+"'s Wallet");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", (Float.parseFloat(amount)*100)+"");//pass amount in currency subunits
//            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", userdetails.get("phone_no"));
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

//            checkout.open(activity, options);

        } catch(Exception e) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e);
            Toast.makeText(activity, "Error in starting Razorpay Checkout"+ e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Sucess !!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed = "+s.toString(), Toast.LENGTH_SHORT).show();
    }
}