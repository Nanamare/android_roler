package com.buttering.roler.find;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.R;
import com.buttering.roler.login.LogInActivity;

public class FindPwdActivity extends AppCompatActivity implements  IFindPwdView{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pwd);
		setToolbar();
	}

	private void setToolbar() {

		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		textView.setTextColor(Color.BLACK);
		textView.setText("비밀 번호 찾기");
		setSupportActionBar(toolbar);

	}

	@Override
	public void moveToLoginActivity() {
		Toast.makeText(this, "비밀번호 변경완료 "+"\n"+"다시 로그인 해주세요", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, LogInActivity.class);
		startActivity(intent);
	}
}
