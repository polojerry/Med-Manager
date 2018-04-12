package com.example.canister.med_manager.userdata;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.canister.med_manager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileNameActivity extends AppCompatActivity {

    EditText mUserUpdateName;
    ImageView mUserUpdateNameIcon;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_name);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUserUpdateName = (EditText) findViewById(R.id.edit_profile_name);
        mUserUpdateNameIcon = (ImageView) findViewById(R.id.image_profile_name_edit);

        Intent getCurrentUserName = getIntent();
        Bundle previousUsername = getCurrentUserName.getExtras();

        final String currentName = previousUsername.get("CurrentName").toString();
        mUserUpdateName.setText(currentName);

        mUserUpdateNameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateName(currentName);
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

    public void updateName(String currentName){
        String changeName = mUserUpdateName.getText().toString();
        if (changeName.equals(currentName)){
            Toast.makeText(this, "No Change Needed", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseUser mUser = mAuth.getCurrentUser();
            String setUserName  = mUserUpdateName.getText().toString();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(setUserName)
                    .build();

            mUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileNameActivity.this, "User profile updated.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EditProfileNameActivity.this, "Error in updating user name.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            finish();
        }

    }
}
