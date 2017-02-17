package com.buttering.roler.plan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.login.ILoginPresenter;
import com.buttering.roler.login.LoginPresenter;
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
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import me.leolin.shortcutbadger.ShortcutBadger;
import rx.Subscriber;

public class PlanActivity extends AppCompatActivity implements IPlanView {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


	@BindView(R.id.activity_plan_name_tv)
	TextView name;
	@BindView(R.id.vp_rolePlanPage)
	FeatureCoverFlow vp_rolePlanPage;
	@BindView(R.id.rv_todolist)
	RecyclerView rv_todolist;
	@BindView(R.id.rl_planBelowBottom)
	CardView rl_planBelowBottom;
	@BindView(R.id.tv_yearMonth)
	TextView tv_yearMonth;
	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_day)
	TextView tv_day;
	@BindView(R.id.activity_plan_arrow_left_iv)
	ImageView left_arrow_iv;
	@BindView(R.id.activity_plan_arrow_right_iv)
	ImageView right_arrow_iv;

	private List<Role> allRoleList;
	private List<List<Todo>> allTodoList = new ArrayList<>();
	private PlanActivityAdapter adapter;
	private PlanActivityTodoAdapter todoAdapter;
	private int currentPosition;
	private IPlanPresenter planPresenter;
	private ACProgressFlower dialog;
	private List<Role> roles = new ArrayList<>();
	private Todo todo = null;
	private List<Todo> todolist;
	private ILoginPresenter tokenPresenter;

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
//
//		FirebaseApp.initializeApp(this);


		setToolbar();
		setUserName();
		setDate();

		//get mock data
		allRoleList = receiveRoles();

		planPresenter = new PlanPresenter(this, getApplicationContext());
		tokenPresenter = new LoginPresenter();

		adapter = new PlanActivityAdapter(this, allRoleList);
		vp_rolePlanPage.setAdapter(adapter);
		planPresenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));
		//get a current role id
		currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();

		addTodoList();

		swipeRole();
		scrollPostion();

		//initSet LayoutManager
		linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rv_todolist.setLayoutManager(linearLayoutManager);

		//make a adapter
		setListView();

