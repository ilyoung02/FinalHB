package com.example.healthbuddypro.Matching.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthbuddypro.R;

import java.util.List;

// RecyclerView의 어댑터 => 채팅 메시지 표시
// Message 객체 이용, 사용자가 보낸 메시지와 상대방이 보낸 메시지 구분
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;

    // ViewType 상수 정의
    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 2;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.isSender() ? VIEW_TYPE_MY_MESSAGE : VIEW_TYPE_OTHER_MESSAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new MyMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new OtherMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof MyMessageViewHolder) {
            ((MyMessageViewHolder) holder).bind(message);
        } else if (holder instanceof OtherMessageViewHolder) {
            ((OtherMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // 내 메시지 ViewHolder
    static class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textSenderName;  // 보낸 사람 이름을 위한 TextView

        MyMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textSenderName = itemView.findViewById(R.id.textSenderName);  // 추가
        }

        void bind(Message message) {
            textMessage.setText(message.getContent());
            textSenderName.setText(message.getSenderName());  // 이름 표시
        }
    }

    // 상대방 메시지 ViewHolder
    static class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textSenderName;  // 보낸 사람 이름을 위한 TextView

        OtherMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textSenderName = itemView.findViewById(R.id.textSenderName);  // 추가
        }

        void bind(Message message) {
            textMessage.setText(message.getContent());
            textSenderName.setText(message.getSenderName());  // 이름 표시
        }
    }
}

