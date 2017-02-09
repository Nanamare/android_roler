package com.buttering.roler.composition.baseservice;

import com.alamkanak.weekview.WeekViewEvent;
import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Schedule;
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
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by kinamare on 2017-02-01.
 */

public class ScheduleService extends BaseService {

	public ScheduleService() {
		super(ScheduleAPI.class);
	}

	@Override
	public ScheduleAPI getAPI() {
		return (ScheduleAPI) super.getAPI();
	}

	public Observable<List<Schedule>> getScheduleList(int user_id, String date) {

		return Observable.create(subscriber -> {
			getAPI().getScheduleList(user_id, date)
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
									List<Schedule> roleList = parseParams(result);
									subscriber.onNext(roleList);

								} else {
									subscriber.onError(new Throwable());
								}

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						private List<Schedule> parseParams(String json) {
							ArrayList<Schedule> list = new ArrayList<>();

							try {
								JSONObject object = new JSONObject(json);
								JSONArray jsonArray = new JSONArray(object.getString("params"));
								String roleJson = jsonArray.toString();
								list.addAll(new Gson().fromJson(roleJson, Schedule.getListType()));

							} catch (JSONException e) {
								e.printStackTrace();
							}


							return list;
						}

					});
		});
	}

	public Observable<Void> addSchedule(String content, String startTime, String endTime, String date
			, int user_id, int role_id) {
		return Observable.create(subscriber -> {
			getAPI().addSchedule(content, startTime, endTime, date, user_id)
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

	public Observable<Void> deleteSchdule(int scheduleId) {
		return Observable.create(subscriber -> {
			getAPI().deleteSchdule(scheduleId)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {

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

	public interface ScheduleAPI {

		@GET("/schedule/read")
		Observable<ResponseBody> getScheduleList(@Query("user_id") int id
				, @Query("date") String date);


		@FormUrlEncoded
		@POST("/schedule/create")
		Observable<ResponseBody> addSchedule(@Field("content") String content
				, @Field("startTime") String startTime, @Field("endTime") String endTime
				, @Field("date") String date, @Field("user_id") int user_id);

		@DELETE("/schedule/delete")
		Observable<ResponseBody> deleteSchdule(@Query("id") int schedule_id);

	}

}
