package com.example.healthbuddypro.Matching.Chat;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthbuddypro.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    private TextView messageTextView;

    public ChatViewHolder(View itemView) {
        super(itemView);
        messageTextView = itemView.findViewById(R.id.textViewMessage);
    }

    public void bind(Message message) {
        messageTextView.setText(message.getContent());
        // 송신자/수신자에 따라 스타일 변경 가능
    }
}
