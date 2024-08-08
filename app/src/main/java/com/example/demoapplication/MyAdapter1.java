package com.example.demoapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyHolder> {

    ArrayList<MyModel.Article> list;
    String imageUrl = "";
    Context context;


    public MyAdapter1(ArrayList<MyModel.Article> list, Context context ){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText("Description: " + list.get(position).getDescription());
        holder.author.setText("Author: " + list.get(position).getAuthor());
        holder.published.setText("Published At: " + list.get(position).getPublishedAt());
        imageUrl = list.get(position).getUrlToImage() != null ? list.get(position).getUrlToImage() : "";
        if(!imageUrl.equals("")){
            Picasso.get().load(imageUrl).into(holder.image);
        }

        holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getUrl().contains("https://removed.com")){
                    Toast.makeText(context, "Source Not found!", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent = new Intent(context, WebsiteActivity.class);
                    intent.putExtra("url", list.get(position).getUrl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        TextView title, description, author, published;
        ImageView image;
        Button readMoreButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            author = itemView.findViewById(R.id.author);
            published = itemView.findViewById(R.id.published);
            image = itemView.findViewById(R.id.image);
            readMoreButton = itemView.findViewById(R.id.readMore);
        }
    }
}
