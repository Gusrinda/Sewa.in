package com.example.sewain.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sewain.Model.User;
import com.example.sewain.PesanActivity;
import com.example.sewain.R;

import java.util.List;


public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private  boolean ischat;

    String theLastMessage;

    public AdapterUser(Context mContext, List<User> mUsers, boolean ischat){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        return new AdapterUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getUrlPhoto().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getUrlPhoto()).into(holder.profile_image);
        }

//        if (ischat){
//            lastMessage(user.getId(), holder.last_msg);
//        } else {
//            holder.last_msg.setVisibility(View.GONE);
//        }

//        //Cek online Offline
//        if (ischat){
//            if (user.getStatus().equals("online")){
//                holder.img_on.setVisibility(View.VISIBLE);
//                holder.img_off.setVisibility(View.GONE);
//            } else {
//                holder.img_on.setVisibility(View.GONE);
//                holder.img_off.setVisibility(View.VISIBLE);
//            }
//        } else {
//            holder.img_on.setVisibility(View.GONE);
//            holder.img_off.setVisibility(View.GONE);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PesanActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private  TextView last_msg;


        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.namaUser);
            profile_image = itemView.findViewById(R.id.imgUser);
        }
    }
}
