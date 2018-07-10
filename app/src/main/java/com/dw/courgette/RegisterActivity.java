package com.dw.courgette;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName, mEmail, mPassword;
    private Button mCreateAccBtn;

    private  Toolbar mToolbar;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // Android
        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mCreateAccBtn = (Button) findViewById(R.id.reg_create_acc_btn);

        // Setting Toolbar
        mToolbar = (Toolbar)findViewById(R.id.register_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Register Account By Btn Click
        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = mDisplayName.getEditText().getText().toString();
                String email       = mEmail.getEditText().getText().toString();
                String password    = mPassword.getEditText().getText().toString();


                if(email.isEmpty()){
                    mEmail.setError("Email is required ");
                    mEmail.requestFocus();
                    return;
                }
                if(displayName.isEmpty()){
                    mDisplayName.setError("user is required ");
                    mDisplayName.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Please enter a valid email");
                    mEmail.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    mPassword.setError("Password is required ");
                    mPassword.requestFocus();
                    return;
                }
                if( password.length() < 6 ){
                    mPassword.setError("Minimum lenth of password is 6");
                    mPassword.requestFocus();
                    return;
                }
                registerUser(displayName,email,password);
            }
        });

    }


    private void registerUser(String displayName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent MainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(MainIntent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Some kurwa error occured"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
