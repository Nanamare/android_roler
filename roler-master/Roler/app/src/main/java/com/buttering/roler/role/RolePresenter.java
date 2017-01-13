package com.buttering.roler.role;

import android.app.Activity;

import com.buttering.roler.VO.Role;
import com.buttering.roler.composition.basepresenter.BasePresenter;
import com.buttering.roler.composition.baseservice.RoleService;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-01-13.
 */

public class RolePresenter  extends BasePresenter implements IRolePresenter {

	private RoleService roleService;
	private Activity activity;
	private IRoleView view;


	public RolePresenter(Activity activity,IRoleView view){
		this.roleService = new RoleService();
		this.activity = activity;
		this.view = view;

	}



	@Override
	public void getRoleContent(int id) {
		view.showLoadingBar();

		roleService
				.getRoleContent(id)
				.subscribe(new Subscriber<List<Role>>() {
					@Override
					public void onCompleted() {
						view.hideLoadingBar();
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(List<Role> role) {
						view.setRoleContent(role);
						onCompleted();
					}
				});

	}
}
