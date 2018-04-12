package com.example.canister.med_manager.userdata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.canister.med_manager.R;

import com.example.canister.med_manager.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity {

    //Used to request image from system for update
    static final int REQUEST_IMAGE_GET = 1;

    private FirebaseAuth mAuth;

    TextView mUserName;
    TextView mUserEmail;
    ImageView mUserPhoto;

    ImageView mImageEditName;
    ImageView mImageEditEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUserName = (TextView) findViewById(R.id.text_user_name);
        mUserEmail = (TextView) findViewById(R.id.text_user_email);
        mUserPhoto = (ImageView) findViewById(R.id.image_profile);

        mImageEditName = (ImageView) findViewById(R.id.icon_edit_profile_name);
        mImageEditEmail = (ImageView) findViewById(R.id.icon_edit_profile_email);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();

        final String userName = mUser.getDisplayName();
        String userEmail  = mUser.getEmail();
        String userPhotoUrl = mUser.getPhotoUrl().toString();

        mUserName.setText(userName);
        mUserEmail.setText(userEmail);

        Picasso.with(this)
                .load(userPhotoUrl)
                .into(mUserPhoto);

        mUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);


                }

            }



        });

        mImageEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentName = mUserName.getText().toString();
                Intent updateName = new Intent(ProfileActivity.this, EditProfileNameActivity.class);
                updateName.putExtra("CurrentName", currentName);
                startActivity(updateName);
            }
        });

        mImageEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentEmail = mUserEmail.getText().toString();
                Intent updateEmail = new Intent(ProfileActivity.this, EditEmailActivity.class);
                updateEmail.putExtra("CurrentEmail", currentEmail);
                startActivity(updateEmail);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }



}
