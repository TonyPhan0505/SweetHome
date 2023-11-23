package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

// Import Firebase libraries
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.sweethome.User;


public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get the TextView for Sign in
        TextView textViewSignIn = findViewById(R.id.textViewSignIn);
        // Initialize the EditTexts and Button
        usernameEditText = findViewById(R.id.editTextUsernameSignUp);
        signUpButton = findViewById(R.id.buttonSignUp);

        // Underline the text "Sign in"
        String textSignIn = textViewSignIn.getText().toString();
        SpannableString spannableStringSignIn = new SpannableString(textSignIn);
        spannableStringSignIn.setSpan(new UnderlineSpan(), 0, textSignIn.length(), 0);
        textViewSignIn.setText(spannableStringSignIn);

        // Set the click listener for the Sign In TextView
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // If LoginActivity is already in the stack, bring it to front
                startActivity(intent);
            }
        });

        // Set the click listener for the Sign Up Button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your method to handle the sign-up process
                attemptSignUp();
            }
        });
    }

    private void attemptSignUp() {
        String desiredUsername = usernameEditText.getText().toString().trim();

        // Check for empty or invalid username input, etc.
        if (desiredUsername.isEmpty()) {
            usernameEditText.setError("Username cannot be empty.");
            return;
        }

        // Check if the username is already taken
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", desiredUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().isEmpty()) {
                            // Username is not taken, proceed with signup
                            createUserAccount(desiredUsername);
                        } else {
                            // Username is taken, prompt user to choose another
                            usernameEditText.setError("Username is already taken. Choose a different one.");
                        }
                    } else {
                        // Handle errors here
                        Toast.makeText(SignUpActivity.this, "Error checking username availability.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserAccount(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a unique username
        db.collection("users").document(username).set(new User(username))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    // Here you might want to start the LoginActivity or MainActivity
                    // depending on how you want to proceed after account creation
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Close the SignUpActivity once the process is complete
                })
                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Error creating user", Toast.LENGTH_SHORT).show());
    }
}
