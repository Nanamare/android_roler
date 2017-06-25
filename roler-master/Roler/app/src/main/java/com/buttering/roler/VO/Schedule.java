package com.buttering.roler.VO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.alamkanak.weekview.WeekViewEvent;
import com.buttering.roler.R;
import com.buttering.roler.util.MyApplication;
import com.google.gson.reflect.TypeToken;
import com.vocketlist.android.roboguice.log.Ln;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by ichaeeun on 2016. 7. 30..
 */
public class Schedule {

	private int id;
	private String content;
	private String startTime;
	private String endTime;
	private int role_id;
	private int user_id;

	public Date getDate() {
		return date;
	}

	private Date date;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public static Type getListType() {
		return new TypeToken<List<Schedule>>() {
		}.getType();
	}

	@SuppressLint("SimpleDateFormat")
	public WeekViewEvent toWeekViewEvent(Schedule schedule, Date date) {

		Random random = new Random();

		Calendar tempDate = toCalendar(date);

		DateFormat dateType = new SimpleDateFormat(MyApplication.getInstance().getString(R.string.today_data_format));
		dateType.setTimeZone(TimeZone.getDefault());
		Ln.v(TimeZone.getDefault());
		String nowDate = dateType.format(tempDate.getTime());


		Calendar getStartTime = Calendar.getInstance();
		SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			getStartTime.setTime(startDate.parse(nowDate + ' ' + schedule.getStartTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar getEndTime = Calendar.getInstance();
		SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			getEndTime.setTime(endDate.parse(nowDate + ' ' + schedule.getEndTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Create an week view event.
		WeekViewEvent weekViewEvent = new WeekViewEvent();
		weekViewEvent.setId(schedule.getId());
		weekViewEvent.setName(schedule.getContent());
		weekViewEvent.setStartTime(getStartTime);
		weekViewEvent.setEndTime(getEndTime);
		weekViewEvent.setColor((MyApplication.getInstance().getResources().getColor(bgColor[random.nextInt(6)])));
		return weekViewEvent;
	}

	public static Calendar toCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}



	final int[] bgColor = {R.color.role_color_0, R.color.role_color_1, R.color.role_color_2, R.color.role_color_3,
			R.color.role_color_4, R.color.role_color_5};


}
