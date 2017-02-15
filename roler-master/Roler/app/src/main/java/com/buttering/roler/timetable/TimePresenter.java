package com.buttering.roler.timetable;

import com.buttering.roler.VO.Schedule;
import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.ScheduleService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-01.
 */

public class TimePresenter extends BasePresenter implements ITimePresenter {

	private ScheduleService scheduleService;
	private ITimeView view;

	public TimePresenter(ITimeView view) {
		this.scheduleService = new ScheduleService();
		this.view = view;
	}


	@Override
	public void getSchduleList(int user_id, String date) {
		view.showLoadingBar();

		addSubscription(scheduleService
				.getScheduleList(user_id, date)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<List<Schedule>>() {
					@Override
					public void onCompleted() {
						view.hideLoadingBar();

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						view.hideLoadingBar();

					}

					@Override
					public void onNext(List<Schedule> schedules) {
						view.setScheduleList(schedules);
						onCompleted();
					}
				}));
	}

	@Override
	public void addSchdule(String content, String startTime, String endTime,
	                       String date, int user_id, int role_id) {
		addSubscription(scheduleService
				.addSchedule(content, startTime, endTime, date, user_id, role_id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {
						unsubscribe();
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						onError(e);
					}

					@Override
					public void onNext(Void aVoid) {
						onCompleted();
					}
				}));

	}

	@Override
	public void deleteSchdule(int schudule_id) {
		addSubscription(scheduleService
				.deleteSchdule(schudule_id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Void aVoid) {

					}
				}));
	}


}
