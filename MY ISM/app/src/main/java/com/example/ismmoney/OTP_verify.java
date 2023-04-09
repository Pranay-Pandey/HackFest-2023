package com.example.ismmoney;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ismmoney.databinding.ActivityOtpVerifyBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
public class OTP_verify extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityOtpVerifyBinding binding;
    private Button VerifyButton;
    private TextInputEditText otpBox;
    private String id="";
    private boolean otpSent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        FirebaseAuth firebaseAppAuth = FirebaseAuth.getInstance();
        binding = ActivityOtpVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        otpBox = findViewById(R.id.verifyOTP);
        VerifyButton = findViewById(R.id.VerifyOTP_Button);
        Intent intent = getIntent();
        String admission_number = intent.getStringExtra("admission_number");
        String phone_number = intent.getStringExtra("phone_number");

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAppAuth)
                .setPhoneNumber("+91 "+phone_number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTP_verify.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(OTP_verify.this, "OTP Sent successfulluy", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTP_verify.this, "SOmething Went Wrong "+ e.toString(), Toast.LENGTH_SHORT).show();
                        otpBox.setText(e.toString());
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        id = s;
                        otpSent = true;
                        Toast.makeText(OTP_verify.this, "OTP Sent.", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpSent==false)
                {
                    Toast.makeText(OTP_verify.this, "OTP NOT SENT YET", Toast.LENGTH_SHORT).show();
                    return;
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otpBox.getText().toString());
                firebaseAppAuth .signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser userdetails = task.getResult().getUser();
                            Toast.makeText(OTP_verify.this, "User Verified", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(OTP_verify.this, DisplayPage.class);
                            startActivity(intent1);
                        }
                        else {
                            Toast.makeText(OTP_verify.this, "Something went wrong in verification.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_otp_verify);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}