package com.example.healthbuddypro.ShortTermMatching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.healthbuddypro.R;

public class WritePostFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextContent;
    private Spinner spinnerTopic;
    private Spinner spinnerLocation;
    private Spinner spinnerCategory;
    private Button btnSubmit;
    private OnPostSubmitListener onPostSubmitListener;

    public interface OnPostSubmitListener {
        void onPostSubmitted(String title, String content, String topic, String location, String category);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_post, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextContent = view.findViewById(R.id.editTextContent);
        spinnerTopic = view.findViewById(R.id.spinner_topic);
        spinnerLocation = view.findViewById(R.id.spinner_location);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        btnSubmit = view.findViewById(R.id.btn_submit);

        ArrayAdapter<CharSequence> adapterTopic = ArrayAdapter.createFromResource(getContext(), R.array.topic_array, android.R.layout.simple_spinner_item);
        spinnerTopic.setAdapter(adapterTopic);

        ArrayAdapter<CharSequence> adapterLocation = ArrayAdapter.createFromResource(getContext(), R.array.location_array, android.R.layout.simple_spinner_item);
        spinnerLocation.setAdapter(adapterLocation);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        spinnerCategory.setAdapter(adapterCategory);

        btnSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String content = editTextContent.getText().toString();
            String topic = spinnerTopic.getSelectedItem().toString();
            String location = spinnerLocation.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (!title.isEmpty() && !content.isEmpty()) {
                if (onPostSubmitListener != null) {
                    onPostSubmitListener.onPostSubmitted(title, content, topic, location, category);
                }
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setOnPostSubmitListener(OnPostSubmitListener listener) {
        this.onPostSubmitListener = listener;
    }
}
