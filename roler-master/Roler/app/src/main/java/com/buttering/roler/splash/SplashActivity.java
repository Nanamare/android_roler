package com.buttering.roler.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.buttering.roler.plan.PlanActivity;
import com.buttering.roler.login.LogInActivity;
import com.buttering.roler.R;
import com.buttering.roler.util.NetUtil;

/**
 * Created by nanamare on 2016-07-30.
 */
public class SplashActivity extends Activity {

	private static final String TAG = SplashActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if(NetUtil.isNetworkAvailable(getApplicationContext())) {
			Intent intent = new Intent(this, LogInActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			boolean isLoggedIn = SharePrefUtil.getBooleanSharedPreference("isLoggedIn");
			boolean isLoggedIn = false;
			if (isLoggedIn) {

				new Handler().postDelayed(() -> {
					Intent loggedInIntent = new Intent(this, PlanActivity.class);
					loggedInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(loggedInIntent);
					finish();
				}, 1000);


			} else {

				new Handler().postDelayed(() -> {
					startActivity(intent);
					finish();
				}, 1000);
			}


		} else {
			Toast.makeText(getApplicationContext(),"인터넷 연결을 확인해보세요", Toast.LENGTH_LONG).show();
			super.onPause();
		}


//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				goToLoginActivity();
//			}
//		}, 1200);
	}

//	private void goToLoginActivity() {
//		Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);
//	}


}
