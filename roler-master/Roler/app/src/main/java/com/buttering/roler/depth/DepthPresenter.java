package com.buttering.roler.depth;

import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.UserService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-06-06.
 */

public class DepthPresenter extends BasePresenter implements IDepthPresenter {

	UserService userService;
	IDepthView view;

	public DepthPresenter(IDepthView view){
		userService = new UserService();
		this.view = view;
	}

	@Override
	public void isCheckTokenExpired(String token) {

		userService.checkUserToken(token)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<String>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(String string) {

						if(string.equals("false")){
							//expired Todo goToLoginActivity
							view.logOut();
							view.goToLoginActivity();
						} else {
							//valid Todo nothing
						}

					}
				});
	}
}
