package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DataDisplayActivity extends AppCompatActivity {

    private GridLayout gridData;
    private Button buttonAddData, buttonUpdateData, buttonGoToSMS;
    private EditText dateEditText, weightEditText;
    private DatabaseHelper databaseHelper;
    private int currentEditId = -1; // To track the item being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        gridData = findViewById(R.id.gridData);
        buttonAddData = findViewById(R.id.buttonAddData);
        buttonUpdateData = findViewById(R.id.buttonUpdateData);
        buttonGoToSMS = findViewById(R.id.buttonSMS); // NEW: SMS Button

        databaseHelper = new DatabaseHelper(this);

        // Create input fields
        dateEditText = new EditText(this);
        weightEditText = new EditText(this);
        dateEditText.setHint("Enter Date");
        weightEditText.setHint("Enter Weight");
        dateEditText.setPadding(8, 8, 8, 8);
        weightEditText.setPadding(8, 8, 8, 8);

        // Setup input field layout
        LinearLayout inputFieldsLayout = new LinearLayout(this);
        inputFieldsLayout.setOrientation(LinearLayout.HORIZONTAL);
        inputFieldsLayout.setGravity(Gravity.CENTER_VERTICAL);
        inputFieldsLayout.setPadding(16, 16, 16, 16);
        inputFieldsLayout.addView(dateEditText);
        inputFieldsLayout.addView(weightEditText);

        // Add input fields to main layout
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.addView(inputFieldsLayout);

        // Add click listeners
        buttonAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataRow();
            }
        });

        buttonUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataRow();
            }
        });

        buttonGoToSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DataDisplayActivity.this, SMSPermissionActivity.class));
            }
        });

        loadDataFromDatabase();
    }

    private void addDataRow() {
        String date = dateEditText.getText().toString();
        String weight = weightEditText.getText().toString();

        if (date.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = databaseHelper.insertData(date, weight);
        if (isInserted) {
            Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
            loadDataFromDatabase();
        } else {
            Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show();
        }

        dateEditText.setText("");
        weightEditText.setText("");
    }

    private void updateDataRow() {
        String date = dateEditText.getText().toString();
        String weight = weightEditText.getText().toString();

        if (date.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentEditId == -1) {
            Toast.makeText(this, "No row selected to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = databaseHelper.updateData(currentEditId, date, weight);
        if (isUpdated) {
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            loadDataFromDatabase();
        } else {
            Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
        }

        currentEditId = -1;
        buttonAddData.setVisibility(View.VISIBLE);
        buttonUpdateData.setVisibility(View.GONE);
        dateEditText.setText("");
        weightEditText.setText("");
    }

    private void loadDataFromDatabase() {
        gridData.removeAllViews();
        Cursor cursor = databaseHelper.getAllData();
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DATE));
            String weight = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_WEIGHT));
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));

            TextView dateTextView = new TextView(this);
            TextView weightTextView = new TextView(this);
            Button editButton = new Button(this);
            Button deleteButton = new Button(this);

            dateTextView.setText(date);
            weightTextView.setText(weight);
            editButton.setText("Edit");
            deleteButton.setText("Delete");

            dateTextView.setPadding(8, 8, 8, 8);
            weightTextView.setPadding(8, 8, 8, 8);
            editButton.setPadding(8, 8, 8, 8);
            deleteButton.setPadding(8, 8, 8, 8);

            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);
            rowLayout.addView(dateTextView);
            rowLayout.addView(weightTextView);
            rowLayout.addView(editButton);
            rowLayout.addView(deleteButton);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
            params.columnSpec = GridLayout.spec(0);
            rowLayout.setLayoutParams(params);
            gridData.addView(rowLayout);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isDeleted = databaseHelper.deleteData(id);
                    if (isDeleted) {
                        Toast.makeText(DataDisplayActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
                        loadDataFromDatabase();
                    } else {
                        Toast.makeText(DataDisplayActivity.this, "Failed to delete entry", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateEditText.setText(date);
                    weightEditText.setText(weight);
                    currentEditId = id;
                    buttonAddData.setVisibility(View.GONE);
                    buttonUpdateData.setVisibility(View.VISIBLE);
                }
            });
        }
        cursor.close();
    }
}
