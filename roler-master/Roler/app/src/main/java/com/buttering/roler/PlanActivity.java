package com.buttering.roler;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.adapter.PlanActivityAdapter;
import com.buttering.roler.adapter.PlanActivityTodoAdapter;
import com.buttering.roler.adapter.RoleActivityAdapter;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class PlanActivity extends AppCompatActivity {


    List<Role> allRoleList = null;
    List<Todo> allTodoList = null;
    PlanActivityAdapter adapter = null;
    PlanActivityTodoAdapter todoAdapter = null;

    FeatureCoverFlow vp_rolePlanPage = null;

    RecyclerView rv_todolist = null;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);


        vp_rolePlanPage = (FeatureCoverFlow) findViewById(R.id.vp_rolePlanPage);
        rv_todolist = (RecyclerView) findViewById(R.id.rv_todolist);

        allRoleList = receiveRoles();
        adapter = new PlanActivityAdapter(this, allRoleList);
        vp_rolePlanPage.setAdapter(adapter);


        vp_rolePlanPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PlanActivity.this);

                alert.setTitle("To Do List");
                alert.setMessage("내용을 입력해주세요");

                // Set an EditText view to get user input
                final EditText input = new EditText(PlanActivity.this);
                alert.setView(input);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        value.toString();

                        Todo todo = new Todo();
                        todo.setContent(value);

                        allTodoList.add(todo);

                        todoAdapter = new PlanActivityTodoAdapter(PlanActivity.this, allTodoList, R.layout.activity_todolist_item);
                        rv_todolist.setAdapter(todoAdapter);

                    }
                });


                alert.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                alert.show();
            }
        });


        //LayoutManager 생성 START
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //LayoutManager 생성 END

        rv_todolist.setLayoutManager(linearLayoutManager);
        listRecall();   //adapter 생성


    }

    private void listRecall() {

        allTodoList = receiveTodoItems();
        todoAdapter = new PlanActivityTodoAdapter(this, allTodoList, R.layout.activity_todolist_item);
        rv_todolist.setAdapter(todoAdapter);//adapter 다시 만들어서 연결

    }

    private List<Todo> receiveTodoItems() {

        allTodoList = new ArrayList<>();
        //테스트용 for문 START
        Todo todo = null;

        todo = new Todo();

        todo.setId(0);
        todo.setContent("test");
        todo.setDone(true);
        allTodoList.add(todo);
        todo = new Todo();
        todo.setId(1);
        todo.setContent("어머니 안아 드리기");
        todo.setDone(true);
        allTodoList.add(todo);
        todo = new Todo();
        todo.setId(2);
        todo.setContent("일찍 일어나기");
        todo.setDone(true);
        allTodoList.add(todo);
        todo = new Todo();
        todo.setId(3);
        todo.setContent("택배 찾아 오기");
        todo.setDone(true);
        allTodoList.add(todo);
        todo = new Todo();
        todo.setId(4);
        todo.setContent("올때 순간 접착제 사기");
        todo.setDone(true);
        allTodoList.add(todo);

        todo = new Todo();
        todo.setId(5);
        todo.setContent("집에가서 북어국 끓이기");
        todo.setDone(true);
        allTodoList.add(todo);

        todo = new Todo();
        todo.setId(6);
        todo.setContent("발표자료 만들기");
        todo.setDone(true);
        allTodoList.add(todo);

        todo = new Todo();
        todo.setId(7);
        todo.setContent("씻고 잠 자기");
        todo.setDone(true);
        allTodoList.add(todo);

        todo = new Todo();
        todo.setId(8);
        todo.setContent("페이스북 친구 안부 묻기");
        todo.setDone(true);
        allTodoList.add(todo);

        todo = new Todo();
        todo.setId(9);
        todo.setContent("친구한테 전화 하기");
        todo.setDone(true);
        allTodoList.add(todo);

        todo = new Todo();
        todo.setId(10);
        todo.setContent("10시에 집가기");
        todo.setDone(true);
        allTodoList.add(todo);

        //테스트용 for문 END
        return allTodoList;

    }

    @Override
    protected void onResume() {
        super.onResume();
        allRoleList = receiveRoles();
        adapter = new PlanActivityAdapter(this, allRoleList);
        vp_rolePlanPage.setAdapter(adapter);
    }

    public List<Role> receiveRoles() {
        Log.d("PlanActivity", "receiveRoles START");
        List<Role> roles = new ArrayList<>();

        //테스트용 for문 START
        Role role = null;
        role = new Role();
        role.setId(1);
        role.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다.");
        role.setRoleName("사랑하는 사람");
        role.setRolePrimary(2);
        role.setUser_id(2);
        roles.add(role);

        role = new Role();
        role.setId(2);
        role.setRoleContent("경영학적인 도전을 게을리하지 않는다. 수익과 니즈, 시장을 항상 살피며, 생각하고, 공부한다,");
        role.setRoleName("경영학도로서의 나");
        role.setRolePrimary(3);
        role.setUser_id(3);
        roles.add(role);

        role = new Role();
        role.setId(0);
        role.setRoleContent("가족을 사랑하는 내가 된다. 항상 부모님께 감사하는 마음을 가지고 생활 한다 으쌰!");
        role.setRoleName("아들이자 동생");
        role.setRolePrimary(1);
        role.setUser_id(1);
        roles.add(role);

        role = new Role();
        role.setId(3);
        role.setRoleContent("테스트 용");
        role.setRoleName("테스트 용");
        role.setRolePrimary(4);
        role.setUser_id(4);
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

    public void mOnclick() {
//        Intent intent = new Intent(getApplicationContext(),xxxxx.class);
//        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_day:
                startActivity(new Intent(PlanActivity.this, DayActivity.class));
                return true;
            case android.R.id.home:
                startActivity(new Intent(PlanActivity.this, RoleActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


}
