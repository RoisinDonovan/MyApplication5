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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private Button btnSignup;
    private TextView btnLogin, btnForgotPassword;
    private EditText display_name, input_email, input_password;
    private RelativeLayout activity_sign_up;

    private DatabaseReference mDatabase;

    private FirebaseAuth auth;
    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //view
        btnSignup = (Button) findViewById(R.id.signup_btn_register);
        btnLogin = (TextView) findViewById(R.id.signup_btn_login);
        btnForgotPassword = (TextView) findViewById(R.id.sign_btn_forgot_password);
        display_name = (EditText) findViewById(R.id.signup_name);
        input_email = (EditText) findViewById(R.id.signup_email);
        input_password = (EditText) findViewById(R.id.signup_password);
        activity_sign_up = (RelativeLayout) findViewById(R.id.activity_sign_up);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.signup_btn_login) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.sign_btn_forgot_password) {
            startActivity(new Intent(RegisterActivity.this, ForgotPasswordActivity.class));
            finish();
        } else if (view.getId() == R.id.signup_btn_register) {
            signUpUser(display_name.getText().toString(), input_email.getText().toString(), input_password.getText().toString());
        }
    }

    private void signUpUser(final String display_name, final String email, final String password) {
        Task<AuthResult> authResultTask = auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {


                            FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                            String uid= current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child ("Users").child (uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", display_name);
                            //userMap.put("email", email);
                            //userMap.put("password", password);
                            userMap.put("status","Hi there I'm using P2P");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (!task.isSuccessful()) {
                                        Snackbar snackbar = Snackbar.make(activity_sign_up, "Error: " + task.getException(), Snackbar.LENGTH_INDEFINITE);
                                        snackbar.show();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(activity_sign_up, "Registration Successful : " + task.getException(), Snackbar.LENGTH_INDEFINITE);
                                        snackbar.show();
                                    }
                                }

                                {
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }

                            });
                        }
                    }
                });
    }
}
