package com.buttering.roler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nanamare on 2016-07-30.
 */
public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private boolean isFirst;
    private boolean isUserExists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //splash 들어갈 위치
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToLoginActivity();
            }
        },1200);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
