package com.buttering.roler.setting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.buttering.roler.R;
import com.buttering.roler.depth.DepthBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends DepthBaseActivity {

	@BindView(R.id.activity_profile_bong_tv) AppCompatTextView bongTv;
	@BindView(R.id.activity_profile_hs_blog_tv) AppCompatTextView hsBlogTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);

	}

	@OnClick(R.id.activity_profile_bong_tv)
	public void bongOnClick(){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vocketlist_url)));
		startActivity(intent);
	}

	@OnClick(R.id.activity_profile_hs_blog_tv)
	public void hsBlogOnClick(){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.hs_blog_url)));
		startActivity(intent);

	}

}
