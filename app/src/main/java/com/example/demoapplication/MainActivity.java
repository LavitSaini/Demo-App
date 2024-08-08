package com.example.demoapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    TextView userNameText, userEmailText;
    EditText queryInput;
    Button logoutButton, showAllUsersButton, searchButton;
    ImageView userImage;
    ProgressBar progressBar;
    CardView imageCard;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        userNameText = findViewById(R.id.userNameText);
        userEmailText = findViewById(R.id.userEmailText);
        userImage = findViewById(R.id.userImage);
        logoutButton = findViewById(R.id.logoutButton);
        showAllUsersButton = findViewById(R.id.allusersbtn);
        queryInput = findViewById(R.id.queryinput);
        searchButton = findViewById(R.id.search);
        progressBar = findViewById(R.id.progressBar);
        imageCard = findViewById(R.id.imagecard);
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        user = mAuth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        } else{
            DatabaseReference node = db.getReference("users");
            node.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        DataSnapshot data = task.getResult();
                        String username = String.valueOf(data.child("username").getValue());
                        String email = "Email: " + data.child("email").getValue();
                        String imageUrl = String.valueOf(data.child("image").getValue());
                        progressBar.setVisibility(View.GONE);
                        userNameText.setVisibility(View.VISIBLE);
                        userEmailText.setVisibility(View.VISIBLE);
                        showAllUsersButton.setVisibility(View.VISIBLE);
                        userNameText.setText(username);
                        userEmailText.setText(email);
                        if(!imageUrl.equals("")){
                           Picasso.get().load(imageUrl).into(userImage);
                           imageCard.setVisibility(View.VISIBLE);
                        } else{
                            Toast.makeText(MainActivity.this, "Profile Image not found!", Toast.LENGTH_SHORT).show();
                            imageCard.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Intent intent = new Intent(MainActivity.this, login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logout Successfull!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        showAllUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = queryInput.getText().toString();

                if(query.isEmpty()){
                    Toast.makeText(MainActivity.this, "Query field cannot be Empty!", Toast.LENGTH_SHORT).show();
                } else{
                    SharedPreferences sharedPreferences = getSharedPreferences("savedQuery", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("query", query);
                    myEdit.apply();
                    Intent intent = new Intent(MainActivity.this, RecyclerActivity1.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}