package com.buttering.roler.signup;

import android.app.Activity;

import com.buttering.roler.VO.User;
import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.FileService;
import com.buttering.roler.composition.baseservice.UserService;
import com.buttering.roler.composition.serialization.RolerResponse;
import com.buttering.roler.util.FileUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-17.
 */

public class SignUpProfilePresenter extends BasePresenter implements ISignUpProfilePresenter {

	private ISignUpProfileView view;
	private Activity activity;
	private Subscription signUpSubscription;
	private UserService userService;
	private FileService fileService;

	public  SignUpProfilePresenter(Activity activity, ISignUpProfileView view){
		this.view = view;
		this.activity = activity;
		this.userService = new UserService();

	}


	@Override
	public Observable<User> signUp(String email, String pwd, String name) {
		view.showLoadingBar();

		User user = generateUser(email, pwd,name);


		return userService
				.signUp(user)
				.observeOn(AndroidSchedulers.mainThread());
	}

	private User generateUser(String email, String pwd, String name) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(pwd);
		user.setName(name);
		return user;
	}

	@Override
	public Observable<String> uploadProfileImg(File file) {

		return Observable.create(subscriber -> {

			if (file == null) {
				subscriber.onNext("");
				subscriber.onCompleted();
			} else {
				fileService
						.uploadProfileImg(FileUtil.makeMultiPartBody(file))
						.subscribe(new Subscriber<ResponseBody>() {
							@Override
							public void onCompleted() {
								subscriber.onCompleted();
							}

							@Override
							public void onError(Throwable e) {
								subscriber.onNext("");
							}

							@Override
							public void onNext(ResponseBody responseBody) {
								try {
									String json = responseBody.string();
									subscriber.onNext(parseUrl(json));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							private String parseUrl(String json) {
								JsonElement jsonElement = new JsonParser().parse(json);
								String url = jsonElement
										.getAsJsonObject()
										.getAsJsonObject("result")
										.getAsJsonPrimitive("uploadUrl").getAsString();

								return url;
							}
						});
			}


		});
	}

	@Override
	public Observable<Void> setProfileImg(String profileImgUrl) {
		return Observable.create((Observable.OnSubscribe<Void>) subscriber -> {
			userService
					.setProfilePhotos(profileImgUrl)
					.subscribe(new Subscriber<RolerResponse>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
						}

						@Override
						public void onError(Throwable e) {
							subscriber.onError(e);
						}

						@Override
						public void onNext(RolerResponse rolerResponse) {

						}
					});

		}).observeOn(AndroidSchedulers.mainThread());

	}

}
