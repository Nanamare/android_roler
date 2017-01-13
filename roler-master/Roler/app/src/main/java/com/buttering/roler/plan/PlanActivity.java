package com.buttering.roler.plan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.buttering.roler.timetable.BaseActivity;
import com.buttering.roler.R;
import com.buttering.roler.setting.SettingActivity;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.role.RoleActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class PlanActivity extends AppCompatActivity {

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

	private List<Role> allRoleList = null;
	private List<List<Todo>> allTodoList = null;
	private PlanActivityAdapter adapter = null;
	private PlanActivityTodoAdapter todoAdapter = null;
	private int currentPosition;
	List<Role> roles = new ArrayList<>();
	private Todo todo = null;
	List<Todo> todolist;


	LinearLayoutManager linearLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan);
		ButterKnife.bind(this);

		setToolbar();
		setUserInfo();
		setDate();


		allRoleList = receiveRoles();
		adapter = new PlanActivityAdapter(this, allRoleList);
		vp_rolePlanPage.setAdapter(adapter);

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
					value.toString();
					Todo todo = new Todo();
					todo.setContent(value);
					allTodoList.get(currentPosition).add(todo);
					todoAdapter.notifyDataSetChanged();
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


		left_arrow_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPosition = vp_rolePlanPage.getScrollPosition();
				if (currentPosition == 0) {
					currentPosition = vp_rolePlanPage.getCount();
				}
				if (currentPosition > 0) {
					currentPosition -= 1;
					vp_rolePlanPage.scrollToPosition(currentPosition);
				}

				listRecall();
			}
		});

		right_arrow_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPosition = vp_rolePlanPage.getScrollPosition();
				if (currentPosition < vp_rolePlanPage.getCount()) {
					vp_rolePlanPage.scrollToPosition(currentPosition + 1);
				}
				listRecall();
			}
		});


		//LayoutManager 생성 START
		linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		//LayoutManager 생성 END

		rv_todolist.setLayoutManager(linearLayoutManager);
		listRecall();   //adapter 생성


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

	private void setUserInfo() {
		name.setText(MyInfoDAO.getInstance().getNickName() + " 님 안녕하세요!");

	}

	private void listRecall() {

		allTodoList = receiveTodoItems();
		todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
		rv_todolist.setAdapter(todoAdapter);//adapter 다시 만들어서 연결

	}

	private List<List<Todo>> receiveTodoItems() {

		allTodoList = new ArrayList<>();
		//테스트용 for문 START
		todolist = new ArrayList<>();


		todo = new Todo();
		todo.setRole_id(0);
		todo.setId(0);
		todo.setContent("test 0");
		todo.setDone(true);
		todolist.add(todo);

		todo = new Todo();
		todo.setRole_id(0);
		todo.setId(0);
		todo.setContent("test 0");
		todo.setDone(true);
		todolist.add(todo);
		allTodoList.add(todolist);

		todolist = new ArrayList<>();
		todo = new Todo();
		todo.setRole_id(1);
		todo.setId(0);
		todo.setContent("test 1");
		todo.setDone(true);
		todolist.add(todo);

		todo = new Todo();
		todo.setRole_id(1);
		todo.setId(0);
		todo.setContent("test 1");
		todo.setDone(true);
		todolist.add(todo);
		allTodoList.add(todolist);

		//테스트용 for문 END
		return allTodoList;

	}

	@Override
	protected void onResume() {
		super.onResume();
//		allRoleList = receiveRoles();
//		adapter = new PlanActivityAdapter(this, allRoleList);
//		vp_rolePlanPage.setAdapter(adapter);
	}

	public List<Role> receiveRoles() {
		Log.d("PlanActivity", "receiveRoles START");


		//테스트용 for문 START
		Role role = null;
		role = new Role();
		role.setId(1);
		role.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
		role.setRoleName("사랑하는 사람");
		role.setRolePrimary(2);
		role.setUser_id(1);
		roles.add(role);

		role = new Role();
		role.setId(2);
		role.setRoleContent("경영학적인 도전을 게을리하지 않는다. 수익과 니즈, 시장을 항상 살피며, 생각하고, 공부한다,");
		role.setRoleName("경영학도로서의 나");
		role.setRolePrimary(3);
		role.setUser_id(2);
		roles.add(role);


		//테스트용 for문 END
		//서버에서 받아오거나 혹은 SharedPreference에 있는 정보를 넣을 것(협의 안됨)

		Log.d("PlanActivity", "role list 정보: " + roles);
		Log.d("PlanActivity", "receiveRoles END");
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

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_setting) {

			Intent setIntent = new Intent(this, SettingActivity.class);
			startActivity(setIntent);

		} else if (id == R.id.action_refresh) {
			// signin 정보 재확인 후 main으로 이동
			Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
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


}
