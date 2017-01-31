package com.buttering.roler.timetable;

/**
 * Created by kinamare on 2017-02-01.
 */

public interface ITimePresenter {
	void getSchduleList(int user_id, String date);
	void addSchdule(String content, String startTime,String endTime,String date
			,int user_id, int role_id);
}
