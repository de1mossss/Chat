package com.illumo.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.illumo.chat.Chat;
import com.illumo.chat.Message;
import com.illumo.chat.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> messageList = new ArrayList<>();

    public void setItems(List<Message> messageList)
    {
        this.messageList = new ArrayList<>();
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        messageList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recuclerview_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        holder.bind(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView timeText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.messageOther);
            timeText = itemView.findViewById(R.id.timeMessageOther);
        }

        public void bind(Message message)
        {
            messageText.setText(message.getContent());
            timeText.setText(message.getCreateAt());
        }
    }

}
