package com.buttering.roler.signup;

import java.io.File;

import rx.Observable;

/**
 * Created by kinamare on 2016-12-17.
 */

public interface ISignUpProfilePresenter {
	Observable<Void> signUp(String email, String pwd, String name);

	Observable<String> uploadProfileImg(File file);

	Observable<Void> setProfileImg(String profileImgUrl);

	Observable<String> loadProfileImg(String email);
}
