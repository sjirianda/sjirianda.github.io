package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.LoginViewModel;
import com.example.myapplication.WeightEntry;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, createAccountButton;
    private LoginViewModel viewModel; //  defined here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.textUsername);
        passwordEditText = findViewById(R.id.textPassword);
        loginButton = findViewById(R.id.buttonLogin);
        createAccountButton = findViewById(R.id.buttonCreateAccount);

        // Initialize ViewModel and store it for use in onResume
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(LoginViewModel.class);

        // Auto-login if user already exists
        // if (viewModel.isUserLoggedIn()) {
        //     Toast.makeText(this, "Welcome back, " + viewModel.getStoredUsername(), Toast.LENGTH_SHORT).show();
        //     startActivity(new Intent(this, DataDisplayActivity.class));
        //     finish();
        // }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                if (viewModel.validateCredentials(enteredUsername, enteredPassword)) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, DataDisplayActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = usernameEditText.getText().toString();
                String newPassword = passwordEditText.getText().toString();

                viewModel.registerUser(newUsername, newPassword);

                Toast.makeText(MainActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TEST: sample weight entries
        viewModel.addWeightEntry("2025-07-01", 178.0f);
        viewModel.addWeightEntry("2025-07-05", 182.4f);
        viewModel.addWeightEntry("2025-07-10", 175.3f);

        // TEST: Search for the closest weight to 180.0
        WeightEntry closest = viewModel.searchClosestWeight(180.0f);
        if (closest != null) {
            Toast.makeText(this, "Closest to 180: " + closest.getWeight() + " lbs on " + closest.getDate(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No entries in BST.", Toast.LENGTH_SHORT).show();
        }
    }
}
