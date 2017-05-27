package com.buttering.roler.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.net.baseservice.UserService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-05-27.
 */

public class TokenRefreshService extends Service {

	UserService userService = new UserService();

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Token refresh", Toast.LENGTH_SHORT).show();

		userService.refreshToken(MyInfoDAO.getInstance().getUserId()
				, MyInfoDAO.getInstance().getEmail())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Void aVoid) {
						aVoid.toString();
					}
				});
		return START_NOT_STICKY;
	}

}
