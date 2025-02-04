package com.example.bai11acnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity {
    // Khai báo biến giao diện
    private ListView lv;
    private ArrayList<String> arrayWork;
    private ArrayAdapter<String> arrayAdapter;
    private EditText edtWork, edtHour, edtMinute;
    private TextView txtDate;
    private Button btnAddWork, btnDeleteMode, btnDeleteSelected;
    private boolean isDeleteMode = false;
    private SharedPreferences sf;
    private Set<String> selectedWorks = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view từ layout
        edtHour = findViewById(R.id.edthour);
        edtMinute = findViewById(R.id.edtminute);
        edtWork = findViewById(R.id.edtwork);
        btnAddWork = findViewById(R.id.btnadd);
        btnDeleteMode = findViewById(R.id.btnDeleteMode);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);
        lv = findViewById(R.id.listView);
        txtDate = findViewById(R.id.txtdate);

        // SharedPreferences
        sf = getSharedPreferences("WORK_LIST", Context.MODE_PRIVATE);

        // Khởi tạo danh sách công việc
        arrayWork = new ArrayList<>(loadWorkList());
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arrayWork);
        lv.setAdapter(arrayAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Hiển thị ngày hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtDate.setText("Hôm nay: " + dateFormat.format(new Date()));

        // Xử lý sự kiện nút thêm công việc
        btnAddWork.setOnClickListener(view -> addWork());

        // Chế độ xóa
        btnDeleteMode.setOnClickListener(view -> toggleDeleteMode());

        // Xóa các công việc đã chọn
        btnDeleteSelected.setOnClickListener(view -> confirmDelete());

        // Bắt sự kiện chọn công việc
        lv.setOnItemClickListener((adapterView, view, position, id) -> {
            if (isDeleteMode) {
                String selectedItem = arrayWork.get(position);
                if (selectedWorks.contains(selectedItem)) {
                    selectedWorks.remove(selectedItem);
                } else {
                    selectedWorks.add(selectedItem);
                }
            }
        });
    }

    private void addWork() {
        String work = edtWork.getText().toString().trim();
        String hour = edtHour.getText().toString().trim();
        String minute = edtMinute.getText().toString().trim();

        if (work.isEmpty() || hour.isEmpty() || minute.isEmpty()) {
            showAlert("Thông tin thiếu", "Vui lòng nhập đầy đủ thông tin công việc!");
        } else {
            String newWork = work + " - " + hour + ":" + minute;
            arrayWork.add(newWork);
            arrayAdapter.notifyDataSetChanged();
            saveWorkList();

            // Xóa nội dung sau khi thêm
            edtWork.setText("");
            edtHour.setText("");
            edtMinute.setText("");
        }
    }

    private void saveWorkList() {
        SharedPreferences.Editor editor = sf.edit();
        Set<String> workSet = new HashSet<>(arrayWork);
        editor.putStringSet("work_list", workSet);
        editor.apply();
    }

    private Set<String> loadWorkList() {
        return sf.getStringSet("work_list", new HashSet<>());
    }

    private void toggleDeleteMode() {
        isDeleteMode = !isDeleteMode;
        btnDeleteMode.setText(isDeleteMode ? "Thoát chế độ xóa" : "Chuyển sang xóa");
        btnDeleteSelected.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
        selectedWorks.clear();
    }

    private void confirmDelete() {
        if (selectedWorks.isEmpty()) {
            Toast.makeText(this, "Chưa chọn công việc nào để xóa!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa những công việc đã chọn không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteSelectedWorks())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteSelectedWorks() {
        arrayWork.removeAll(selectedWorks);
        selectedWorks.clear();
        saveWorkList();
        arrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Đã xóa công việc!", Toast.LENGTH_SHORT).show();
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
