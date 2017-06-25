package com.buttering.roler.plan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.depth.DepthBaseActivity;
import com.buttering.roler.dialog.TodoDialog;
import com.buttering.roler.helper.KeyboardHelper;
import com.buttering.roler.login.ILoginPresenter;
import com.buttering.roler.login.LoginPresenter;
import com.buttering.roler.role.EditRoleActivity;
import com.buttering.roler.role.RoleActivity;
import com.buttering.roler.setting.SettingActivity;
import com.buttering.roler.timetable.BaseActivity;
import com.buttering.roler.util.MyApplication;
import com.buttering.roler.util.SharePrefUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import me.leolin.shortcutbadger.ShortcutBadger;
import rx.Subscriber;

/**
 * Created by nanamare on 16. 7. 31..
 */
public class PlanActivity extends DepthBaseActivity implements IPlanView {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@BindView(R.id.activity_plan_add_role_iv) AppCompatImageView addRoleIv;
	@BindView(R.id.role_empty_ll) LinearLayout role_empty_ll;
	@BindView(R.id.activity_plan_name_tv) AppCompatTextView name;
	@BindView(R.id.vp_rolePlanPage) FeatureCoverFlow vp_rolePlanPage;
	@BindView(R.id.rv_todolist) RecyclerView rv_todolist;
	@BindView(R.id.rl_planBelowBottom) CardView rl_planBelowBottom;
	@BindView(R.id.tv_yearMonth) AppCompatTextView tv_yearMonth;
	@BindView(R.id.tv_date) AppCompatTextView tv_date;
	@BindView(R.id.tv_day) AppCompatTextView tv_day;
	@BindView(R.id.activity_plan_arrow_left_iv) AppCompatImageView left_arrow_iv;
	@BindView(R.id.activity_plan_arrow_right_iv) AppCompatImageView right_arrow_iv;
	@BindView(R.id.activity_plan_bottom_ll) LinearLayout plan_bottom_ll;

	private List<Role> allRoleList = new ArrayList<>();
	private List<List<Todo>> allTodoList = new ArrayList<>();
	private PlanActivityAdapter adapter;
	private PlanActivityTodoAdapter todoAdapter;
	private int currentPosition;
	private IPlanPresenter planPresenter;
	private SweetAlertDialog materialDialog;
	private Todo todo = null;
	private List<Todo> todolist = new ArrayList<>();
	private ILoginPresenter tokenPresenter;
	private int roleSize;

