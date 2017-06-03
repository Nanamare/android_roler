package com.buttering.roler.timetable;

/**
 * Created by kinamare on 2017-02-01.
 */

public interface ITimePresenter {
	void getSchduleList(String date);
	void addSchdule(String content, String startTime, String endTime, String date);
	void deleteSchdule(int schudule_id);

}
