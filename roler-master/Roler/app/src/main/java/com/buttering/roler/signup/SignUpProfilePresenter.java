package com.buttering.roler.signup;

import android.app.Activity;

import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.VO.User;
import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.FileService;
import com.buttering.roler.composition.baseservice.UserService;
import com.buttering.roler.composition.serialization.RolerResponse;
import com.buttering.roler.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
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

	public SignUpProfilePresenter(Activity activity, ISignUpProfileView view) {
		this.view = view;
		this.activity = activity;
		this.userService = new UserService();

	}


	@Override
	public Observable<User> signUp(String email, String pwd, String name) {
		view.showLoadingBar();

		User user = generateUser(email, pwd, name);

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
									if (parseResult(json) == "true") {
										MyInfoDAO.getInstance().setPicUrl(json);
										subscriber.onNext(parseUrl(json));
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							private String parseUrl(String json) {
								try {
									JSONObject object = new JSONObject(json);
									JSONArray jsonArray = new JSONArray(object.getString("params"));
									String todoJson = jsonArray.toString();
									return todoJson;
								} catch (JSONException e) {
									e.printStackTrace();
									return "false";
								}
							}

							private String parseResult(String json) {
								JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
								String result = ja.get("result").getAsString();
								return result;
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


	@Override
	public Observable<String> loadProfileImg(String email) {

		return Observable.create(subscriber -> {
			addSubscription(fileService
					.loadProfileImg(email)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<ResponseBody>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(ResponseBody responseBody) {

						}
					}));
		});
	}

}
