package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get the TextView for Sign in
        TextView textViewSignIn = findViewById(R.id.textViewSignIn);

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

        // TODO: Add logic for handling user sign up
    }
}
