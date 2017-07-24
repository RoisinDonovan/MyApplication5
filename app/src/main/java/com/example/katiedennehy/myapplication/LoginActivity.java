package com.example.katiedennehy.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email, input_password;
    TextView btnSignUp, btnForgotPassword;

    RelativeLayout activity_main;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        input_email = (EditText) findViewById(R.id.login_email);
        input_password = (EditText) findViewById(R.id.login_password);
        btnSignUp = (TextView) findViewById(R.id.login_btn_signup);
        btnForgotPassword = (TextView) findViewById(R.id.login_btn_forgot_password);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        btnSignUp.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        if (mAuth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_btn_forgot_password) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            finish();
        } else if (view.getId() == R.id.login_btn_signup) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        } else if (view.getId() == R.id.signup_btn_register) {

        } else if (view.getId() == R.id.login_btn_login) {
            loginUser(input_email.getText().toString(), input_password.getText().toString());

        }
    }

    private void loginUser(final String email, final String password) {
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            if (password.length() < 6) {
                                Snackbar snackbar = Snackbar.make(activity_main, "Password length has be a minimum of 6 characters",
                                        Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(activity_main, task.getException().toString(),
                                        Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                            String current_user_id = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }

                });

    }
}






