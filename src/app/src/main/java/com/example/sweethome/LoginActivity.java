package com.example.sweethome;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);
        String text = textViewSignUp.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textViewSignUp.setText(spannableString);

        // Set the click listener for the Sign Up TextView
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignUpActivity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Update the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String enteredUsername = editTextUsername.getText().toString().trim();

        if (enteredUsername.isEmpty()) {
            editTextUsername.setError("Username cannot be empty.");
            return;
        }

        // Check if the username exists in the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(enteredUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Username exists, proceed to show user data
                            proceedToMainActivity(enteredUsername);
                        } else {
                            // Username does not exist
                            editTextUsername.setError("Username does not exist. Please sign up.");
                        }
                    } else {
                        // Handle errors here
                        Toast.makeText(LoginActivity.this, "Error checking username.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void proceedToMainActivity(String username) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish(); // Close the LoginActivity once the process is complete
    }
}
