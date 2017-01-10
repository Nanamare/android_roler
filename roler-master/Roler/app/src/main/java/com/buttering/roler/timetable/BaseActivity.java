package com.buttering.roler.timetable;

/**
 * Created by nanamare on 2016-07-31.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.buttering.roler.R;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.plan.PlanActivity;
import com.buttering.roler.signup.SignUpProfileTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public abstract class BaseActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
	private static final int TYPE_DAY_VIEW = 1;
	private static final int TYPE_THREE_DAY_VIEW = 2;
	private static final int TYPE_WEEK_VIEW = 3;
	private int mWeekViewType;


	WeekView mWeekView;
	FloatingActionButton floatingActionButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		setToolbar();


		floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final View innerView = getLayoutInflater().inflate(R.layout.dialog_time_custom, null);
				AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this);
				alert.setTitle("일정 추가 하기");
				alert.setView(innerView);
				// Set an EditText view to get user input


				TextView txtTime = (TextView) innerView.findViewById(R.id.dialog_startTime_edt);
				txtTime.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final Calendar c = Calendar.getInstance();
						int hourofDay = c.get(Calendar.HOUR_OF_DAY);
						int minite = c.get(Calendar.MINUTE);
						TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								txtTime.setText(hourOfDay + " : " + minute);
							}
						};

						ScheduleDialog dialog = new ScheduleDialog(BaseActivity.this, timeSetListener, hourofDay, minite, true);
						dialog.setTitle("시작 시간을 골라주세요");
						dialog.show();

					}
				});
//				txtTime.setOnFocusChangeListener((view, hasfocus) -> {
//					if (hasfocus) {
//						final Calendar c = Calendar.getInstance();
//						int hourofDay = c.get(Calendar.HOUR_OF_DAY);
//						int minite = c.get(Calendar.MINUTE);
//						TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
//							@Override
//							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//								txtTime.setText(hourOfDay + " : " + minute);
//							}
//						};
//
//						ScheduleDialog dialog = new ScheduleDialog(BaseActivity.this, timeSetListener, hourofDay, minite, true);
//						dialog.setTitle("시작 시간을 골라주세요");
//						dialog.show();
//
//
//					}
//				});




				alert.setPositiveButton("확인", (dialog, whichButton) -> {
//					String value = input.getText().toString();
//					if (!value.isEmpty()) {
//						value.toString();
//
//
//					} else {
//						Toast.makeText(getApplicationContext(), "일정을 입력해 주세요.", Toast.LENGTH_SHORT).show();
//					}
				});


				alert.setNegativeButton("취소",
						(dialog, whichButton) -> {
							// Canceled.
						});
				AlertDialog dialog = alert.create();
				dialog.show();
			}
		});


		// Get a reference for the week view in the layout.
		mWeekView = (WeekView) findViewById(R.id.weekView);

		// Show a toast message about the touched event.
		mWeekView.setOnEventClickListener(this);
		mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
			@Override
			public void onEmptyViewClicked(Calendar time) {
				Toast.makeText(BaseActivity.this, "비어있는 곳 클릭", Toast.LENGTH_SHORT).show();


			}
		});


		// The week view has infinite scrolling horizontally. We have to provide the events of a
		// month every time the month changes on the week view.

		mWeekView.setMonthChangeListener(this);

		// Set long press listener for events.
		mWeekView.setEventLongPressListener(this);

		// Set long press listener for empty view
		mWeekView.setEmptyViewLongPressListener(this);

		// Set up a date time interpreter to interpret how the date and time will be formatted in
		// the week view. This is optional.
		setupDateTimeInterpreter(false);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}


	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		imageView.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
		textView.setTextColor(Color.BLACK);
		textView.setText("오늘의 일정 관리");
		setSupportActionBar(toolbar);


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		setupDateTimeInterpreter(id == R.id.action_week_view);
		switch (id) {
			case R.id.action_today: {
				mWeekView.goToToday();
			}
			return true;
			case R.id.action_day_view:
				if (mWeekViewType != TYPE_DAY_VIEW) {
					item.setChecked(!item.isChecked());
					mWeekViewType = TYPE_DAY_VIEW;
					mWeekView.setNumberOfVisibleDays(1);

					// Lets change some dimensions to best fit the view.
					mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
					mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
					mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
				}
				return true;
			case R.id.action_three_day_view:
				if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
					item.setChecked(!item.isChecked());
					mWeekViewType = TYPE_THREE_DAY_VIEW;
					mWeekView.setNumberOfVisibleDays(3);

					// Lets change some dimensions to best fit the view.
					mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
					mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
					mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
				}
				return true;
			case R.id.action_week_view:
				if (mWeekViewType != TYPE_WEEK_VIEW) {
					item.setChecked(!item.isChecked());
					mWeekViewType = TYPE_WEEK_VIEW;
					mWeekView.setNumberOfVisibleDays(7);

					// Lets change some dimensions to best fit the view.
					mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
					mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
					mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
				}
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set up a date time interpreter which will show short date values when in week view and long
	 * date values otherwise.
	 *
	 * @param shortDate True if the date values should be short.
	 */
	private void setupDateTimeInterpreter(final boolean shortDate) {
		mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
			@Override
			public String interpretDate(Calendar date) {
				SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
				String weekday = weekdayNameFormat.format(date.getTime());
				SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

				// All android api level do not have a standard way of getting the first letter of
				// the week day name. Hence we get the first char programmatically.
				// Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
				if (shortDate)
					weekday = String.valueOf(weekday.charAt(0));
				return "              " + weekday.toUpperCase() + "요일 " + format.format(date.getTime()) + "              ";
			}

			@Override
			public String interpretTime(int hour) {
				return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
			}
		});
	}

	protected String getEventTitle(Calendar time) {
		return String.format("%02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
		//return "Chaeeun Fighting!";
	}

	@Override
	public void onEventClick(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(this, "해결 완료", Toast.LENGTH_SHORT).show();
		event.setColor(getResources().getColor(R.color.body_background_green));
		mWeekView.notifyDatasetChanged();

	}

	@Override
	public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEmptyViewLongPress(Calendar time) {
		Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
	}

	public WeekView getWeekView() {
		return mWeekView;
	}

}
