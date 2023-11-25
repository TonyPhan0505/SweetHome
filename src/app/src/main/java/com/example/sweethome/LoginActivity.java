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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    private final String DOMAIN = "@cmput301f23t17.com";
    private FirebaseAuth userAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and if so take them to the main activity
        FirebaseUser currentUser = userAuth.getCurrentUser();
        if(currentUser != null) {
            String usernameWithDomain = currentUser.getEmail();
            String username = usernameWithDomain.replace(DOMAIN, "");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USERNAME", username); //send username to main activity
            startActivity(intent);
            finish(); // Close the LoginActivity once the process is complete
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the TextView for Sign Up
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);
        // Initialize the EditTexts and Button
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        // Initialize Firebase Auth
        userAuth = FirebaseAuth.getInstance();

        // Underline the text "Sign Up"
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

        // Set the click listener for the Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                // Call method to handle the login process
                attemptLogin(enteredUsername, password);
            }
        });
    }

    private void attemptLogin(String enteredUsername, String password) {

        // Check for empty username input
        if (enteredUsername.trim().isEmpty()) {
            editTextUsername.setError("Username cannot be empty.");
            return;
        }
        /* check if the password is empty */
        if (password.trim().isEmpty()) {
            editTextPassword.setError("Password cannot be empty.");
            return;
        }
        /* now actually try to sign the user into their account */
        loginToUserAccount(enteredUsername, password);

//        // Check if the username exists in the Firestore database
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users")
//                .document(enteredUsername)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document != null && document.exists()) {
//                            // Username exists, proceed to show user data
//                            proceedToMainActivity(enteredUsername);
//                        } else {
//                            // Username does not exist
//                            editTextUsername.setError("Username does not exist. Please sign up.");
//                        }
//                    } else {
//                        // Handle errors here
//                        Toast.makeText(LoginActivity.this, "Error checking username.", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void loginToUserAccount(String username, String password) {
        /* make an "email" so we can use the firebase email and password user creation */
        String usernameWithDomain = username + DOMAIN;

        userAuth.signInWithEmailAndPassword(usernameWithDomain, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("Firestore", "user login:success");
                            Toast.makeText(LoginActivity.this, "Login successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("USERNAME", username); //send username to main activity
                            startActivity(intent);
                            finish(); // Close the LoginActivity once the process is complete
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firestore", "user login:failure", task.getException());
                            editTextUsername.setError("Incorrect username or password.");
                            editTextPassword.setError("Incorrect username or password.");
                            Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    private void proceedToMainActivity(String username) {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        intent.putExtra("USERNAME", username);
//        startActivity(intent);
//        finish(); // Close the LoginActivity once the process is complete
//    }
}
