package com.buttering.roler.plan;

import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.RoleService;
import com.buttering.roler.composition.baseservice.TodoService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class PlanPresenter extends BasePresenter implements IPlanPresenter {

    private TodoService todoService;
    private IPlanView view;
    private RoleService roleService;

    public PlanPresenter(IPlanView view) {
        todoService = new TodoService();
        roleService = new RoleService();
        this.view = view;
    }

    @Override
    public void loadToList(int userId, int roleId) {

        addSubscription(todoService
                .getTodoList(userId, roleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Todo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Todo> todos) {
                        view.setTodoList(todos);
                    }
                }));

    }

    @Override
    public void getRoleContent(int id) {

        addSubscription(roleService
                .getRoleContent(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Role>>() {
                    @Override
                    public void onCompleted() {
                        view.setCurrentPosition();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(List<Role> role) {
                        view.setRoleContent(role);
                        onCompleted();

                    }
                }));


    }

    @Override
    public void addTodo(String content, int todoOrder, String todoDate, int role_id, int user_id, boolean isDone) {

        addSubscription(todoService
        .addTodoList(content, todoOrder, todoDate, role_id, user_id, isDone)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Todo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Todo todo) {
                view.setTodo(todo);
            }
        }));
    }
}
