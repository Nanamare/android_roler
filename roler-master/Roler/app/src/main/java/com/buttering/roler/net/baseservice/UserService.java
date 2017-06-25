package com.buttering.roler.net.baseservice;

import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.User;
import com.buttering.roler.net.serialization.RolerResponse;
import com.buttering.roler.util.MyApplication;
import com.buttering.roler.util.SharePrefUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2016-12-17.
 */

public class UserService extends BaseService {

	public UserService() {
		super(UserAPI.class);
	}

	@Override
	public UserAPI getAPI() {
		return (UserAPI) super.getAPI();
	}


	public Observable<Void> signUp(User user) {

		return Observable.create(subscriber ->
				getAPI().signUp(user.getName(), user.getEmail(), user.getPassword())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<ResponseBody>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(ResponseBody responseBody) {

						try {
							String result = parseParams(responseBody.string());
							if (result.equals("true")) {
								subscriber.onNext(null);
							} else {
								subscriber.onError(
										new Throwable(MyApplication.getInstance().getString(R.string.impossible_sign_up)));
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					private String parseParams(String json) {
						JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
						return ja.get("result").getAsString();
					}


//					private void saveMyInfo(String userId, String name) {
//						MyInfoDAO.getInstance().setUserId(userId);
//						MyInfoDAO.getInstance().setName(name);
//						MyInfoDAO.getInstance().saveAccountInfo(userId, user.getEmail(), user.getPassword(), user.getName(), "NULL");
//					}

				}));
	}

	public Observable<RolerResponse> setProfilePhotos(String profileImgUrl) {

		User userInfo = MyInfoDAO.getInstance().getMyUserInfo();
		userInfo.setPicture_url(profileImgUrl);

		RolerRequest req = new RolerRequest.Builder()
				.setParam(new Gson().toJson(userInfo)).build();

		return getAPI().setProfilePhotos(req)
				.subscribeOn(Schedulers.io());
	}


	public Observable<Void> isDuplicateEmail(String email) {

		return Observable.create(subscriber -> {
			getAPI()
					.isDuplicateEmail(email)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
							subscriber.unsubscribe();
						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(ResponseBody responseBody) {
							try {
								String result = parseParams(responseBody.string());
								if (result.equals("true")) {
									subscriber.onNext(null);
								} else {
									subscriber.onError(new Throwable());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						private String parseParams(String json) {
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							return ja.get("result").getAsString();
						}

					});

		});
	}

	public Observable<Void> signIn(String email, String pwd) {
		return Observable.create(subscriber -> {
			getAPI().signIn(email, pwd)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							subscriber.onError(new Throwable(
									MyApplication.getInstance().getString(R.string.not_working_server)));
						}

						@Override
						public void onNext(ResponseBody responseBody) {
							try {
								String result = parseParams(responseBody.string());
								if (result.equals("true")) {
									subscriber.onNext(null);
								} else if (result.equals("false")) {
									subscriber.onError(
											new Throwable(
													MyApplication.getInstance().getString(R.string.check_user_info)));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						private String parseParams(String json) {
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String result = ja.get("result").getAsString();
							if (result.equals("true")) {
								String name = ja.get("name").getAsString();
								String email = ja.get("email").getAsString();
								String id = ja.get("id").getAsString();
								String picUrl = ja.get("imageUrl").getAsString();
								String accessToken = ja.get("access_token").getAsString();
								SharePrefUtil.putSharedPreference("accessToken", accessToken);
								MyInfoDAO.getInstance().loginAccountInfo(id, email, name, picUrl);
							}
							return result;
						}


					});

		});

	}


	public Observable<Void> registerToken(String token, String email) {
		return Observable.create(subscriber -> {
			getAPI().registerToken(token, email)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(ResponseBody responseBody) {

							try {
								String result = responseBody.string();
								if (getStatusResult(result) == "true") {
									subscriber.onNext(null);

								} else {

									subscriber.onError(new Throwable());

								}

							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});

		});
	}

	public Observable<String> checkPwd(String name, String pwd) {
		return Observable.create(subscriber -> {
			getAPI().checkPwd(name, pwd)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(ResponseBody responseBody) {
							try {
								String result = responseBody.string();
								if (getStatusResult(result) == "true") {
									subscriber.onNext(parseParams(result));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						private String parseParams(String json) {
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String code = ja.get("confirmation_token").getAsString();
							return code;
						}

					});

		});
	}

	public Observable<Void> changePwd(String pwd, String email) {
		return Observable.create(subscriber -> getAPI().changePwd(pwd, email)
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<ResponseBody>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(ResponseBody responseBody) {
						try {
							String result = responseBody.string();
							if (getStatusResult(result) == "true") {
								subscriber.onNext(null);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}));
	}

	public Observable<Void> refreshToken(String id, String email) {
		return Observable.create(subscriber -> {
			getAPI().refreshToken(id, email)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Response<ResponseBody>>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(Response<ResponseBody> responseBodyResponse) {
							String json = null;
							try {
								json = responseBodyResponse.body().string();
							} catch (IOException e) {
								e.printStackTrace();
							}
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String accessToken = ja.get("access_token").getAsString();
							SharePrefUtil.putSharedPreference("accessToken", accessToken);


						}
					});

		});
	}

	public Observable<String> checkUserToken(String token){

		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {

				getAPI()
						.checkUserToken(token)
						.subscribeOn(Schedulers.io())
						.subscribe(new Subscriber<Response<ResponseBody>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								e.printStackTrace();
							}

							@Override
							public void onNext(Response<ResponseBody> responseBodyResponse) {
								try {
									String json = responseBodyResponse.body().string();
									JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
									String result = ja.get("result").getAsString();
									subscriber.onNext(result);
								} catch (IOException e) {
									e.printStackTrace();
								}

							}
						});

			}
		});

	}

	public interface UserAPI {

		@FormUrlEncoded
		@POST("/users/refresh")
		Observable<Response<ResponseBody>> refreshToken(@Field("id") String id, @Field("email") String email);

		@FormUrlEncoded
		@POST("/users/signin")
		Observable<ResponseBody> signIn(@Field("email") String email, @Field("password") String password);

		@GET("/users/duplitcation")
		Observable<ResponseBody> isDuplicateEmail(@Query("email") String email);

		@PUT("/users/photos")
		Observable<RolerResponse> setProfilePhotos(@Body RolerRequest req);

		@GET("/users/check")
		Observable<ResponseBody> checkPwd(@Query("name") String name, @Query("email") String email);

		@FormUrlEncoded
		@POST("/users/change")
		Observable<ResponseBody> changePwd(@Field("password") String pwd, @Field("email") String email);

		@FormUrlEncoded
		@POST("/users/signup")
		Observable<ResponseBody> signUp(@Field("name") String name, @Field("email") String email, @Field("password") String password);

		@FormUrlEncoded
		@PUT("/fcm/register")
		Observable<ResponseBody> registerToken(@Field("token") String token, @Field("email") String email);

		@FormUrlEncoded
		@POST("/users/token_check")
		Observable<Response<ResponseBody>> checkUserToken(@Field("token") String token);

	}
}
