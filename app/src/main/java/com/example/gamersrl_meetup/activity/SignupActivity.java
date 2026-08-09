package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.stripe.android.identity.IdentityVerificationSheet;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SignupPage";

    private FirebaseAuth firebaseAuth;

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;
    private EditText dobEditText;

    private Button verifyIdentityButton;
    private Button signUpButton;

    private IdentityVerificationSheet identityVerificationSheet;

    /*
     * For this sandbox project, this becomes true when the user completes
     * the Stripe Identity sheet.
     *
     * In a production application, the backend/webhook should determine
     * whether verification was truly approved.
     */
    private boolean identityVerificationCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dobEditText = findViewById(R.id.dobEditText);

        verifyIdentityButton = findViewById(R.id.verifyIdentityButton);
        signUpButton = findViewById(R.id.signUpButton);

        /*
         * Do not allow account creation until the user completes
         * the Stripe sandbox verification flow.
         */
        signUpButton.setEnabled(false);

        configureStripeIdentity();

        verifyIdentityButton.setOnClickListener(v -> {
            if (validateForm()) {
                didTapVerifyButton();
            }
        });

        signUpButton.setOnClickListener(v -> createFirebaseAccount());
    }

    private void configureStripeIdentity() {
        Uri logoUri = Uri.parse(
                "android.resource://" +
                        getPackageName() +
                        "/" +
                        R.mipmap.ic_launcher
        );

        identityVerificationSheet =
                IdentityVerificationSheet.Companion.create(
                        this,
                        new IdentityVerificationSheet.Configuration(logoUri),
                        verificationResult -> {
                            Log.d(
                                    LOG_TAG,
                                    "Stripe result: " + verificationResult
                            );

                            if (verificationResult instanceof
                                    IdentityVerificationSheet
                                            .VerificationFlowResult
                                            .Completed) {

                                identityVerificationCompleted = true;
                                signUpButton.setEnabled(true);
                                verifyIdentityButton.setEnabled(false);

                                Toast.makeText(
                                        SignupActivity.this,
                                        "Identity verification completed. " +
                                                "You may now create your account.",
                                        Toast.LENGTH_LONG
                                ).show();

                            } else if (verificationResult instanceof
                                    IdentityVerificationSheet
                                            .VerificationFlowResult
                                            .Canceled) {

                                identityVerificationCompleted = false;
                                signUpButton.setEnabled(false);

                                Toast.makeText(
                                        SignupActivity.this,
                                        "Identity verification was canceled.",
                                        Toast.LENGTH_LONG
                                ).show();

                            } else if (verificationResult instanceof
                                    IdentityVerificationSheet
                                            .VerificationFlowResult
                                            .Failed) {

                                identityVerificationCompleted = false;
                                signUpButton.setEnabled(false);

                                Toast.makeText(
                                        SignupActivity.this,
                                        "Identity verification failed.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }

//    Validation of the inputs in the form
    private boolean validateForm() {
        String fullName =
                fullNameEditText.getText().toString().trim();

        String email =
                emailEditText.getText().toString().trim();

        String password =
                passwordEditText.getText().toString();

        String phone =
                phoneEditText.getText().toString().trim();

        String dob =
                dobEditText.getText().toString().trim();

        if (fullName.isEmpty()) {
            fullNameEditText.setError("Full name is required");
            fullNameEditText.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError(
                    "Password must be at least 6 characters"
            );
            passwordEditText.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            phoneEditText.requestFocus();
            return false;
        }

        if (dob.isEmpty()) {
            dobEditText.setError("Date of birth is required");
            dobEditText.requestFocus();
            return false;
        }

        return true;
    }
// Firebase account creator
    private void createFirebaseAccount() {
        if (!identityVerificationCompleted) {
            Toast.makeText(
                    this,
                    "Complete identity verification before signing up.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        if (!validateForm()) {
            return;
        }

        String email =
                emailEditText.getText().toString().trim();

        String password =
                passwordEditText.getText().toString();

        signUpButton.setEnabled(false);

        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        createFirestoreUser();
                    }
                    else {
                        /*
                         * Re-enable it because verification was already
                         * completed and the user may correct the signup error.
                         */
                        signUpButton.setEnabled(true);

                        String errorMessage =
                                "Account creation failed.";

                        if (task.getException() != null &&
                                task.getException().getMessage() != null) {
                            errorMessage =
                                    task.getException().getMessage();
                        }

                        Toast.makeText(
                                SignupActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void didTapVerifyButton() {
        Log.d(LOG_TAG, "Starting Stripe verification");

        verifyIdentityButton.setEnabled(false);

        new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(
                        "http://10.0.2.2:8080/create-verification-session"
                );

                connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                OutputStream outputStream =
                        connection.getOutputStream();

                outputStream.write(new byte[0]);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode < 200 || responseCode >= 300) {
                    throw new IllegalStateException(
                            "Server returned response code " +
                                    responseCode
                    );
                }

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()
                        )
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject responseJson =
                        new JSONObject(response.toString());

                String verificationSessionId =
                        responseJson.getString("id");

                String ephemeralKeySecret =
                        responseJson.getString(
                                "ephemeral_key_secret"
                        );

                runOnUiThread(() -> {
                    Log.d(
                            LOG_TAG,
                            "Presenting Stripe Identity sheet"
                    );

                    identityVerificationSheet.present(
                            verificationSessionId,
                            ephemeralKeySecret
                    );
                });

            } catch (Exception e) {
                Log.e(
                        LOG_TAG,
                        "Error starting Stripe verification",
                        e
                );

                runOnUiThread(() -> {
                    verifyIdentityButton.setEnabled(true);

                    String errorMessage =
                            e.getMessage() == null
                                    ? "Could not start verification."
                                    : e.getMessage();

                    Toast.makeText(
                            SignupActivity.this,
                            errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                });

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void createFirestoreUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userData = new HashMap<>();

        userData.put(
                "name",
                fullNameEditText.getText().toString().trim()
        );

        userData.put("email", user.getEmail());

        // All new users start as regular users
        userData.put("isAdmin", false);

        db.collection("users")
                .document(user.getUid())
                .set(userData)
                .addOnSuccessListener(unused -> {
                    Log.d(LOG_TAG, "Firestore user created");

                    Toast.makeText(
                            SignupActivity.this,
                            "Account created successfully.",
                            Toast.LENGTH_LONG
                    ).show();

                    Intent intent = new Intent(
                            SignupActivity.this,
                            AppContentActivity.class
                    );

                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(
                            LOG_TAG,
                            "Could not create Firestore user",
                            e
                    );

                    signUpButton.setEnabled(true);

                    Toast.makeText(
                            SignupActivity.this,
                            "Account created, but user profile could not be saved.",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}