//		FCM
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
				planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), role_id);
			}

			@Override
			public void onScrolling() {
				//TODO CoverFlow began scrolling
				Toast.makeText(PlanActivity.this, "Todolist 로딩중", Toast.LENGTH_SHORT).show();
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
				planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), role_id);
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
				planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), role_id);
			}
		});
	}

	private void addTodoList() {
		rl_planBelowBottom.setOnClickListener(v -> {
			AlertDialog.Builder alert = new AlertDialog.Builder(PlanActivity.this);

			alert.setTitle("To Do List 추가하기");
			alert.setMessage("내용을 입력해주세요");

			// Set an EditText view to get user input
			final EditText input = new EditText(PlanActivity.this);
			alert.setView(input);

			alert.setPositiveButton("확인", (dialog, whichButton) -> {
				String value = input.getText().toString();
				if (!value.isEmpty()) {
//					check "역할별로 할일을 적어 보세요!"
//					for (int i = 0; i < allTodoList.size(); i++) {
//						for (int j = 0; j < allTodoList.get(i).size(); j++) {
//							if (allTodoList.get(j).get(0).getContent().equals("역할별로 할일을 적어 보세요!")) {
//								allTodoList.get(j).remove(0);
//							}
//						}
//					}
					value.toString();
					Todo todo = new Todo();
					todo.setContent(value);
					allTodoList.get(currentPosition).add(todo);
					todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
					rv_todolist.setAdapter(todoAdapter);
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar calendar = Calendar.getInstance();
					String date = sdf.format(calendar.getTime());
					int role_id = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
					planPresenter.addTodo(value
							, 1, date, role_id
							, Integer.valueOf(MyInfoDAO.getInstance().getUserId()), false);
//					planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), role_id);
//					todoAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
				}
			});


			alert.setNegativeButton("취소",
					(dialog, whichButton) -> {
						// Canceled.
					});

			alert.show();

		});
	}

	private void setDate() {
//		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		name.setText(MyInfoDAO.getInstance().getNickName() + " 님 안녕하세요!");
	}

	private void setListView() {

		//get a mock data
		allTodoList = receiveTodoItems();
		todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
		rv_todolist.setAdapter(todoAdapter);

	}

	private List<List<Todo>> receiveTodoItems() {
		todolist = new ArrayList<>();

		todo = new Todo();
		todo.setRole_id(0);
		todo.setId(0);
		todo.setContent("역할별로 할일을 적어 보세요!");
		todo.setDone(false);
		todolist.add(todo);
		allTodoList.add(todolist);

		//꼭 고쳐야하는 코드
		for (int loop = 0; loop < 999; loop++) {
			allTodoList.add(todolist);
		}


		return allTodoList;

	}


	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				badgeReceiver,
				new IntentFilter("badgeCount")
		);
	}

	public List<Role> receiveRoles() {
		Role role = null;
		role = new Role();
		role.setId(0);
		role.setRoleContent("역할에 대한 목표,설명을 적어 주세요");
		role.setRoleName("역할을 정해보세요!");
		role.setRolePrimary(1);
		role.setUser_id(1);
		role.setRole_id(0);
		roles.add(role);

		return roles;
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

			Toast.makeText(this, "refreshing...", Toast.LENGTH_SHORT).show();
			planPresenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));

		}

		return super.onOptionsItemSelected(item);
	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		ImageView imageView2 = (ImageView) findViewById(R.id.custom_toolbar_right_iv);
		imageView2.setImageResource(R.drawable.icon_timeline);
		imageView.setImageResource(R.drawable.icon_profile);
		textView.setTextColor(Color.BLACK);
		textView.setText("Plan");
		setSupportActionBar(toolbar);

		imageView.setOnClickListener(v -> {
			startActivity(new Intent(PlanActivity.this, RoleActivity.class));
		});

		imageView2.setOnClickListener(v -> {
			startActivity(new Intent(PlanActivity.this, BaseActivity.class));
		});


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
		if (adapter != null) {
			allRoleList = roleList;
			adapter = new PlanActivityAdapter(this, allRoleList);
			vp_rolePlanPage.setAdapter(adapter);
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
					planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), movePosition);
				}
			}
		}

	}

	@Override
	public void getTodoList() {
		planPresenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));
	}

	@Override
	public void setCurrentPosition() {
		currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
		planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), currentPosition);
	}

	@Override
	public void showLoadingBar() {
		dialog = new ACProgressFlower.Builder(this)
				.direction(ACProgressConstant.DIRECT_CLOCKWISE)
				.themeColor(Color.WHITE)
				.fadeColor(Color.DKGRAY).build();
		dialog.show();
	}

	@Override
	public void setTodo(Todo todo) {
		if (todoAdapter != null) {
			todoAdapter.setTodo(todo);
			todoAdapter.notifyDataSetChanged();
//            allTodoList.set(currentPosition, todoList);
//            todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
//            rv_todolist.setAdapter(todoAdapter);
		}

	}

	@Override
	public void setTodoListId(int id) {
		allTodoList.get(currentPosition).get(allTodoList.get(currentPosition).size() - 1).setId(id);
	}

	@Override
	public void refreshProgress() {
		currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
//		planPresenter.updateRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), currentPosition);
		//progress를 업데이트 하기 위한 작업
		planPresenter.updateProgress(currentPosition, Integer.valueOf(MyInfoDAO.getInstance().getUserId()));
	}

	@Override
	public void refreshProgressLast() {
//		currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();
		planPresenter.updateRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), currentPosition);
	}


	@Override
	public void hideLoadingBar() {
		if (dialog != null)
			dialog.dismiss();
	}


	@Override
	public void setTodoList(List<Todo> todoList) {

		if (todoAdapter != null) {
			todolist.clear();
			todolist.addAll(todoList);
			allTodoList.set(currentPosition, todoList);
			todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
			rv_todolist.setAdapter(todoAdapter);
		}
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



}
