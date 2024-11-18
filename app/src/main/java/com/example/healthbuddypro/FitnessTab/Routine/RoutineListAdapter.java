package com.example.healthbuddypro.FitnessTab.Routine;

// RoutineListAdapter.java (Adapter class)
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.healthbuddypro.R;

import java.util.ArrayList;

public class RoutineListAdapter extends ArrayAdapter<Routine> {
    private Context context;
    private int resource;
    private ArrayList<Routine> routines;

    public RoutineListAdapter(Context context, int resource, ArrayList<Routine> routines) {
        super(context, resource, routines);
        this.context = context;
        this.resource = resource;
        this.routines = routines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.hbdName);
        TextView routineTitleTextView = convertView.findViewById(R.id.routinelist_routineTitle);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_routinelist);
        ImageButton editButton = convertView.findViewById(R.id.edit_routinelist);

        Routine routine = routines.get(position);

        nameTextView.setText(routine.getName());
        routineTitleTextView.setText(routine.getRoutineTitle());

        // 삭제 버튼
        deleteButton.setOnClickListener(v -> {
            // 삭제 로직
        });

        // 수정 버튼
        editButton.setOnClickListener(v -> {
            // 수정 로직
        });

        return convertView;
    }
}
