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

import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.role.RoleActivity;
import com.buttering.roler.setting.SettingActivity;
import com.buttering.roler.timetable.BaseActivity;

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

public class PlanActivity extends AppCompatActivity implements IPlanView {

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

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);

        setToolbar();
        setUserName();
        setDate();

        //get mock data
        allRoleList = receiveRoles();

        planPresenter = new PlanPresenter(this);

        adapter = new PlanActivityAdapter(this, allRoleList);
        vp_rolePlanPage.setAdapter(adapter);
        planPresenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));
        //get a current role id
        currentPosition = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();

        addTodoList();

        swipeRole();

        //initSet LayoutManager
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_todolist.setLayoutManager(linearLayoutManager);

        //make a adapter
        listRecall();



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

                int role_id = ((Role) adapter.getItem(vp_rolePlanPage.getScrollPosition())).getRole_id();

                planPresenter.loadToList(Integer.valueOf(MyInfoDAO.getInstance().getUserId()), role_id);
            }
        });

        right_arrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = vp_rolePlanPage.getScrollPosition();
                if (currentPosition < vp_rolePlanPage.getCount()-1) {
                    currentPosition += 1;
                    vp_rolePlanPage.scrollToPosition(currentPosition);

                } else if (currentPosition == vp_rolePlanPage.getCount()-1){
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
                    value.toString();
                    Todo todo = new Todo();
                    todo.setContent(value);
                    allTodoList.get(currentPosition).add(todo);
                    todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
                    rv_todolist.setAdapter(todoAdapter);
//                    todoAdapter.notifyDataSetChanged();
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

    private void listRecall() {

        //get a mock data
        allTodoList = receiveTodoItems();
        todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
        rv_todolist.setAdapter(todoAdapter);

    }

    private List<List<Todo>> receiveTodoItems() {

        //테스트용 for문 START
        todolist = new ArrayList<>();


        todo = new Todo();
        todo.setRole_id(0);
        todo.setId(0);
        todo.setContent("역할별로 할일을 적어 보세요!");
        todo.setDone(false);
        todolist.add(todo);
        allTodoList.add(todolist);

        //꼭 고쳐야하는 코드
        allTodoList.add(todolist);
        allTodoList.add(todolist);
        allTodoList.add(todolist);
        allTodoList.add(todolist);
        allTodoList.add(todolist);
        allTodoList.add(todolist);


//
//		todo = new Todo();
//		todo.setRole_id(0);
//		todo.setId(0);
//		todo.setContent("test 0");
//		todo.setDone(false);
//		todolist.add(todo);
//		allTodoList.add(todolist);
//
//		todolist = new ArrayList<>();
//		todo = new Todo();
//		todo.setRole_id(1);
//		todo.setId(0);
//		todo.setContent("test 1");
//		todo.setDone(false);
//		todolist.add(todo);
//
//		todo = new Todo();
//		todo.setRole_id(1);
//		todo.setId(0);
//		todo.setContent("test 1");
//		todo.setDone(false);
//		todolist.add(todo);
//		allTodoList.add(todolist);
//
//		todo = new Todo();
//		todo.setRole_id(3);
//		todo.setId(1);
//		todo.setContent("test 0");
//		todo.setDone(false);
//		todolist.add(todo);
//
//		todo = new Todo();
//		todo.setRole_id(3);
//		todo.setId(1);
//		todo.setContent("test 0");
//		todo.setDone(false);
//		todolist.add(todo);
//		allTodoList.add(todolist);

        //테스트용 for문 END
        return allTodoList;

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public List<Role> receiveRoles() {
        Role role = null;
        role = new Role();
        role.setId(0);
        role.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
        role.setRoleName("사랑하는 사람");
        role.setRolePrimary(2);
        role.setUser_id(1);
        role.setRole_id(0);
        roles.add(role);

//        Role role5 = null;
//        role5 = new Role();
//        role5.setId(1);
//        role5.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role5.setRoleName("사랑하는 사람22");
//        role5.setRolePrimary(2);
//        role5.setUser_id(1);
//        role5.setRole_id(0);
//        roles.add(role5);
//
//        Role role2 = null;
//        role2 = new Role();
//        role2.setId(2);
//        role2.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role2.setRoleName("사랑하는 사람33");
//        role2.setRolePrimary(2);
//        role2.setUser_id(1);
//        role2.setRole_id(0);
//        roles.add(role2);
//
//        Role role3 = null;
//        role3 = new Role();
//        role3.setId(4);
//        role3.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role3.setRoleName("사랑하는 사람44");
//        role3.setRolePrimary(2);
//        role3.setUser_id(1);
//        role3.setRole_id(0);
//        roles.add(role3);
//
//        Role role4 = null;
//        role4 = new Role();
//        role4.setId(3);
//        role4.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role4.setRoleName("사랑하는 사람55");
//        role4.setRolePrimary(2);
//        role4.setUser_id(1);
//        role4.setRole_id(0);
//        roles.add(role4);
//
//        Role role6 = null;
//        role6 = new Role();
//        role6.setId(0);
//        role6.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role6.setRoleName("사랑하는 사람66");
//        role6.setRolePrimary(2);
//        role6.setUser_id(1);
//        role6.setRole_id(0);
//        roles.add(role6);
//
//        Role role7 = null;
//        role7 = new Role();
//        role7.setId(0);
//        role7.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role7.setRoleName("사랑하는 사람77");
//        role7.setRolePrimary(2);
//        role7.setUser_id(1);
//        role7.setRole_id(0);
//        roles.add(role7);
//
//        Role role88 = null;
//        role88 = new Role();
//        role88.setId(0);
//        role88.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
//        role88.setRoleName("사랑하는 사람88");
//        role88.setRolePrimary(2);
//        role88.setUser_id(1);
//        role88.setRole_id(0);
//        roles.add(role88);

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

    @Override
    public void setRoleContent(List<Role> roleList) {
        if (adapter != null) {
            allRoleList = roleList;
            adapter = new PlanActivityAdapter(this, allRoleList);
            vp_rolePlanPage.setAdapter(adapter);
//            adapter.setRoleList(roleList);
//            adapter.notifyDataSetChanged();
        }

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
    public void hideLoadingBar() {
        if (dialog != null)
            dialog.dismiss();
    }


    @Override
    public void setTodoList(List<Todo> todoList) {
        if (todoAdapter != null) {
//            todoAdapter.setTodoList(todoList);
//            todoAdapter.notifyDataSetChanged();
            allTodoList.set(currentPosition, todoList);
            todoAdapter = new PlanActivityTodoAdapter(this, allTodoList.get(currentPosition), R.layout.activity_todolist_item);
            rv_todolist.setAdapter(todoAdapter);
        }
    }
}
