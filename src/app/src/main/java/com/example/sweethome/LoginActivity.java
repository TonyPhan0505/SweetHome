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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the TextView for Sign Up
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);
        /* get the TextView for forgot password */
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        // Initialize the EditTexts and Button
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        /* initialize firebase auth */
        userAuth = FirebaseAuth.getInstance();
        /* set up a connection to our db and a reference to the users collection */
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

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
                finish(); // Close the LoginActivity once the process is complete
            }
        });

        /* set the on click listener for the forgot password TextView */
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* start ForgotPasswordActivity */
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish(); //close the LoginActivity
            }
        });

        // Set the click listener for the Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                // Call method to handle the login process
                attemptLogin(enteredUsername, password);
            }
        });
    }

    private void attemptLogin(String enteredUsername, String password) {

        // Check for empty username input
        if (enteredUsername.isEmpty()) {
            editTextUsername.setError("Username cannot be empty.");
            return;
        }
        /* check if the password is empty */
        if (password.trim().isEmpty()) {
            editTextPassword.setError("Password cannot be empty.");
            return;
        }

        usersRef.whereEqualTo("username", enteredUsername)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot q = task.getResult();
                        if (q != null) {
                            List docs = q.getDocuments();
                            if (docs.size() != 0 && q.getDocuments().get(0) != null && q.getDocuments().get(0).exists()) {
                                // Username exists, now actually try to sign the user into their account
                                DocumentSnapshot doc = q.getDocuments().get(0);
                                User user = doc.toObject(User.class);
                                if (user != null) {
                                    String email = user.getEmail();
                                    loginToUserAccount(email, enteredUsername, password);
                                } else {
                                    // Username does not exist
                                    editTextUsername.setError("Username does not exist. Please sign up.");
                                }
                            } else {
                                // Username does not exist
                                editTextUsername.setError("Username does not exist. Please sign up.");
                            }
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

    private void loginToUserAccount(String email, String username, String password) {

        userAuth.signInWithEmailAndPassword(email, password)
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
}
