package com.buttering.roler.signup;

import android.widget.EditText;

import com.buttering.roler.composition.basepresenter.IBasePresenter;

import rx.Observable;

/**
 * Created by kinamare on 2016-12-18.
 */

public interface ISignUpPresenter extends IBasePresenter{
	void registerUser(String email, String pwd);
	void check_blank(EditText activity_signup_edt_id, EditText activity_signup_edt_pwd);
	Observable<String> checkDuplicateEmail(String email);
}
