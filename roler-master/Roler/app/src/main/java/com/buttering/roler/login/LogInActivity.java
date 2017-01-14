package com.buttering.roler.login;


import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.plan.PlanActivity;
import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.User;
import com.buttering.roler.signup.SignUpActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import rx.Subscriber;

/**
 * Created by nanamare on 2016-07-30.
 */
public class LogInActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ILoginView {

	public final static String EXTRA_MESSAGE = "com.buttering.roler";
	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final int GET_ACCOUNT = 111;

	private static final String TAG = "Login_Activity";
	private static String OAUTH_CLIENT_ID = "nfRec7uCc36x_KoxxTzC";
	private static String OAUTH_CLIENT_SECRET = "dPDGbaB_3V";
	private static String OAUTH_CLIENT_NAME = "Roler";
	private OAuthLogin mOAuthLoginModule;
	private ILoginPresenter loginPresenter;
	private ACProgressFlower dialog;

	@BindView(R.id.activity_login_google_btn)
	ImageButton login_google_btn;

	@BindView(R.id.activity_login_signIn_btn)
	Button login_signIn_btn;

	@BindView(R.id.activity_login_signUp_btn)
	Button login_signUp_btn;

	@BindView(R.id.email_et)
	TextView email_et;

	@BindView(R.id.pw_et)
	TextView pw_et;

