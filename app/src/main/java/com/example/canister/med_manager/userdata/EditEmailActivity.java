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

public class EditEmailActivity extends AppCompatActivity {

    EditText mUserUpdateEmail;
    ImageView mUserUpdateEmailIcon;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUserUpdateEmail = (EditText) findViewById(R.id.edit_profile_name);
        mUserUpdateEmailIcon = (ImageView) findViewById(R.id.image_profile_name_edit);

        Intent getCurrentUserName = getIntent();
        Bundle previousUsername = getCurrentUserName.getExtras();

        final String currentEmail = previousUsername.get("CurrentEmail").toString();
        mUserUpdateEmail.setText(currentEmail);

        mUserUpdateEmailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmail(currentEmail);
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

    public void updateEmail(String currentEmail){
        String changeEmail = mUserUpdateEmail.getText().toString();
        if (changeEmail.equals(currentEmail)){
            Toast.makeText(this, "No Change Done", Toast.LENGTH_SHORT).show();
        }else{
            String changedEmail = mUserUpdateEmail.getText().toString();
            FirebaseUser mUser = mAuth.getCurrentUser();
            mUser.updateEmail(changedEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditEmailActivity.this, "User email address updated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            finish();
        }

    }
}
