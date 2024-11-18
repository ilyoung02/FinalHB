package com.example.healthbuddypro.ShortTermMatching;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthbuddypro.R;

import java.util.List;

public class DayOfWeekAdapter extends RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder> {

    private final List<String> daysOfWeek;
    private final OnDayClickListener listener;
    private int selectedPosition = 0; // 선택된 요일을 표시하기 위한 변수

    public interface OnDayClickListener {
        void onDayClick(String day);
    }

    public DayOfWeekAdapter(List<String> daysOfWeek, OnDayClickListener listener) {
        this.daysOfWeek = daysOfWeek;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_of_week, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String day = daysOfWeek.get(position);
        holder.textViewDay.setText(day);

        // 일요일일 경우 빨간색으로 설정
        if (day.equals("일")) {
            holder.textViewDay.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
        } else {
            holder.textViewDay.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
        }

        // 선택된 항목을 강조 표시
        if (position == selectedPosition) {
            holder.textViewDay.setTextSize(28);  // 선택된 요일은 더 크게 표시
            holder.textViewDay.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_blue_dark));  // 선택된 요일 색상 변경
        } else {
            holder.textViewDay.setTextSize(24);  // 기본 크기
        }

        // 클릭 리스너
        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;  // 이전에 선택된 항목을 저장
            selectedPosition = holder.getAdapterPosition();  // 새로운 선택된 항목으로 업데이트

            // 선택된 항목이 변경되었으므로, 이전 선택된 항목과 현재 선택된 항목을 업데이트
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            listener.onDayClick(day);  // 선택된 요일 전달
        });
    }

    @Override
    public int getItemCount() {
        return daysOfWeek.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay;

        ViewHolder(View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.textViewDay);
        }
    }
}
