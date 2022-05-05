package com.illumo.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.illumo.chat.Chat;
import com.illumo.chat.ChatMain;
import com.illumo.chat.R;
import com.illumo.chat.User;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<Chat> chats = new ArrayList<>();

    public void setItems(List<Chat> chats)
    {
        this.chats = new ArrayList<>();
        this.chats = chats;
        notifyDataSetChanged();
    }

    public void clearItems() {
        chats.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chats, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView lastMessage;
        TextView timeLastMessage;

        public ChatViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.chatName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            timeLastMessage = itemView.findViewById(R.id.timeLastMessage);


        }

        public void bind(final Chat chat)
        {
            title.setText(chat.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(Chat chat : chats)
                    {
                        chat.setSelected(false);
                    }
                    chat.setSelected(true);
                    ChatMain.selectedChat = getSelected();
                    ChatMain.SwitchToMessage();
                }
            });
        }
    }

    public List<Chat> getAll() {return chats;}

    public Chat getSelected()
    {
        Chat selected = null;
        for(int i = 0; i < chats.size(); i++)
        {
            if(chats.get(i).isSelected())
            {
                selected = chats.get(i);
            }
        }
        if(selected != null)
            return selected;
        else
        {
            selected = chats.get(1);
            return selected;
        }

    }
}
