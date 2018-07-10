package com.dw.courgette;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.courgette.MainActivity;
import com.dw.courgette.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmail, mPassword;
    private Button mloginBtn;

    private ProgressBar progressBar;
    private TextView progresText;

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = (TextInputLayout) findViewById(R.id.login_email);
        mPassword = (TextInputLayout) findViewById(R.id.login_password);
        mloginBtn = (Button) findViewById(R.id.login_sign_in_user);

        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        progresText = (TextView) findViewById(R.id.progress_text);

        mToolbar = (Toolbar) findViewById(R.id.login_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(email.isEmpty()){
                    mEmail.setError("Email is required ");
                    mEmail.requestFocus();
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

                switchProgressState(1);
                loginUser(email, password);

            }
        });
    }

    public void switchProgressState(int state){
        if(state == 0){
            progressBar.setVisibility(View.INVISIBLE);
            progresText.setVisibility(View.INVISIBLE);
        }
        if(state != 0){
            progressBar.setVisibility(View.VISIBLE);
            progresText.setVisibility(View.VISIBLE);
        }

    }
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent MainIntent =  new Intent(LoginActivity.this, MainActivity.class);
                            MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(MainIntent);
                            finish();
                        } else {
                            switchProgressState(0);
                            Toast.makeText(LoginActivity.this, "Authentication failed: "+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }




}
