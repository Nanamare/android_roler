package com.buttering.roler.setting;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.BuildConfig;
import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.login.LogInActivity;
import com.buttering.roler.util.SharePrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingActivity extends AppCompatActivity {

	@BindView(R.id.activity_setting_version_ll)
	LinearLayout ll_version;
	@BindView(R.id.activity_setting_tutorial_ll)
	LinearLayout ll_tutorial;
	@BindView(R.id.activity_setting_inviteFriend_ll)
	LinearLayout ll_inviteFriend;
	@BindView(R.id.activity_setting_chPwd_ll)
	LinearLayout ll_chPwd;
	@BindView(R.id.activity_setting_noti_ll)
	LinearLayout ll_noti;
	@BindView(R.id.activity_setting_policy_ll)
	LinearLayout ll_policy;
	@BindView(R.id.activity_setting_terms_ll)
	LinearLayout ll_terms;
	@BindView(R.id.activity_setting_logout)
	LinearLayout ll_logout;


	@BindView(R.id.activity_setting_version_tv)
	TextView tv_version;

	private static int single_top_activity = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.bind(this);

		setToolbar();
		setAppVersion();
		moveToTutorialActivity();
		inviteFriend();
		moveToChPwdAcitivity();
		logout();
		setTermsPolicy();

	}

	private void setTermsPolicy() {
		ll_terms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent pdfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.liveo.me/eula/terms_of_service.pdf"));
				startActivity(pdfIntent);
			}
		});
		ll_policy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent pdfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.liveo.me/eula/privacy_policy.pdf"));
				startActivity(pdfIntent);
			}
		});

	}

	private void logout() {
		ll_logout.setOnClickListener(view -> {
			MyInfoDAO.getInstance().deleteAccountInfo();
			SharePrefUtil.putSharedPreference("isLoggedIn",false);
			Intent loginIntent = new Intent(this, LogInActivity.class);
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(loginIntent);
			startActivityForResult(loginIntent,single_top_activity);
			finish();
		});

	}

	private void moveToChPwdAcitivity() {
		ll_chPwd.setOnClickListener(view -> {
//			Intent ResetIntent = new Intent(this, ResetPwdActivity.class);
//			startActivity(ResetIntent);
			Toast.makeText(this, "프로필 리셋 액티비티 이동", Toast.LENGTH_SHORT).show();
		});
	}

	private void inviteFriend() {
		ll_inviteFriend.setOnClickListener(view -> {
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = MyInfoDAO.getInstance().getNickName()
					+ " " + getString(R.string.invite);
			sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
			startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), 1000);
		});

	}

	private void moveToTutorialActivity() {
		ll_tutorial.setOnClickListener(view -> {
//			Intent intent = new Intent(this, TutorialActivity.class);
//			startActivity(intent);
			Toast.makeText(this, "튜토리얼", Toast.LENGTH_SHORT).show();
		});
	}

	private void setAppVersion() {
		tv_version.setText(BuildConfig.VERSION_CODE + "");
	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		imageView.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
		imageView.setOnClickListener(view -> {
			finish();
		});
		textView.setTextColor(Color.BLACK);
		textView.setText("    Options");
		setSupportActionBar(toolbar);

	}


}

