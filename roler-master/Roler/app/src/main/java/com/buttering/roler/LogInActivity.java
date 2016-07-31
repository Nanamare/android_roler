package com.buttering.roler;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.ArrayList;

/**
 * Created by nanamare on 2016-07-30.
 */
public class LogInActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "Login_Activity";
    private GoogleApiClient mGoogleApiClient;
    public final static String EXTRA_MESSAGE = "com.buttering.roler";
    Handler updateHandler = new Handler();
    private static OAuthLogin mOAuthLoginModule;

    private static String OAUTH_CLIENT_ID = "nfRec7uCc36x_KoxxTzC";  // 1)에서 받아온 값들을 넣어좁니다
    private static String OAUTH_CLIENT_SECRET = "dPDGbaB_3V";
    private static String OAUTH_CLIENT_NAME = "Roler";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                getApplicationContext()
                ,OAUTH_CLIENT_ID
                ,OAUTH_CLIENT_SECRET
                ,OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );

        initSetting();



        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(LogInActivity.this, "Permission 받아져 있습니다.", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(LogInActivity.this, "Permission 거부되었습니다.\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("카메라,사진 앨범에 대한 퍼미션이 필요합니다")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.CAMERA)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


        Button btn = (Button) findViewById(R.id.login_button);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 회원가입 테스트를 위해 액티비티 이동.
                    Intent intentSubActivity =
                            new Intent(LogInActivity.this, SignUpActivity.class);
                    startActivity(intentSubActivity);

                    // 서버에 id, pw 맞는지 확인 요청

                    // 맞으면 로그인 성공, PlanActivity
                    // 틀리면 로그인 실패, 실패메시지
                }
            });
        }

    }

    private void initSetting() {

        final OAuthLoginButton mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }


    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(getApplicationContext());
                String refreshToken = mOAuthLoginModule.getRefreshToken(getApplicationContext());
                long expiresAt = mOAuthLoginModule.getExpiresAt(getApplicationContext());
                String tokenType = mOAuthLoginModule.getTokenType(getApplicationContext());
                Toast.makeText(getApplicationContext(),accessToken+refreshToken+expiresAt+tokenType, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
                startActivity(intent);

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(getApplicationContext()).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(getApplicationContext());
                Toast.makeText(getApplicationContext(), "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };

//    mOAuthLoginModule.startOauthLoginActivity(LogInActivity.this, mOAuthLoginHandler);

    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.google_btn:
                Toast.makeText(this, "접속합니다", Toast.LENGTH_SHORT).show();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .build();

                mGoogleApiClient.connect();

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

            if (currentPerson.hasImage()) {

                Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

               /* Glide.with(MainActivity.this)
                        .load(currentPerson.getImage().getUrl())
                        .into(userphoto);*/

            }
            if (currentPerson.hasDisplayName()) {
                Log.d(TAG, "google+ name  : " + currentPerson.getDisplayName());
                Log.d(TAG, "google+ id : " + currentPerson.getId());
                //userName.setText(currentPerson.getDisplayName()+"님 안녕 하세요");
                //userName.setText(currentPerson.getDisplayName() + "님 로그인 완료 되어 있습니다.");
                String message = currentPerson.getDisplayName().toString();
                Log.d("현재 사용자님은",message +"입니다");
                Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
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





}
