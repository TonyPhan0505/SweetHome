package com.example.sweethome;

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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText editTextSendEmail;
    private Button sendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        /* get the TextView for for going back to login */
        TextView textViewBackToLogin = findViewById(R.id.textViewBackToLogin);
        /* initialize the EditText and button */
        editTextSendEmail = findViewById(R.id.editTextSendEmail);
        sendEmailButton = findViewById(R.id.buttonSendEmail);

        // Underline the text "Login"
        String text = textViewBackToLogin.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textViewBackToLogin.setText(spannableString);

        // Set the click listener for the Sign Up TextView
        textViewBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the ForgotPasswordActivity once the process is complete
            }
        });

        /* set the click listener for the send email button */
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* get the inputted email */
                String email = editTextSendEmail.getText().toString().trim();
                if (email.isEmpty()) { //check that the email is not empty
                    editTextSendEmail.setError("Email cannot be empty.");
                } else { //otherwise try to send the password reset email
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Firestore", "password reset email sent:success");
                                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent",Toast.LENGTH_SHORT).show();
                                        // Start LoginActivity
                                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish(); // Close the ForgotPasswordActivity
                                    }
                                    else {
                                        Log.w("Firestore", "password reset email sent:failure", task.getException());
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email could not be sent\n" + error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

}