	private LinearLayoutManager linearLayoutManager;


	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i("df", "This device is not supported.");
			}
			return false;
		}
		return true;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan);
		ButterKnife.bind(this);

		setToolbar();
		setUserName();
		setDate();

		planPresenter = new PlanPresenter(this, this);
		tokenPresenter = new LoginPresenter(this);

		//smooth scrolling
		rv_todolist.setNestedScrollingEnabled(false);
		ViewCompat.setNestedScrollingEnabled(vp_rolePlanPage, false);

		roleSize = allRoleList.size();
		if (roleSize == 0) {
			role_empty_ll.setVisibility(View.VISIBLE);
			vp_rolePlanPage.setVisibility(View.GONE);
			plan_bottom_ll.setVisibility(View.GONE);
			left_arrow_iv.setVisibility(View.GONE);
			right_arrow_iv.setVisibility(View.GONE);
		}


		allTodoList = receiveTodoItems();
		planPresenter.getRoleContent();

		swipeRole();
		scrollPostion();

		//FCM
		SharePrefUtil.putSharedPreference("isFcmToken", false);
		if (checkPlayServices()) {
			if (!SharePrefUtil.getBooleanSharedPreference("isFcmToken")) {
				FirebaseMessaging.getInstance().subscribeToTopic("news");
				String token = FirebaseInstanceId.getInstance().getToken();
				tokenPresenter.registerToken(token, MyInfoDAO.getInstance().getEmail())
						.subscribe(new Subscriber<Void>() {
							@Override
							public void onCompleted() {
								SharePrefUtil.putSharedPreference("fcmToken", token);
								SharePrefUtil.putSharedPreference("isFcmToken", true);
								unsubscribe();

							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(Void aVoid) {
								onCompleted();

							}
						});
			}
		}

		ShortcutBadger.removeCount(MyApplication.getInstance().getContext()); //for 1.1.4+


	}


	private void scrollPostion() {
		vp_rolePlanPage.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
			@Override
			public void onScrolledToPosition(int position) {
				//TODO CoverFlow stopped to position
				int role_id = ((Role) adapter.getItem(position)).getRole_id();
				planPresenter.loadToList(role_id);
			}

			@Override
			public void onScrolling() {
				//TODO CoverFlow began scrolling
				Toast.makeText(PlanActivity.this, getString(R.string.load_role), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void swipeRole() {

			left_arrow_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					currentPosition = vp_rolePlanPage.getScrollPosition();
					if (currentPosition == 0) {
						currentPosition = vp_rolePlanPage.getCount();
						vp_rolePlanPage.scrollToPosition(currentPosition);
					}
					if (currentPosition > 0) {
						currentPosition -= 1;
						vp_rolePlanPage.scrollToPosition(currentPosition);
					}

					int role_id = ((Role) adapter.getItem(currentPosition)).getRole_id();
					planPresenter.loadToList(role_id);
				}
			});


			right_arrow_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					currentPosition = vp_rolePlanPage.getScrollPosition();
					if (currentPosition < vp_rolePlanPage.getCount() - 1) {
						currentPosition += 1;
						vp_rolePlanPage.scrollToPosition(currentPosition);

					} else if (currentPosition == vp_rolePlanPage.getCount() - 1) {
						vp_rolePlanPage.scrollToPosition(0);
						currentPosition = 0;

					}

					int role_id = ((Role) adapter.getItem(currentPosition)).getRole_id();
					planPresenter.loadToList(role_id);
				}
			});
	}

	@OnClick(R.id.activity_plan_bottom_ll)
	public void planBottomLayoutOnClick(){
		if (allRoleList.size() != 0) {

			TodoDialog dialog = new TodoDialog(PlanActivity.this);
			KeyboardHelper.show(dialog);
			dialog.show();

			dialog.setTodoListener(new TodoDialog.TodoListener() {
				@Override
				public void addTodoList(String contents) {

					Todo todo = new Todo();
					todo.setContent(contents);
					allTodoList.get(currentPosition).add(todo);
					todoAdapter = new PlanActivityTodoAdapter(PlanActivity.this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
					rv_todolist.setAdapter(todoAdapter);
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calendar = Calendar.getInstance();
					String date = sdf.format(calendar.getTime());
					int role_id = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
					planPresenter.addTodo(contents
							, 1, date, role_id, false);

				}

				@Override
				public void onCancel() {

				}
			});

		} else {
			Toast.makeText(this, getString(R.string.empty_role), Toast.LENGTH_SHORT).show();
		}

	}

	private void setDate() {
		Calendar calendar = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("yyyy - MM");
		String date = sdf.format(calendar.getTime());
		tv_yearMonth.setText(date);
		DateFormat bigSdf = new SimpleDateFormat("dd");
		String bigDate = bigSdf.format(calendar.getTime());
		tv_date.setText(bigDate);
		final String[] week = {"일", "월", "화", "수", "목", "금", "토"};
		tv_day.setText(week[calendar.get(calendar.DAY_OF_WEEK) - 1] + "요일");
	}


	private void setUserName() {
		name.setText(MyInfoDAO.getInstance().getNickName() + " 님 ");
	}

	private List<List<Todo>> receiveTodoItems() {
		todo = new Todo();
		todolist.add(todo);
		allTodoList.add(todolist);

		//꼭 고쳐야하는 코드
		for (int loop = 0; loop < 9999; loop++) {
			allTodoList.add(todolist);
		}

		return allTodoList;
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				badgeReceiver,
				new IntentFilter("badgeCount")
		);
		planPresenter.getRoleContent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.plan, menu);
		this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_profile);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_setting) {

			Intent setIntent = new Intent(this, SettingActivity.class);
			startActivity(setIntent);

		} else if (id == R.id.action_refresh) {

			Toast.makeText(this, getString(R.string.refresh_plan), Toast.LENGTH_SHORT).show();
			planPresenter.getRoleContent();

		}

		return super.onOptionsItemSelected(item);
	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		ImageView imageView2 = (ImageView) findViewById(R.id.custom_toolbar_right_iv);
		imageView2.setImageResource(R.drawable.icon_timeline);
		imageView.setImageResource(R.drawable.icon_profile);
		setSupportActionBar(toolbar);

		imageView.setOnClickListener(v -> {
			startActivity(new Intent(PlanActivity.this, RoleActivity.class));
		});

		imageView2.setOnClickListener(v -> {
			startActivity(new Intent(PlanActivity.this, BaseActivity.class));
		});

	}

	@OnClick(R.id.activity_plan_add_role_iv)
	public void addRoleOnClick(){
		Intent intent = new Intent(this, EditRoleActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Roler를 종료 하시겠습니까?").setCancelable(false)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	@Override
	public void setRoleContent(List<Role> roleList) {
		if (roleList.size() == 0) {
			role_empty_ll.setVisibility(View.VISIBLE);
			vp_rolePlanPage.setVisibility(View.GONE);
			plan_bottom_ll.setVisibility(View.GONE);
			left_arrow_iv.setVisibility(View.GONE);
			right_arrow_iv.setVisibility(View.GONE);
		} else {
			left_arrow_iv.setVisibility(View.VISIBLE);
			right_arrow_iv.setVisibility(View.VISIBLE);
			plan_bottom_ll.setVisibility(View.VISIBLE);
			role_empty_ll.setVisibility(View.GONE);
			allRoleList = roleList;
			adapter = new PlanActivityAdapter(this, allRoleList);
			vp_rolePlanPage.setAdapter(adapter);
			vp_rolePlanPage.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void moveRoleContent(List<Role> roleList, int movePosition) {
		if (adapter != null) {
			allRoleList = roleList;
			adapter = new PlanActivityAdapter(this, allRoleList);
			vp_rolePlanPage.setAdapter(adapter);
			for (int position = 0; position < adapter.getCount(); position++) {
				if (((Role) adapter.getItem(position)).getRole_id() == movePosition) {
					vp_rolePlanPage.scrollToPosition(position);
					planPresenter.loadToList(movePosition);
				}
			}
		}
	}

	@Override
	public void getTodoList() {
		planPresenter.getRoleContent();
	}

	@Override
	public void setCurrentPosition() {
		if (allRoleList.size() != 0) {
			currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
			planPresenter.loadToList(currentPosition);
		}
	}


	@Override
	public void setTodo(Todo todo) {
		if (todoAdapter != null) {
			todoAdapter.setTodo(todo);
			todoAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void setTodoListId(int id) {
		allTodoList.get(currentPosition).get(allTodoList.get(currentPosition).size() - 1).setId(id);
	}

	@Override
	public void refreshProgress() {
		currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
		planPresenter.updateProgress(currentPosition);
	}

	@Override
	public void refreshProgressLast() {
		planPresenter.updateRoleContent(currentPosition);
	}


	@Override
	public void setTodoList(List<Todo> todoList) {
		linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rv_todolist.setLayoutManager(linearLayoutManager);
		todolist.clear();
		todolist.addAll(todoList);
		allTodoList.set(currentPosition, todoList);
		todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
		rv_todolist.setAdapter(todoAdapter);
	}

	private BroadcastReceiver badgeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get the received random number
			int badgeCount = intent.getIntExtra("badgeCount", 0);
			updateBadge(badgeCount);
		}
	};


	private void updateBadge(int count) {
		ShortcutBadger.applyCount(this, count); //for 1.1.4+
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(badgeReceiver);
	}

	@Override
	public void showLoadingBar() {
		materialDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		materialDialog.getProgressHelper().setBarColor(this.getResources().getColor(R.color.dialog_color));
		materialDialog.setTitleText(getString(R.string.loading_dialog_title));
		materialDialog.setCancelable(false);
		materialDialog.show();
	}

	@Override
	public void hideLoadingBar() {
		if (materialDialog != null)
			materialDialog.dismiss();
	}



}
