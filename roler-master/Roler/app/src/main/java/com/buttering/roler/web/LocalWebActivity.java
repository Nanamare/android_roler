package com.buttering.roler.web;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.buttering.roler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class LocalWebActivity extends AppCompatActivity {

	private static final String TAG = LocalWebActivity.class.getSimpleName();

	@BindView(R.id.wvContents) WebView wvContents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_web);
		ButterKnife.bind(this);

		setToolbar();

		Log.d(TAG, "onCreate: " + wvContents);
		//
		WebSettings ws = wvContents.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setUseWideViewPort(true);
		ws.setLoadWithOverviewMode(true);
		ws.setPluginState(WebSettings.PluginState.ON);
		ws.setSupportMultipleWindows(false);
		ws.setDomStorageEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(false);
		ws.setMediaPlaybackRequiresUserGesture(false);

		wvContents.loadUrl("file:///android_asset/" + getFileName());
	}

	abstract protected String getFileName();

	abstract protected void setToolbar();

}
