package com.buttering.roler.signup;

import com.buttering.roler.VO.User;
import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.FileService;
import com.buttering.roler.net.baseservice.UserService;
import com.buttering.roler.net.serialization.RolerResponse;
import com.buttering.roler.util.FileUtil;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-17.
 */

public class SignUpProfilePresenter extends BasePresenter implements ISignUpProfilePresenter {

	private ISignUpProfileView view;
	private UserService userService;
	private FileService fileService;

	public SignUpProfilePresenter(ISignUpProfileView view) {
		this.view = view;
		this.userService = new UserService();
		this.fileService = new FileService();
	}

	public SignUpProfilePresenter() {
		this.userService = new UserService();
		this.fileService = new FileService();

	}


	@Override
	public Observable<User> signUp(String email, String pwd, String name) {

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

//	@Override
//	public Observable<String> uploadProfileImg(File file) {
//
//		return Observable.create(subscriber -> {
//
//			if (file == null) {
//				subscriber.onNext("");
//				subscriber.onCompleted();
//			} else {
//				fileService
//						.uploadProfileImg(FileUtil.makeMultiPartBody(file))
//						.subscribe(new Subscriber<ResponseBody>() {
//							@Override
//							public void onCompleted() {
//								subscriber.onCompleted();
//							}
//
//							@Override
//							public void onError(Throwable e) {
//								subscriber.onNext("");
//							}
//
//							@Override
//							public void onNext(ResponseBody responseBody) {
//								try {
//									String json = responseBody.string();
//									if (parseResult(json) == "true") {
//										MyInfoDAO.getInstance().setPicUrl(parseResult(json));
//										subscriber.onNext(parseResult(json));
//									}
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//							}
//
////							private String parseUrl(String json) {
////								try {
////									JSONObject object = new JSONObject(json);
////									JSONArray jsonArray = new JSONArray(object.getString("params"));
////									String todoJson = jsonArray.toString();
////									return todoJson;
////								} catch (JSONException e) {
////									e.printStackTrace();
////									return "false";
////								}
////							}
//
//							private String parseResult(String json) {
//								JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
//								String result = ja.get("imageUrl").getAsString();
//								return result;
//							}
//
//						});
//			}
//
//
//		});
//	}

	@Override
	public Observable<String> uploadProfileImg(File file) {

		return fileService
				.uploadProfileImg(FileUtil.makeMultiPartBody(file))
				.observeOn(AndroidSchedulers.mainThread());
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
