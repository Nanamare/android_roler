package com.buttering.roler.plan;

import android.content.Context;

import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;
import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.RoleService;
import com.buttering.roler.net.baseservice.TodoService;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class PlanPresenter extends BasePresenter implements IPlanPresenter {

	private TodoService todoService;
	static private IPlanView view;
	private RoleService roleService;
	private List<Role> roles;
	private Context context;

	public PlanPresenter(IPlanView view, Context context) {
		todoService = new TodoService();
		roleService = new RoleService();
		this.view = view;
		roles = new ArrayList<>();
		this.context = context;
	}

	public PlanPresenter(Context context) {
		this.context = context;
		roles = new ArrayList<>();
		todoService = new TodoService();
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
						e.printStackTrace();
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

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();

					}

					@Override
					public void onNext(List<Role> role) {
						view.setRoleContent(role);
						view.setCurrentPosition();
						onCompleted();

					}
				}));

	}

	@Override
	public void updateRoleContent(int id, int movePosition) {

		addSubscription(roleService
				.getRoleContent(id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<List<Role>>() {
					@Override
					public void onCompleted() {
//						view.setCurrentPosition();

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();

					}

					@Override
					public void onNext(List<Role> role) {
						view.moveRoleContent(role,movePosition);

					}
				}));

	}

	@Override
	public void updateProgress(int role_id, int user_id) {
		addSubscription(roleService
		.updateProgress(role_id, user_id)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Subscriber<Void>() {
			@Override
			public void onCompleted() {
				view.refreshProgressLast();
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(Void aVoid) {
//				view.getTodoList();
				onCompleted();

			}
		}));

	}


//	@Override
//	public void addTodo(String content, int todoOrder, String todoDate, int role_id, int user_id, boolean isDone) {
//
//		addSubscription(todoService
//				.addTodoList(content, todoOrder, todoDate, role_id, user_id, isDone)
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Subscriber<Todo>() {
//					@Override
//					public void onCompleted() {
//
//					}
//
//					@Override
//					public void onError(Throwable e) {
//
//					}
//
//					@Override
//					public void onNext(Todo todo) {
//						view.setTodo(todo);
//					}
//				}));
//	}

	@Override
	public void addTodo(String content, int todoOrder, String todoDate, int role_id, int user_id, boolean isDone) {

		addSubscription(todoService
				.addTodoList(content, todoOrder, todoDate, role_id, user_id, isDone)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Integer>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Integer integer) {
						view.setTodoListId(integer);
					}
				}));
	}


	@Override
	public void deleteTodo(int id) {
		addSubscription(todoService
				.deleteTodo(id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {
						unsubscribe();
					}

					@Override
					public void onError(Throwable e) {
						onError(e);
					}

					@Override
					public void onNext(Void aVoid) {
						onCompleted();
					}
				}));
	}

	@Override
	public void setDone(int todoId, boolean isDone) {
		addSubscription(todoService
				.setDone(todoId, isDone)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {
						view.refreshProgress();

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Void aVoid) {
						onCompleted();
					}
				}));

	}


}