	private GoogleApiClient mGoogleApiClient;

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case GET_ACCOUNT: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//reload my activity with permission granted or use the features what required the permission
				} else {
					Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
			}
		}


	}


	private void checkThePemission() {
		if (Build.VERSION.SDK_INT > 22) {
			boolean hasPermission = (ContextCompat.checkSelfPermission(this,
					Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED);
			if (!hasPermission) {
				ActivityCompat.requestPermissions(this,
						new String[]{
								android.Manifest.permission.GET_ACCOUNTS}, GET_ACCOUNT);
			}
		}
	}


	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i("df", "This device is not supported.");
			}
			return false;
		}
		return true;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ButterKnife.bind(this);

		loginPresenter = new LoginPresenter(this);

		initLoginSetting();
		initFbService();

	}

	private void initFbService() {
		if (checkPlayServices()) {

			new AsyncTask() {

				@Override
				protected Object doInBackground(Object[] objects) {
					//파이어 베이스 이름을 지우고 토큰을 다시 받는다
//					try {
//						FirebaseInstanceId.getInstance().deleteInstanceId();
//						FirebaseInstanceId.getInstance().getToken();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}

					return null;
				}
			}.execute(null, null, null);

		}
	}


	private void initLoginSetting() {

		mOAuthLoginModule = OAuthLogin.getInstance();
		mOAuthLoginModule.init(
				getApplicationContext()
				, OAUTH_CLIENT_ID
				, OAUTH_CLIENT_SECRET
				, OAUTH_CLIENT_NAME
		);


		OAuthLoginButton mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
		mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

		login_signUp_btn.setOnClickListener(v -> {
			Intent intent = new Intent(this, SignUpActivity.class);
			startActivity(intent);
		});

		login_signIn_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = email_et.getText().toString();
				String pwd = pw_et.getText().toString();
				if (isValid(email, pwd)) {
					loginPresenter.signIn(email, pwd)
							.subscribe(new Subscriber<String>() {
								@Override
								public void onCompleted() {
									hideLoadingBar();
									Toast.makeText(LogInActivity.this, "로그인 완료 환영합니다", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
									startActivity(intent);
									finish();
								}

								@Override
								public void onError(Throwable e) {
									hideLoadingBar();
									Toast.makeText(LogInActivity.this, "Invalid id or password", Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(String s) {

								}
							});
				}

			}
		});

	}

	private boolean isValid(String email, String passwd) {

		if (email == null || email.isEmpty()) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_empty_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (email.length() > 60) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_too_long_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!isEmailValid(email)) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_invalid_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}


		if (passwd == null || passwd.isEmpty()) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_empty_pwd),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (passwd.length() < 8) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_too_short_pwd),
					Toast.LENGTH_SHORT).show();
			return false;
		}


		return true;
	}


	private boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}


	private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
		@Override
		public void run(boolean success) {
			if (success) {
				String accessToken = mOAuthLoginModule.getAccessToken(getApplicationContext());
				String refreshToken = mOAuthLoginModule.getRefreshToken(getApplicationContext());
				long expiresAt = mOAuthLoginModule.getExpiresAt(getApplicationContext());
				String tokenType = mOAuthLoginModule.getTokenType(getApplicationContext());
				Toast.makeText(getApplicationContext(), accessToken + refreshToken + expiresAt + tokenType, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
				startActivity(intent);
				finish();

			} else {
				String errorCode = mOAuthLoginModule.getLastErrorCode(getApplicationContext()).getCode();
				String errorDesc = mOAuthLoginModule.getLastErrorDesc(getApplicationContext());
				Toast.makeText(getApplicationContext(), "errorCode:" + errorCode
						+ ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
			}
		}

		;
	};


	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.activity_login_google_btn:
				checkThePemission();
				mGoogleApiClient = new GoogleApiClient.Builder(this)
						.addConnectionCallbacks(this)
						.addOnConnectionFailedListener(this)
						.addApi(Plus.API)
						.addScope(Plus.SCOPE_PLUS_PROFILE)
						.build();

				mGoogleApiClient.connect();
				showLoadingBar();
				break;
		}

	}


	public void onConnected(Bundle bundle) {
		Log.d(TAG, "구글 플레이 연결이 되었습니다.");

		if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {

			Log.d(TAG, "onConnected 연결 실패");

		} else {
			Log.d(TAG, "onConnected 연결 성공");

			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//				 TODO: Consider calling
//				    ActivityCompat#requestPermissions
//				 here to request the missing permissions, and then overriding
//				   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//				                                          int[] grantResults)
//				 to handle the case where the user grants the permission. See the documentation
//				 for ActivityCompat#requestPermissions for more details.
				return;
			}
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

			if (currentPerson.hasImage()) {

				Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

               /* Glide.with(MainActivity.this)
                        .load(currentPerson.getImage().getUrl())
                        .into(userphoto);*/

			}
			if (currentPerson.hasDisplayName()) {
				Log.d(TAG, "google+ name  : " + currentPerson.getDisplayName());
				Log.d(TAG, "google+ id : " + currentPerson.getId());
				String message = currentPerson.getDisplayName();
				Log.d("현재 사용자님은", message + "입니다");
				//구글 유저 저장
				User user = new User();
				user.setName(currentPerson.getDisplayName());
				user.setPicture_url(currentPerson.getImage().getUrl());
				user.setId(currentPerson.getId());
				user.setEmail(email);
				MyInfoDAO.getInstance().saveUserInfo(user);
				hideLoadingBar();
				Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
				finish();
			}

		}
	}


	public void onConnectionSuspended(int i) {

	}


	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "연결 에러 " + connectionResult);

		if (connectionResult.hasResolution()) {

			Log.e(TAG,
					String.format(
							"Connection to Play Services Failed, error: %d, reason: %s",
							connectionResult.getErrorCode(),
							connectionResult.toString()));
			try {
				//이게 핵심?
				connectionResult.startResolutionForResult(this, 0);
			} catch (IntentSender.SendIntentException e) {
				Log.e(TAG, e.toString(), e);
			}
		} else {
			Toast.makeText(getApplicationContext(), "이미 로그인 중", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void showLoadingBar() {
		dialog = new ACProgressFlower.Builder(this)
				.direction(ACProgressConstant.DIRECT_CLOCKWISE)
				.themeColor(Color.WHITE)
				.fadeColor(Color.DKGRAY).build();
		dialog.show();
	}

	@Override
	public void hideLoadingBar() {
		if (dialog != null)
			dialog.dismiss();
	}

}
