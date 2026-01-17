package com.tastehaven.rms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tastehaven.rms.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, passwordEditText;
    private Spinner roleSpinner;
    private Button registerButton;
    private TextView loginRedirectText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerButton = findViewById(R.id.registerButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Set up Spinner with role options
        String[] roles = {"Customer", "Manager", "Waiter", "Chef"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Register button click listener
        registerButton.setOnClickListener(v -> registerUser());

        // Login redirect click listener
        loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String selectedRole = roleSpinner.getSelectedItem().toString().toLowerCase();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get user ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Store user data in Realtime Database
                        DatabaseReference userRef = mDatabase.child("users").child(userId);
                        userRef.child("name").setValue(name);
                        userRef.child("email").setValue(email);
                        userRef.child("phone").setValue(phone);
                        userRef.child("role").setValue(selectedRole)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,
                                                "Registration successful", Toast.LENGTH_SHORT).show();

                                        // Navigate to appropriate activity based on role
                                        Intent intent;
                                        switch (selectedRole) {
                                            case "manager":
                                                intent = new Intent(RegisterActivity.this, ManagerHomeActivity.class);
                                                break;
                                            case "waiter":
                                                intent = new Intent(RegisterActivity.this, WaiterHomeActivity.class);
                                                break;
                                            case "chef":
                                                intent = new Intent(RegisterActivity.this, ChefHomeActivity.class);
                                                break;
                                            default:
                                                intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}