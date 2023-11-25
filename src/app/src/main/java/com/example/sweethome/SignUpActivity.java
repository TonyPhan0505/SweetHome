package com.example.sweethome;
// source : https://firebase.google.com/docs/auth/android/password-auth#java_2

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signUpButton;
    private final String DOMAIN = "@cmput301f23t17.com";
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get the TextView for Login
        TextView textViewSignIn = findViewById(R.id.textViewLogin);
        // Initialize the EditTexts and Button
        usernameEditText = findViewById(R.id.editTextUsernameSignUp);
        passwordEditText = findViewById(R.id.editTextPasswordSignUp);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPasswordSignUp);
        signUpButton = findViewById(R.id.buttonSignUp);
        // Initialize Firebase Auth
        userAuth = FirebaseAuth.getInstance();

        // Underline the text "Login"
        String textSignIn = textViewSignIn.getText().toString();
        SpannableString spannableStringSignIn = new SpannableString(textSignIn);
        spannableStringSignIn.setSpan(new UnderlineSpan(), 0, textSignIn.length(), 0);
        textViewSignIn.setText(spannableStringSignIn);

        // Set the click listener for the Login TextView
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
                String desiredUsername = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                // Call method to handle the sign-up process
                attemptSignUp(desiredUsername, password, confirmPassword);
            }
        });
    }

    private void attemptSignUp(String desiredUsername, String password, String confirmPassword) {

        // Check for empty or invalid username input, etc.
        if (desiredUsername.trim().isEmpty() || desiredUsername.contains(" ") || desiredUsername.contains("@")) {
            usernameEditText.setError("Username cannot be empty, contain spaces, or contain the @ symbol.");
            return;
        }
        /* check if the password contains any spaces or is empty */
        if (password.trim().isEmpty() || password.contains(" ")) {
            passwordEditText.setError("Password cannot be empty or contain spaces.");
            return;
        }
        /* check that the passwords match */
        if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Passwords do not match.");
            return;
        }
        /* now actually try to create the user account */
        createUserAccount(desiredUsername, password);
//        // Check if the username is already taken
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users")
//                .whereEqualTo("username", desiredUsername)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        if (task.getResult().isEmpty()) {
//                            // Username is not taken, proceed with signup
//                            createUserAccount(desiredUsername);
//                        } else {
//                            // Username is taken, prompt user to choose another
//                            usernameEditText.setError("Username is already taken. Choose a different one.");
//                        }
//                    } else {
//                        // Handle errors here
//                        Toast.makeText(SignUpActivity.this, "Error checking username availability.", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void createUserAccount(String username, String password) {
        /* make an "email" so we can use the firebase email and password user creation */
        String usernameWithDomain = username + DOMAIN;
        userAuth.createUserWithEmailAndPassword(usernameWithDomain, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            // Here you might want to start the LoginActivity or MainActivity
                            // depending on how you want to proceed after account creation
                            // currently we go to the LoginActivity
                            Log.d("Firestore", "create new user:success");
                            Toast.makeText(SignUpActivity.this, "Account creation successful",Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut(); // creation of a new account automatically logs a user in so log them out before sending them to login page
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Close the SignUpActivity once the process is complete
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w("Firestore", "create new user:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                usernameEditText.setError("Username is already taken. Please choose a different one.");
                            } else {
                                Toast.makeText(SignUpActivity.this, "Account creation failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Create a new user with a unique username
//        db.collection("users").document(username).set(new User(username))
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
//                    // Here you might want to start the LoginActivity or MainActivity
//                    // depending on how you want to proceed after account creation
//                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish(); // Close the SignUpActivity once the process is complete
//                })
//                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Error creating user", Toast.LENGTH_SHORT).show());
    }
}
