package com.tunglain.atmj02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private EditText edDate;
    private EditText edInfo;
    private EditText edAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edDate = findViewById(R.id.ed_date);
        edInfo = findViewById(R.id.ed_info);
        edAmount = findViewById(R.id.ed_amount);
    }

    public void add(View view) {
        String date = edDate.getText().toString();
        String info = edInfo.getText().toString();
        int amount = Integer.parseInt(edAmount.getText().toString());
        ExpenseHelper expenseHelper = new ExpenseHelper(this);
        ContentValues values = new ContentValues();
        values.put("cdate",date);
        values.put("info",info);
        values.put("amount",amount);
        long id = expenseHelper.getWritableDatabase()
                .insert("expense", null, values);
        if (id > -1) {
            Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "新增失敗", Toast.LENGTH_SHORT).show();
        }
    }
}
