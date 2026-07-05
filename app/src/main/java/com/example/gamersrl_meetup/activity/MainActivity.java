package com.example.gamersrl_meetup.activity;
import com.example.gamersrl_meetup.R;
import androidx.appcompat.app.AppCompatActivity;
// Sources for logging in Android Studio with Log.d:
// https://stackoverflow.com/a/16783890
// https://developer.android.com/studio/debug
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.stripe.android.identity.*;


public class MainActivity extends AppCompatActivity {
    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
    final String strLogTag = "StartPage - ";
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

        mStartPageTextView = findViewById(R.id.start_page_header);
        mLoginButton = findViewById(R.id.to_login_button);
        mSignupButton = findViewById(R.id.to_signup_button);

        mStartPageTextView.setText(R.string.start_page_activity_name);


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(strLogTag, "SIGNUP button clicked");
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(strLogTag, "LOGIN button clicked");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}