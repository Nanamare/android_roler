package com.buttering.roler.depth;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.buttering.roler.login.LogInActivity;

/**
 * Created by kinamare on 2017-05-29.
 * 액티비티 생명주기, 스택관리, 공통된 처리를 위한 액티비티
 */
public class DepthBaseActivity extends AppCompatActivity implements IDepthView{

	private DepthPresenter presenter;

	@Override
	protected void onRestart(){
		super.onRestart();

		presenter = new DepthPresenter(this);
		presenter.isCheckTokenExpired();
	}

	@Override
	public void goToLoginActivity() {
		Intent intent = new Intent(this, LogInActivity.class);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		else {
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		startActivity(intent);
	}


	@Override
	protected void onResume(){
		super.onResume();

	}

}
