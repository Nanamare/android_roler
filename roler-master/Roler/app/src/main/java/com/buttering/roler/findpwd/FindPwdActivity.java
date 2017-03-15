package com.buttering.roler.findpwd;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.buttering.roler.R;

public class FindPwdActivity extends AppCompatActivity {

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
}
