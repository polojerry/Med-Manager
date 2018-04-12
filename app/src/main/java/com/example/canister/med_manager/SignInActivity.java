package com.example.canister.med_manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.canister.med_manager.userdata.UserData;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    public List<UserData> mUserDatas;

    /*Creating Views */
    private Button mLogInButton;
    private EditText mUserEmail;
    private EditText mUserPassword;
    private Button mSignUp;

    /*Boolean for checking if Edit Text for Email and Password are Empty*/
    Boolean mCheckEditText;

    /*Creating SignInButton and GoogleApiClient*/
    private SignInButton mGoogleSignInButton;
    private GoogleApiClient mGoogleApiClient;

    /*Show progress dialog when registering user*/
    ProgressDialog mProgressDialog;

    /*Firebase Authentication*/
    private FirebaseAuth mFirebaseAuth;


    /*Declaring Sign In Constants*/
    public static final int RC_SIGN_IN = 9001;
    public static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        /*Creating a Google SignIn Object specifying SignIn Scope*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        /*Creating a google api Client*/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        /*Referencing the Google Sign In Button and onClick Listener*/
        mGoogleSignInButton= (SignInButton) findViewById(R.id.button_googleSignIn);
        mGoogleSignInButton.setOnClickListener(this);

        /*Referencing the Google Sign Up Button and onClick Listener*/
        mSignUp = (Button) findViewById(R.id.button_SignUpLoginScreen);
        mSignUp.setOnClickListener(this);

        /*Referencing the Log In Button onClick Listener*/
        mLogInButton = (Button) findViewById(R.id.button_Login);
        mLogInButton.setOnClickListener(this);

        /*Referencing the Email and Password EditText*/
        mUserEmail = (EditText) findViewById(R.id.edit_emailLogin);
        mUserPassword = (EditText) findViewById(R.id.edit_passwordLogin);

        /*Progress Dialog to show while signing up*/
        mProgressDialog = new ProgressDialog(this);

        /*Initializing Firebase Authentication*/
        mFirebaseAuth = FirebaseAuth.getInstance();



    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_googleSignIn:
                signIn();
                break;
            case R.id.button_SignUpLoginScreen:
                Intent mSignUpIntent = new Intent(this,SignUpActivity.class);
                startActivity(mSignUpIntent);
                break;
            case R.id.button_Login:
                logIn();
                break;
        }
    }

    /* Log In with Email and Password*/
    private void logIn() {
        /*Get the Email amd Password*/
        String email = mUserEmail.getText().toString().trim();
        String password = mUserPassword.getText().toString().trim();

        /*Checks if Edit Text for Email and Password are Empty*/
        CheckEditTextIsEmptyOrNot();

        if (!mCheckEditText){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            /*Stops method from executing further*/
            return;
        }

        /*Display Progress Dialog*/
        mProgressDialog.setMessage("Logging In...");
        mProgressDialog.show();

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Logged In Succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignInActivity.this, "Could not Log In, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mProgressDialog.dismiss();
    }

    public void CheckEditTextIsEmptyOrNot() {
        /*Get the Email amd Password*/
        String mEmail = mUserEmail.getText().toString().trim();
        String mPassword = mUserPassword.getText().toString().trim();

        // Checking whether EditText value is empty or not.
        if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword)) {

            // If any of EditText is empty then set variable value as False.
            mCheckEditText = false;

        } else {

            // If any of EditText is filled then set variable value as True.
            mCheckEditText = true;
        }
    }

    /* Creating a Google Sign In intent that starts the Sign In Activity for us*/
    private void signIn() {
        Intent googleSignInIntent =Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent,RC_SIGN_IN);
    }

    /*Getting Google Sign In Result*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult.isSuccess()){
                GoogleSignInAccount signInAccount = signInResult.getSignInAccount();
                firebaseAuthWithGoogle(signInAccount);
                mProgressDialog.hide();

                Intent openDrawerActivity  = new Intent(this, DisplayMedicineActivity.class);
                startActivity(openDrawerActivity);

                Toast.makeText(SignInActivity.this, "Signed in as "+ signInAccount.getDisplayName(),Toast.LENGTH_LONG).show();

            }
            else{
                mProgressDialog.hide();
            }
        }else{
            mProgressDialog.hide();
        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount signInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            UserData  userData = new UserData(
                                    signInAccount.getDisplayName(),
                                    signInAccount.getEmail(),
                                    signInAccount.getPhotoUrl().toString()
                            );
                            mUserDatas.add(userData);

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference userReference = firebaseDatabase.getReference("users");
                            userReference.child(signInAccount.getEmail().replace(",", ", "))
                                    .setValue(userData, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            finish();
                                        }
                                    });
                        }else{
                            mProgressDialog.hide();
                            Toast.makeText(SignInActivity.this, "Authentication Failed",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed"+ connectionResult);
    }
}
