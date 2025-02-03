package com.example.bai11acnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    // Khai báo biến giao diện
    ListView lv;
    ArrayList<String> arrayWork;
    ArrayAdapter<String> arrayAdapter;
    EditText edtWork, edtHour, edtMinute;
    TextView txtDate;
    Button btnAddWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view từ layout
        edtHour = findViewById(R.id.edthour);
        edtMinute = findViewById(R.id.edtminute);
        edtWork = findViewById(R.id.edtwork);
        btnAddWork = findViewById(R.id.btnadd);
        lv = findViewById(R.id.listView);
        txtDate = findViewById(R.id.txtdate);

        // Khởi tạo danh sách công việc
        arrayWork = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayWork);
        lv.setAdapter(arrayAdapter);

        // Hiển thị ngày hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtDate.setText("Hôm nay: " + dateFormat.format(new Date()));

        // Xử lý sự kiện khi nhấn nút thêm công việc
        btnAddWork.setOnClickListener(view -> addWork());
    }

    private void addWork() {
        String work = edtWork.getText().toString().trim();
        String hour = edtHour.getText().toString().trim();
        String minute = edtMinute.getText().toString().trim();

        if (work.isEmpty() || hour.isEmpty() || minute.isEmpty()) {
            showAlert("Thông tin thiếu", "Vui lòng nhập đầy đủ thông tin công việc!");
        } else {
            arrayWork.add(work + " - " + hour + ":" + minute);
            arrayAdapter.notifyDataSetChanged();

            // Xóa nội dung sau khi thêm
            edtWork.setText("");
            edtHour.setText("");
            edtMinute.setText("");
        }
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
