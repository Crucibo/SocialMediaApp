package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.socialmediaapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {
    EditText enternumber;
    Button getOtpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        getSupportActionBar().hide();
        enternumber = findViewById(R.id.mobileNumber);
        getOtpButton = findViewById(R.id.ContinueBtn);

        ProgressBar progressBar=findViewById(R.id.progressbarContinue);

        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!enternumber.getText().toString().trim().isEmpty()){
                    if(enternumber.getText().toString().trim().length() == 10){

                        progressBar.setVisibility(View.VISIBLE);
                        getOtpButton.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + enternumber.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                PhoneNumberActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        getOtpButton.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        getOtpButton.setVisibility(View.VISIBLE);
                                        Toast.makeText(PhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(backendOTP, forceResendingToken);
                                        progressBar.setVisibility(View.GONE);
                                        getOtpButton.setVisibility(View.VISIBLE);
                                        Intent intent= new Intent(getApplicationContext(),OTPActivity.class);
                                        intent.putExtra("mobile",enternumber.getText().toString());
                                        intent.putExtra("backendOTP",backendOTP);
                                        startActivity(intent);
                                    }
                                }
                        );

                        Intent intent= new Intent(getApplicationContext(),OTPActivity.class);
                        intent.putExtra("mobile",enternumber.getText().toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(PhoneNumberActivity.this,"Please Enter Correct number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PhoneNumberActivity.this,"Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}