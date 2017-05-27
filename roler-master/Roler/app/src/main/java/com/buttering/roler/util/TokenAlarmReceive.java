package com.buttering.roler.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.net.baseservice.UserService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-05-27.
 */

public class TokenAlarmReceive extends BroadcastReceiver {

	public static final String TOKEN_REFRESH = "com.buttering.roler.TOKEN_REFRESH";

	UserService userService = new UserService();

	@Override
	public void onReceive(Context context, Intent intent) {

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

					}
				});
	}

}
