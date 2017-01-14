package com.buttering.roler.plan;

import com.buttering.roler.VO.Todo;
import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.TodoService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class PlanPresenter extends BasePresenter implements IPlanPresenter {

	private TodoService todoService;
	private IPlanView view;

	public PlanPresenter(IPlanView view) {
		todoService = new TodoService();
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
}
