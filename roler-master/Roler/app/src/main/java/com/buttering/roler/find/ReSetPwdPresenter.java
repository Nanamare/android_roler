package com.buttering.roler.find;

import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.UserService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-03-27.
 */

public class ReSetPwdPresenter extends BasePresenter implements IReSetPwdPresenter {

	private UserService userService;
	private IReSetUpPwdView view;


	public ReSetPwdPresenter(IReSetUpPwdView view){
		userService = new UserService();
		this.view = view;
	}


	@Override
	public void changePwd(String userPwd, String email) {
		userService.changePwd(userPwd, email)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Void aVoid) {
						view.moveToLoginActivity();
					}
				});
	}
}
