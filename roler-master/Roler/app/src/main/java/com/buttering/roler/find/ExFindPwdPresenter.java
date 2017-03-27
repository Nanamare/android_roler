package com.buttering.roler.find;

import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.UserService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-03-15.
 */

public class ExFindPwdPresenter extends BasePresenter implements IExFindPwdPresenter {

	private UserService userService;
	private IExFindPwdView view;

	public ExFindPwdPresenter(IExFindPwdView view){
		userService = new UserService();
		this.view = view;
	}

	@Override
	public void checkPwd(String name, String pwd) {
		userService.checkPwd(name, pwd)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<String>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(String authorizationCode) {
						view.visibleAuthorizationTextView(authorizationCode);
					}
				});
	}
}
