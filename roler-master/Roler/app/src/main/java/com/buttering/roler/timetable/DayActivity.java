//package com.buttering.roler.timetable;
//
///**
// * Created by nanamare on 2016-07-31.
// */
//
//import android.content.Intent;
//
//import com.alamkanak.weekview.WeekViewEvent;
//import com.buttering.roler.R;
//import com.buttering.roler.timetable.BaseActivity;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
///**
// * A basic example of how to use week view library.
// * Created by Raquib-ul-Alam Kanak on 1/3/2014.
// * Website: http://alamkanak.github.io
// */
//public class DayActivity extends BaseActivity {
//
//
//	@Override
//	public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
//		// Populate the week view with some events.
//		List<WeekViewEvent> events = new ArrayList<>();
//
//		Intent intent = getIntent();
//		if (intent != null) {
//			int startTimeOfDay = Integer.valueOf(intent.getExtras().getString("startTimeOfDay"));
//			int startMinOfDay = Integer.valueOf(intent.getExtras().getString("startMinOfDay"));
//			int endTimeOfDay= Integer.valueOf(intent.getExtras().getString("endTimeOfDay"));
//			int endMinOfDay = Integer.valueOf(intent.getExtras().getString("endMinOfDay"));
//			String content = intent.getExtras().getString("content");
//
//			Calendar startTime = Calendar.getInstance();
//			startTime.set(Calendar.HOUR_OF_DAY, startTimeOfDay);
//			startTime.set(Calendar.MINUTE, startMinOfDay);
//			startTime.set(Calendar.MONTH, newMonth - 1);
//			startTime.set(Calendar.YEAR, newYear);
//			Calendar endTime = (Calendar) startTime.clone();
//			endTime.set(Calendar.HOUR, endTimeOfDay);
//			endTime.set(Calendar.MINUTE,endMinOfDay);
//			endTime.set(Calendar.MONTH, newMonth - 1);
//			WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime) + content, startTime, endTime);
//			event.setColor(getResources().getColor(R.color.c1));
//			events.add(event);
//		}
//
//
//		Calendar startTime = Calendar.getInstance();
//		startTime.set(Calendar.HOUR_OF_DAY, 3);
//		startTime.set(Calendar.MINUTE, 0);
//		startTime.set(Calendar.MONTH, newMonth - 1);
//		startTime.set(Calendar.YEAR, newYear);
//		Calendar endTime = (Calendar) startTime.clone();
//		endTime.add(Calendar.HOUR, 1);
//		endTime.set(Calendar.MONTH, newMonth - 1);
//		WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime) + "어머니 생신 선물 사기 \n 카페 쿠폰 쓰기", startTime, endTime);
//		event.setColor(getResources().getColor(R.color.c1));
//		events.add(event);
//
//		startTime = Calendar.getInstance();
//		startTime.set(Calendar.HOUR_OF_DAY, 4);
//		startTime.set(Calendar.MINUTE, 20);
//		startTime.set(Calendar.MONTH, newMonth - 1);
//		startTime.set(Calendar.YEAR, newYear);
//		endTime = (Calendar) startTime.clone();
//		endTime.set(Calendar.HOUR_OF_DAY, 5);
//		endTime.set(Calendar.MINUTE, 0);
//		event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//		event.setColor(getResources().getColor(R.color.c1));
//		events.add(event);
//
//		startTime = Calendar.getInstance();
//		startTime.set(Calendar.HOUR_OF_DAY, 5);
//		startTime.set(Calendar.MINUTE, 30);
//		startTime.set(Calendar.MONTH, newMonth - 1);
//		startTime.set(Calendar.YEAR, newYear);
//		endTime = (Calendar) startTime.clone();
//		endTime.add(Calendar.HOUR_OF_DAY, 2);
//		endTime.set(Calendar.MONTH, newMonth - 1);
//		event = new WeekViewEvent(2, getEventTitle(startTime) + "택배 붙이기", startTime, endTime);
//		event.setColor(getResources().getColor(R.color.c1));
//		events.add(event);
//
//		startTime = Calendar.getInstance();
//		startTime.set(Calendar.DAY_OF_MONTH, 15);
//		startTime.set(Calendar.HOUR_OF_DAY, 3);
//		startTime.set(Calendar.MINUTE, 0);
//		startTime.set(Calendar.MONTH, newMonth - 1);
//		startTime.set(Calendar.YEAR, newYear);
//		endTime = (Calendar) startTime.clone();
//		endTime.add(Calendar.HOUR_OF_DAY, 3);
//		event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
//		event.setColor(getResources().getColor(R.color.c1));
//		events.add(event);
//
//		startTime = Calendar.getInstance();
//		startTime.set(Calendar.DAY_OF_MONTH, 1);
//		startTime.set(Calendar.HOUR_OF_DAY, 3);
//		startTime.set(Calendar.MINUTE, 0);
//		startTime.set(Calendar.MONTH, newMonth - 1);
//		startTime.set(Calendar.YEAR, newYear);
//		endTime = (Calendar) startTime.clone();
//		endTime.add(Calendar.HOUR_OF_DAY, 3);
//		event = new WeekViewEvent(5, getEventTitle(startTime) + "핸드폰 바꾸기", startTime, endTime);
//		event.setColor(getResources().getColor(R.color.colorPrimary));
//		events.add(event);
//
//		startTime = Calendar.getInstance();
//		startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
//		startTime.set(Calendar.HOUR_OF_DAY, 15);
//		startTime.set(Calendar.MINUTE, 0);
//		startTime.set(Calendar.MONTH, newMonth - 1);
//		startTime.set(Calendar.YEAR, newYear);
//		endTime = (Calendar) startTime.clone();
//		endTime.add(Calendar.HOUR_OF_DAY, 3);
//		event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//		event.setColor(getResources().getColor(R.color.colorPrimary));
//		events.add(event);
//
//
//		return events;
//	}
//
//}
