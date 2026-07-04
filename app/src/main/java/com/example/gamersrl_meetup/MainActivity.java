package com.example.gamersrl_meetup;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.example.gamersrl_meetup.HeaderMenuActivity;
// Sources for logging in Android Studio with Log.d:
// https://stackoverflow.com/a/16783890
// https://developer.android.com/studio/debug
import android.content.Intent;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.stripe.android.identity.*;


public class MainActivity extends AppCompatActivity {
    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
    final String strLogTag = "StartPage - ";

    private IdentityVerificationSheet identityVerificationSheet;
    private TextView mStartPageTextView;
    private Button mStartPageButton;
    private Button mLoginButton;
    private Button mSignupButton;

    /**
     * Create the view.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(strLogTag, (strLogTag + "creating view"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Uri logoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher);


        mStartPageTextView = findViewById(R.id.start_page_header);
        mStartPageButton = findViewById(R.id.start_page_button);
        mLoginButton = findViewById(R.id.to_login_button);
        mSignupButton = findViewById(R.id.to_signup_button);
        mStartPageButton.setText("VERIFY");

        mStartPageTextView.setText(R.string.start_page_activity_name);

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

        mStartPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(strLogTag, "VERIFY button clicked");
//                startActivity(new Intent(MainActivity.this, GamePageActivity.class));
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
                                MainActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
            }
        }).start();
    }
}