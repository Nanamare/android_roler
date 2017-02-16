package com.buttering.roler.login;

import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.UserService;

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

	public LoginPresenter(){
		this.userService = new UserService();

	}


	@Override
	public Observable<String> signIn(String email, String pwd) {
		view.showLoadingBar();

		return userService
				.signIn(email,pwd)
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Observable<Void> registerToken(String token, String email) {

		return userService
				.registerToken(token, email)
				.observeOn(AndroidSchedulers.mainThread());
	}
}
