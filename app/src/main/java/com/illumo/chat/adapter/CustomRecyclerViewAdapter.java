package com.illumo.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.illumo.chat.ChatMain;
import com.illumo.chat.R;
import com.illumo.chat.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.UserViewHolder>{

    private List<User> userList = new ArrayList<>();

//    public CustomRecyclerViewAdapter(List<User> userList)
//    {
//        this.userList = userList;
//    }


    public void setItems(List<User> users)
    {
        this.userList = new ArrayList<>();
        this.userList = users;
        //userList.addAll(users);
        notifyDataSetChanged();
    }

    public void clearItems() {
        userList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        ImageView imageView;

        public UserViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.nickname);
            imageView = itemView.findViewById(R.id.checkimage);

        }

        public void bind(final User user)
        {
            username.setText(user.getNickname());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.setChecked(!user.getChecked());
                    imageView.setVisibility(user.getChecked() ? view.VISIBLE : view.GONE);
                }
            });
        }
    }

    public List<User> getAll() {return userList;}

    public List<User> getSelected()
    {
        List<User> selected = new ArrayList<>();
        for(int i = 0; i < userList.size(); i++)
        {
            if(userList.get(i).getChecked())
            {
                selected.add(userList.get(i));
            }
        }
        return selected;
    }



}
