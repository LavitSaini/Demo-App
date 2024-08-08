package com.example.demoapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerActivity1 extends AppCompatActivity {
    ArrayList<MyModel.Article> list;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView failedText, queryText;
    MyAdapter1 adapter;
    Button backButton;
    ApiInterface apiInterface;
    Retrofit retrofit;
    String baseUrl = "https://newsapi.org/v2/";
    String apiKey = "df515f962045450684254869dffb6444";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler1);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.newslist);
        backButton = findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.progressbar);
        failedText = findViewById(R.id.failedtext);
        queryText = findViewById(R.id.querytext);
        retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        SharedPreferences sharedPreferences = getSharedPreferences("savedQuery", MODE_PRIVATE);
        String query = sharedPreferences.getString("query", "");
        queryText.setText(query.toUpperCase());

        apiInterface = retrofit.create(ApiInterface.class);
        Call<MyModel.Root> call = apiInterface.getEverything(query, apiKey);
        call.enqueue(new Callback<MyModel.Root>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<MyModel.Root> call, @NonNull Response<MyModel.Root> response) {
                if(response.body() != null){
                    list.addAll(response.body().getArticles());
                    adapter = new MyAdapter1(list, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerActivity1.this));
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyModel.Root> call, @NonNull Throwable throwable) {
                Toast.makeText(RecyclerActivity1.this, "failed to fetch Data!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                failedText.setVisibility(View.VISIBLE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecyclerActivity1.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
