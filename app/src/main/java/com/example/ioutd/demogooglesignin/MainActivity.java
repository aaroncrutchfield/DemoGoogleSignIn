package com.example.ioutd.demogooglesignin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    private TextView tvName;
    private TextView tvGivenName;
    private TextView tvFamilyName;
    private TextView tvEmail;
    private TextView tvID;
    private ImageView ivPhoto;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_name);
        tvGivenName = findViewById(R.id.tv_given_name);
        tvFamilyName = findViewById(R.id.tv_family_name);
        tvEmail = findViewById(R.id.tv_email);
        tvID = findViewById(R.id.tv_id);

        ivPhoto = findViewById(R.id.iv_photo);

        // TODO (9): Customize the size of the button
        SignInButton signInButton = findViewById(R.id.btn_sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // TODO (10): Sign in when the button is clicked
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // TODO (3): Configure the sign-in to request the user's ID, email address, and basic profile
        // these included in the DEFAULT_SIGN_IN
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // TODO (4): Build a GoogleSignInClient with the GoogleSignInOptions
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    // TODO (5): Override onStart method
    @Override
    protected void onStart() {
        super.onStart();
        // TODO (6): Check for an existing Google Sign-In account
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        // TODO (7): Update the UI accordingly
        updateUI(signInAccount);
    }

    // TODO (11): Get the signInIntent and startActivityForResult
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);
            handleSignInResult(signInAccountTask);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount signInAccount = completedTask.getResult(ApiException.class);
            updateUI(signInAccount);
        } catch (ApiException e) {
            e.printStackTrace();
            updateUI(null);
        }
    }

    // TODO (?): Fill in this todo
    private void updateUI(GoogleSignInAccount signInAccount) {
        String personName = "";
        String personGivenName = "";
        String personFamilyName = "";
        String personEmail = "";
        String personId = "";
        Uri personPhoto = null;

        if (signInAccount != null) {
            personName = signInAccount.getDisplayName();
            personGivenName = signInAccount.getGivenName();
            personFamilyName = signInAccount.getFamilyName();
            personEmail = signInAccount.getEmail();
            personId = signInAccount.getId();
            personPhoto = signInAccount.getPhotoUrl();
            Toast.makeText(this, personName + " signed in", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();

        tvName.setText(personName);
        tvGivenName.setText(personGivenName);
        tvFamilyName.setText(personFamilyName);
        tvEmail.setText(personEmail);
        tvID.setText(personId);

        Log.d(TAG, "updateUI: photoUri: " + personPhoto.toString());

        Picasso.get()
                .load(personPhoto.toString())
                .resize(100, 100)
                .into(ivPhoto);

    }




}
