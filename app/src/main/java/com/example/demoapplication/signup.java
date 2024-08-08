package com.example.demoapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class signup extends AppCompatActivity{

    TextView login;
    Button signupButton;
    EditText userName, userEmail, userPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase db;
    FirebaseStorage storage;
    StorageReference uploader;
    FloatingActionButton browseButton;
    ImageView userImage;
    Uri imagePath;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            switchToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        login = findViewById(R.id.loginText);
        signupButton = findViewById(R.id.signupButton);
        userName = findViewById(R.id.username);
        userEmail = findViewById(R.id.useremail);
        userPassword = findViewById(R.id.userpassword);
        userImage = findViewById(R.id.userimage);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseDatabase.getInstance();
        browseButton = findViewById(R.id.browseButton);
        storage = FirebaseStorage.getInstance();

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = userName.getText().toString();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(signup.this, "Username, Email & Password are Required!", Toast.LENGTH_SHORT).show();
                } else if(!email.endsWith("@gmail.com")){
                    Toast.makeText(signup.this, "Email Ends with @gmail.com", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 6){
                    Toast.makeText(signup.this, "Password must be at least 6 Characters", Toast.LENGTH_SHORT).show();
                } else{

                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user != null){
                                            DatabaseReference node = db.getReference("users");
                                            if(imagePath == null){
                                                userData data = new userData(name, email, password, "");
                                                node.child(user.getUid()).setValue(data);
                                                switchToMainActivity();
                                            } else{
                                                uploader = storage.getReference().child(user.getUid());
                                                uploader.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                userData data = new userData(name, email, password, uri.toString());
                                                                node.child(user.getUid()).setValue(data);
                                                                switchToMainActivity();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        } else{
                                            Toast.makeText(signup.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(signup.this, "Email Already Exist!",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK && data != null){
            imagePath = data.getData();
            userImage.setImageURI(imagePath);
        } else{
            Toast.makeText(this, "Failed to Upload Image!", Toast.LENGTH_SHORT).show();
        }
    }

    void switchToMainActivity() {
        Toast.makeText(this, "Account Created Successfull!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(signup.this, login.class);
        startActivity(intent);
        finish();
    }
}


