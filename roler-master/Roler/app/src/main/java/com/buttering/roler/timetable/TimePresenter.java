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
	public void getSchduleList(String date) {
		view.showLoadingBar();

		addSubscription(scheduleService
				.getScheduleList(date)
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
	                       String date) {
		addSubscription(scheduleService
				.addSchedule(content, startTime, endTime, date)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Void aVoid) {
						view.updateSchedule();
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
//						view.updateWeekView();
						view.updateSchedule();
					}
				}));
	}


}
