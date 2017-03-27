package com.buttering.roler.find;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class ExFindPwdActivity extends AppCompatActivity implements IExFindPwdView {

	@BindView(R.id.activity_ex_find_pwd_email_btn)
	Button email_btn;
	@BindView(R.id.activity_ex_find_pwd_name_edt)
	TextView name_edt;
	@BindView(R.id.activity_ex_find_pwd_email_edt)
	TextView email_edt;
	@BindView(R.id.activity_ex_find_pwd_authorization_edt)
	TextView authorization_edt;

	private ExFindPwdPresenter presenter;
	private String userName;
	private String userEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ex_find_pwd);

		presenter = new ExFindPwdPresenter(this);

		findPwdFromEmail();

		setToolbar();

	}

	private void findPwdFromEmail() {
		email_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userEmail = email_edt.getText().toString();
				userName = name_edt.getText().toString();
				if (inValid(userName, userEmail)) {
					presenter.checkPwd(userName, userEmail);
				}
			}
		});

	}

	private boolean inValid(String name, String email) {

		if (!isEmailValid(email)) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_invalid_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!isNameValid(name)) {
			Toast.makeText(getApplicationContext(), "이름을 제대로 입력해주세요",
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

	private boolean isNameValid(String userName) {

		if (TextUtils.isEmpty(userName)) {
			Toast.makeText(getApplicationContext(), "please enter user name",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (userName.matches("[^가-힣A-Za-z ]")) {
			Toast.makeText(getApplicationContext(), "User name can't have number including special characters",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		textView.setTextColor(Color.BLACK);
		textView.setText("비밀번호 찾기");
		setSupportActionBar(toolbar);
	}


	@Override
	public void visibleAuthorizationTextView(String autorizationCode) {

		authorization_edt.setVisibility(View.VISIBLE);
		email_btn.setVisibility(View.GONE);
		name_edt.setVisibility(View.GONE);

		if(authorization_edt.getText().equals(autorizationCode)){
			Intent moveToSetUpPwdActivity = new Intent(this, IReSetPwdActivity.class);
			startActivity(moveToSetUpPwdActivity);
		}

	}
}
