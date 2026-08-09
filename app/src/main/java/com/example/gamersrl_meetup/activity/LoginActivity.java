package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        firebaseAuth = FirebaseAuth.getInstance();

        loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());
    }

//    Login handling method. Data verification and firebase authentication.
    private void loginUser() {

        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString();

        if (email.isEmpty()) {
            loginEmailEditText.setError("Enter your email");
            loginEmailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPasswordEditText.setError("Enter your password");
            loginPasswordEditText.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Intent intent = new Intent(
                                LoginActivity.this,
                                AppContentActivity.class
                        );

                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
