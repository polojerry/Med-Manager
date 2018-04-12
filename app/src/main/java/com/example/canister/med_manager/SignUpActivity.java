package com.example.canister.med_manager;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    /*Declaring the views*/
    private Button mSignUp;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    /*Boolean for checking if Edit Text for Email and Password are Empty*/
    Boolean mCheckEditText;

    /*Boolean for checking if the Sign Up Passwords Match*/
    Boolean mPasswordMatch;

    /*Show progress dialog when registering user*/
    ProgressDialog mProgressDialog;

    /*Firebase Authentication*/
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*Initializing our Views*/
        mSignUp = (Button) findViewById(R.id.button_signUp);
        mEmail = (EditText) findViewById(R.id.edit_signUpEmail);
        mPassword =(EditText) findViewById(R.id.edit_signUpPassword);
        mConfirmPassword = (EditText) findViewById(R.id.edit_signUpConfirmPassword);

        /*Attaching OnClickListener to our view*/
        mSignUp.setOnClickListener(this);

        /*Progress Dialog to show while signing up*/
        mProgressDialog = new ProgressDialog(this);

        /*Initializing Firebase Authentication*/
        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_signUp:
                signUp();
                break;
        }
    }

    private void signUp() {
        /*Get the Email amd Password*/
        String mUserEmail = mEmail.getText().toString().trim();
        String mUserPassword = mPassword.getText().toString().trim();
        String mUserConfirmPassword = mConfirmPassword.getText().toString().trim();

        /*Checks if Edit Text for Email and Password are Empty*/
        CheckEditTextIsEmptyOrNot();

        /*Checks if the sign up emails match*/
        CheckPasswordMatch();

        if (!mCheckEditText){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            /*Stops method from executing further*/
            return;
        }
        if (mPasswordMatch = false){
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            /*Stops method from executing further*/
            return;
        }
        /*Display Progress Dialog*/
        mProgressDialog.setMessage("Signing Up...");
        mProgressDialog.show();

        /*Sign Up the user*/
        mFirebaseAuth.createUserWithEmailAndPassword(mUserEmail, mUserPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Signed Up Succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Could not Sign Up, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mProgressDialog.dismiss();

    }

    private void CheckPasswordMatch() {
        /*Get the Passwords*/
        String mUserPassword = mPassword.getText().toString().trim();
        String mUserConfirmPassword = mConfirmPassword.getText().toString().trim();

        if (mUserPassword != mUserConfirmPassword){
            mPasswordMatch = false;
        }else{
            mPasswordMatch = true;
        }
    }

    public void CheckEditTextIsEmptyOrNot() {
        /*Get the Email amd Password*/
        String mUserEmail = mEmail.getText().toString().trim();
        String mUserPassword = mPassword.getText().toString().trim();
        String mUserConfirmPassword = mConfirmPassword.getText().toString().trim();

        // Checking whether EditText value is empty or not.
        if (TextUtils.isEmpty(mUserEmail) || TextUtils.isEmpty(mUserPassword) || TextUtils.isEmpty(mUserConfirmPassword)) {

            // If any of EditText is empty then set variable value as False.
            mCheckEditText = false;

        } else {

            // If any of EditText is filled then set variable value as True.
            mCheckEditText = true;
        }
    }
}
