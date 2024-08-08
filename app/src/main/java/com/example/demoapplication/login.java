package com.example.demoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends AppCompatActivity{

    TextView signup;
    Button loginButton;
    EditText userEmail, userPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
             Intent intent = new Intent(login.this, MainActivity.class);
             startActivity(intent);
             finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        signup = findViewById(R.id.signupText);
        loginButton = findViewById(R.id.loginButton);
        userEmail = findViewById(R.id.useremail);
        userPassword = findViewById(R.id.userpassword);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
                finish();
            }
        });

       loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String email = userEmail.getText().toString();
               String password = userPassword.getText().toString();

               if(email.isEmpty() || password.isEmpty()){
                   Toast.makeText(login.this, "Email & Password are Required!", Toast.LENGTH_SHORT).show();
               } else if(!email.endsWith("@gmail.com")){
                   Toast.makeText(login.this, "Email Ends with @gmail.com", Toast.LENGTH_SHORT).show();
               } else if(password.length() < 6){
                   Toast.makeText(login.this, "Password must be at least 6 Characters", Toast.LENGTH_SHORT).show();
               } else{
                   progressBar.setVisibility(View.VISIBLE);
                   mAuth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       Toast.makeText(login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(login.this, MainActivity.class);
                                       startActivity(intent);
                                       finish();
                                   } else {
                                       Toast.makeText(login.this, "Invalid Username or Password!",
                                               Toast.LENGTH_SHORT).show();
                                       progressBar.setVisibility(View.GONE);
                                   }
                               }
                           });
               }
           }
       });

    }
}
