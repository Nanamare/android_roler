package com.buttering.roler.login;

import android.app.Activity;
import android.content.pm.ProviderInfo;

import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.UserService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class LoginPresenter extends BasePresenter implements ILoginPresenter {

	private UserService userService;
	private ILoginView view;

	public LoginPresenter(ILoginView view){
		this.view = view;
		this.userService = new UserService();

	}


	@Override
	public Observable<String> signIn(String email, String pwd) {
		view.showLoadingBar();

		return userService
				.signIn(email,pwd)
				.observeOn(AndroidSchedulers.mainThread());
	}
}
