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
	public void changePwd(String userPwd) {
		userService.changePwd(userPwd)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {
						view.moveToLoginActivity();
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Void aVoid) {

					}
				});
	}
}
