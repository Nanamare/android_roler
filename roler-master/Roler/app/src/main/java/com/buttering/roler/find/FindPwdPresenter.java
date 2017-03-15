package com.buttering.roler.find;

import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.UserService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-03-15.
 */

public class FindPwdPresenter extends BasePresenter implements IFindPwdPresenter {

	private UserService userService;
	private IFindPwdView view;

	public FindPwdPresenter(IFindPwdView view){
		userService = new UserService();
		this.view = view;
	}

	@Override
	public void changePwd(String pwd) {
		userService.changePwd(pwd)
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
