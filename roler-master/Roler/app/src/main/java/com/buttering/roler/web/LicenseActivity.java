package com.buttering.roler.web;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.buttering.roler.R;

/**
 * Created by kinamare on 2017-06-02.
 */

public class LicenseActivity extends LocalWebActivity {

	@Override
	protected String getFileName() {
		return getString(R.string.html_license);
	}

	@Override
	protected void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		imageView.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
		imageView.setOnClickListener(view -> {
			finish();
		});
		textView.setTextColor(Color.BLACK);
		textView.setText("오픈소스 라이센스");
		setSupportActionBar(toolbar);

	}
}
