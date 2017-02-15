package com.buttering.roler.net.baseservice;

import com.buttering.roler.VO.Role;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2017-01-13.
 */

public class RoleService extends BaseService {

	public RoleService() {
		super(RoleApi.class);
	}

	@Override
	public RoleApi getAPI() {
		return (RoleApi) super.getAPI();
	}

	public Observable<List<Role>> getRoleContent(int id) {

		return Observable.create(subscriber -> {
			getAPI().getRoleContent(id)
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
								String result = responseBody.string();
								if (getStatusResult(result) == "true") {
									List<Role> roleList = parseParams(result);
									subscriber.onNext(roleList);

								} else {
									subscriber.onError(new Throwable());
								}

							} catch (IOException e) {
								e.printStackTrace();
							}


						}

						private List<Role> parseParams(String json) {
							ArrayList<Role> roleList = new ArrayList<>();

							try {
								JSONObject object = new JSONObject(json);
								JSONArray jsonArray = new JSONArray(object.getString("params"));
								String roleJson = jsonArray.toString();
								roleList.addAll(new Gson().fromJson(roleJson, Role.getListType()));

							} catch (JSONException e) {
								e.printStackTrace();
							}


							return roleList;
						}

					});

		});
	}


	public Observable<Void> addRole(int rolePrimary, String roleName, String roleContent, int user_id) {
		return Observable.create(subscriber -> {
			getAPI().addRole(rolePrimary, roleName, roleContent, user_id)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							unsubscribe();
						}

						@Override
						public void onError(Throwable e) {

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

	public  Observable<Void> deleteRole(int role_id){
		return Observable.create(subscriber -> {
			getAPI().deleteRole(role_id)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
						}

						@Override
						public void onError(Throwable e) {

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

	public Observable<Void> editRole(int rolePrimary, String roleName, String roleContent, int role_id) {
		return Observable.create(subscriber -> {
			getAPI().editRole(rolePrimary, roleName, roleContent, role_id)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							unsubscribe();
						}

						@Override
						public void onError(Throwable e) {

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

	public Observable<Void> updateProgress(int role_id, int user_id){

		return Observable.create(subscriber -> {
			getAPI().updateProgress(role_id, user_id)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							subscriber.onNext(null);

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
									onCompleted();

								} else {

								}

							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});
		});
	}

	public interface RoleApi {

		@GET("/role/read")
		Observable<ResponseBody> getRoleContent(@Query("user_id") int id);

		@FormUrlEncoded
		@POST("/role/create")
		Observable<ResponseBody> addRole(@Field("rolePrimary") int rolePrimary
				, @Field("roleName") String roleName
				, @Field("roleContent") String roleContent
				, @Field("user_id") int user_id);


		@DELETE("/role/delete")
		Observable<ResponseBody> deleteRole(@Query("id") int role_id);

		@FormUrlEncoded
		@PUT("/role/update")
		Observable<ResponseBody> editRole(@Field("rolePrimary") int rolePrimary
				, @Field("roleName") String roleName
				, @Field("roleContent") String roleContent
				, @Field("role_id") int role_id);

		@PUT("/role/progress")
		Observable<ResponseBody> updateProgress(@Query("role_id") int todoId, @Query("user_id") int user_id);

	}
}
