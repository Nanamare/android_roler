package com.buttering.roler.login;

import android.app.Activity;

import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.UserService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class LoginPresenter extends BasePresenter implements ILoginPresenter {

	private UserService userService;
	private Activity activity;

	public LoginPresenter(Activity activity){
		this.activity = activity;
		this.userService = new UserService();

	}


	@Override
	public Observable<String> signIn(String email, String pwd) {

		return userService
				.signIn(email,pwd)
				.observeOn(AndroidSchedulers.mainThread());
	}
}
