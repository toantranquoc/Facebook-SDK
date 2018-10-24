package com.wordpress.toanhtc.sdktrain;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProfilePictureView avatar;
    TextView name, birthday, gender, email, friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXA();
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email","user_birthday","user_gender","user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("JSON", response.getJSONObject().toString());
                        try {
                            avatar.setProfileId(Profile.getCurrentProfile().getId());
                            name.setText(object.getString("name"));;
                            email.setText(object.getString("email"));
                            gender.setText(object.getString("gender"));
                            birthday.setText(object.getString("birthday"));
                            friend.setText("Friends: " + object.getJSONObject("friends").getJSONObject("summary").get("total_count"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email,gender,birthday,friends");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    private void AnhXA() {
        avatar = (ProfilePictureView) findViewById(R.id.imageAvatar);
        name = (TextView) findViewById(R.id.txtName);
        birthday = (TextView) findViewById(R.id.txtBirth);
        gender = (TextView) findViewById(R.id.txtGender);
        email = (TextView) findViewById(R.id.txtEmail);
        friend = (TextView) findViewById(R.id.txtFriend);
        loginButton = (LoginButton) findViewById(R.id.login_button);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
