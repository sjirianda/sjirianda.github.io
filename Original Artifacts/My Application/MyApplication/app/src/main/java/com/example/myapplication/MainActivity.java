package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.textUsername);
        passwordEditText = findViewById(R.id.textPassword);
        loginButton = findViewById(R.id.buttonLogin);
        createAccountButton = findViewById(R.id.buttonCreateAccount);

        // Check for stored user credentials
        final SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("username", null);
        String storedPassword = sharedPreferences.getString("password", null);

        // Check if the user is already logged in
        if (storedUsername != null && storedPassword != null) {
            // Proceed to the next activity (skip login screen)
            // You can modify this to jump to a different screen if logged in
            Toast.makeText(this, "Welcome back, " + storedUsername, Toast.LENGTH_SHORT).show();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login logic
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                String storedUsername = sharedPreferences.getString("username", null);
                String storedPassword = sharedPreferences.getString("password", null);

                // Debug logs
                System.out.println("Entered Username: " + enteredUsername);
                System.out.println("Stored Username: " + storedUsername);
                System.out.println("Entered Password: " + enteredPassword);
                System.out.println("Stored Password: " + storedPassword);

                // In a real app, you'd check these credentials against a backend/database
                if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Navigate to DataDisplayActivity
                    Intent intent = new Intent(MainActivity.this, DataDisplayActivity.class);
                    startActivity(intent);
                    finish(); // Optionally close the login activity after successful login
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new account by storing username and password
                String newUsername = usernameEditText.getText().toString();
                String newPassword = passwordEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", newUsername);
                editor.putString("password", newPassword);
                editor.apply();

                // Debug log to ensure the username and password are saved correctly
                System.out.println("Saved Username: " + newUsername);
                System.out.println("Saved Password: " + newPassword);

                Toast.makeText(MainActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
