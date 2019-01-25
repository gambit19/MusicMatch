package com.example.winx10.musicmatch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private com.google.firebase.auth.FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String TAG = "FACELOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView linkText = findViewById(R.id.termsLink);
        linkText.setClickable(true);
        linkText.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "By signing in, you agree to our" +
                "<a href='https://termsfeed.com/privacy-policy/3a17ec4036fbb6087065e331f8e3490a'> terms and conditions</a>.";
        linkText.setText(Html.fromHtml(text));

        // Initialize Firebase Auth
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                android.util.Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                android.util.Log.d(TAG, "facebook:onCancel");
                Toast.makeText(MainActivity.this,"You cancelled",Toast.LENGTH_LONG).show();       // ...
            }

            @Override
            public void onError(FacebookException error) {
                android.util.Log.d(TAG, "facebook:onError", error);
                Toast.makeText(MainActivity.this,"Login Error",Toast.LENGTH_LONG).show();
                // ...
            }
        });


/*        LocationHelper helper = new LocationHelper(this, new LocationHelper.LocationHelperCallback() {
            @Override
            public void onLocationResult(Location location) {
                Log.d("Location:", location.getLatitude()+"");
                Toast.makeText(MainActivity.this, location.getLatitude() + "," + location.getLongitude(),Toast.LENGTH_SHORT).show();
            }
        });

        helper.getLocation();*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        com.google.firebase.auth.FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!= null){
            updateUI();

        }

    }

    private void updateUI(){
        Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_LONG).show();

        Intent accountIntent = new Intent(MainActivity.this,Home_start.class);
        startActivity(accountIntent);
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUser();
                            Toast.makeText(MainActivity.this, Profile.getCurrentProfile().getFirstName(),Toast.LENGTH_LONG).show();
                            updateUI();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void getUser(){

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // Name, email address, and profile photo Url
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            //Uri photoUrl = user.getPhotoUrl();

                            // Check if user's email is verified
                            //boolean emailVerified = user.isEmailVerified();

                            // The user's ID, unique to the Firebase project. Do NOT use this value to
                            // authenticate with your backend server, if you have one. Use
                            // FirebaseUser.getIdToken() instead.
                            String uid = user.getUid();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
