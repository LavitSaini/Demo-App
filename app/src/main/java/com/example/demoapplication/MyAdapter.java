package com.example.demoapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class MyAdapter extends FirebaseRecyclerAdapter<userData, MyAdapter.MyHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */

    public MyAdapter(@NonNull FirebaseRecyclerOptions<userData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull userData model) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.userName.setText(model.getUsername());
        holder.userEmail.setText(model.getEmail());
        if(!model.getImage().equals("")){
            Picasso.get().load(model.getImage()).into(holder.userImage);
        }
        holder.itemView.startAnimation(animation);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyHolder(v);
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        TextView userName, userEmail;
        ImageView userImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            userEmail = itemView.findViewById(R.id.useremail);
            userImage = itemView.findViewById(R.id.userimage);
        }
    }

}
