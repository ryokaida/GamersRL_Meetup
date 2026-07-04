package com.example.gamersrl_meetup.activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gamersrl_meetup.R;
import com.stripe.android.identity.IdentityVerificationSheet;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.view.View;


public class SignupActivity extends AppCompatActivity {
    private IdentityVerificationSheet identityVerificationSheet;
    private Button verifyIdentityButton;
    final String strLogTag = "SignupPage - ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        Uri logoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        identityVerificationSheet = IdentityVerificationSheet.Companion.create(
                this,
                new IdentityVerificationSheet.Configuration(logoUri),
                verificationResult -> {
                    Log.d(strLogTag, "Stripe verification result: " + verificationResult.toString());

                    if (verificationResult instanceof IdentityVerificationSheet.VerificationFlowResult.Completed) {
                        Log.d(strLogTag, "Verification completed");
                    } else if (verificationResult instanceof IdentityVerificationSheet.VerificationFlowResult.Canceled) {
                        Log.d(strLogTag, "Verification canceled");
                    } else if (verificationResult instanceof IdentityVerificationSheet.VerificationFlowResult.Failed) {
                        Log.d(strLogTag, "Verification failed");
                    }
                }
        );
        verifyIdentityButton = findViewById(R.id.verifyIdentityButton);

        verifyIdentityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didTapVerifyButton();
            }
        });
    }

    private void didTapVerifyButton() {
        Log.d(strLogTag, "didTapVerifyButton started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8080/create-verification-session");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(new byte[0]);
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );

                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    JSONObject responseJson = new JSONObject(response.toString());

                    String verificationSessionId = responseJson.getString("id");
                    String ephemeralKeySecret = responseJson.getString("ephemeral_key_secret");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(strLogTag, "About to present Stripe sheet");
                            identityVerificationSheet.present(
                                    verificationSessionId,
                                    ephemeralKeySecret
                            );
                        }
                    });

                } catch (Exception e) {
                    Log.e(strLogTag, "Error starting Stripe verification", e);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(
                                    SignupActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}