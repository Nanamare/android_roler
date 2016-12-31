package com.buttering.roler.login;

import rx.Observable;

/**
 * Created by kinamare on 2016-12-31.
 */

public interface ILoginPresenter {
	Observable<String> signIn(String email, String pwd);
}
