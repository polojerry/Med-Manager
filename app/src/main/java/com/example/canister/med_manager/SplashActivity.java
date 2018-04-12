package com.example.canister.med_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView mSplashImageView = findViewById(R.id.image_splash);
        final Animation mAnimation  = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);

        mAnimation.setDuration(2000);
        mSplashImageView.startAnimation(mAnimation);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent startSignInIntent = new Intent(SplashActivity.this,SignInActivity.class);
                startActivity(startSignInIntent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
