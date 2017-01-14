package com.buttering.roler.composition.baseservice;

import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2017-01-15.
 */

public class TodoService extends BaseService{

	public TodoService() {
		super(TodoApi.class);
	}

	@Override
	public TodoApi getAPI() {
		return (TodoApi) super.getAPI();
	}

	public Observable<List<Todo>> getTodoList(int userId, int roleId){

		return Observable.create(subscriber -> {
				getAPI().getTodoList(userId, roleId)
						.subscribeOn(Schedulers.io())
						.subscribe(new Subscriber<ResponseBody>() {
							@Override
							public void onCompleted() {
								subscriber.onCompleted();
								subscriber.unsubscribe();
							}

							@Override
							public void onError(Throwable e) {
								subscriber.onError(e);
							}

							@Override
							public void onNext(ResponseBody responseBody) {
								try {
									String result = responseBody.string();
									if (getStatusResult(result) == "true") {
										List<Todo> todoList = parseParams(result);
										subscriber.onNext(todoList);

									} else {
										subscriber.onError(new Throwable());
									}

								} catch (IOException e) {
									e.printStackTrace();
								}

							}

							private List<Todo> parseParams(String json) {
								ArrayList<Todo> todo = new ArrayList<>();

								try {
									JSONObject object = new JSONObject(json);
									JSONArray jsonArray = new JSONArray(object.getString("params"));
									String todoJson = jsonArray.toString();
									todo.addAll(new Gson().fromJson(todoJson, Todo.getListType()));
									todo.size();
								} catch (JSONException e) {
									e.printStackTrace();
								}


								return todo;
							}

						});


		});

	};


	public interface TodoApi {

		@GET("/todo/read")
		Observable<ResponseBody> getTodoList(@Query("user_id")int userId,@Query("role_id")int roleId);

	}

}
