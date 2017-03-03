package com.buttering.roler.role;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.buttering.roler.R;
import com.buttering.roler.VO.Role;
import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.RoleService;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.buttering.roler.R.id.activity_edit_role_btn;

/**
 * Created by kinamare on 2017-01-13.
 */

public class RolePresenter extends BasePresenter implements IRolePresenter {

	private RoleService roleService;
	private IRoleView view;
	private Activity activity;
	private boolean isClick;
	private boolean isTitle;
	private boolean isSubTitle;

	public RolePresenter(IRoleView view, Activity activity) {
		this.activity = activity;
		this.roleService = new RoleService();
		this.view = view;

	}


	public RolePresenter(Activity activity) {
		this.activity = activity;
		this.roleService = new RoleService();

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
						onCompleted();

					}
				}));

	}

	@Override
	public void addRole(int rolePrimary, String roleName, String roleContent, int user_id) {
		addSubscription(roleService
				.addRole(rolePrimary, roleName, roleContent, user_id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Void>() {
					@Override
					public void onCompleted() {
						unsubscribe();
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Void aVoid) {
						onCompleted();
					}
				}));
	}

	@Override
	public void check_blank(Button activity_edit_primaryBtn, EditText activity_edit_roleTitle, EditText activity_edit_roleSubTitle) {

//		button = RxView.clicks(activity_edit_primaryBtn)
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(
//				aVoid -> {
//					isClick = true;
//					if (isClick && isTitle && isSubTitle) {
//						activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.colorPrimary);
//					}
//				}
//		);

		Observable<CharSequence> primaryBtn = RxTextView.textChanges(activity_edit_primaryBtn);
		primaryBtn.map(charSequence -> charSequence.length() > 0).subscribe(aBoolean -> {
			isClick = aBoolean;
			if (isClick && isTitle && isSubTitle) {
				activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.colorPrimary);

			} else {
				activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.soft_grey);
			}
		});


		Observable<CharSequence> roleTitle = RxTextView.textChanges(activity_edit_roleTitle);
		roleTitle.map(charSequence -> charSequence.length() > 0).subscribe(aBoolean -> {
			isTitle = aBoolean;
			if (isClick && isTitle && isSubTitle) {
				activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.colorPrimary);

			} else {
				activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.soft_grey);
			}
		});

		Observable<CharSequence> roleSubTitle = RxTextView.textChanges(activity_edit_roleSubTitle);
		roleSubTitle.map(charSequence -> charSequence.length() > 0).subscribe(aBoolean -> {
			isSubTitle = aBoolean;
			if (isClick && isTitle && isSubTitle) {
				activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.colorPrimary);

			} else {
				activity.findViewById(activity_edit_role_btn).setBackgroundResource(R.color.soft_grey);
			}

		});


	}

	@Override
	public void deleteRole(int role_id) {
		addSubscription(
				roleService
						.deleteRole(role_id)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Subscriber<Void>() {
							@Override
							public void onCompleted() {
								view.refreshRoleContent();
							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(Void aVoid) {

							}
						})
		);
	}

	@Override
	public void editRole(int rolePrimary, String roleName, String roleContent, int user_id) {
		addSubscription(
				roleService
						.editRole(rolePrimary, roleName, roleContent, user_id)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Subscriber<Void>() {
							@Override
							public void onCompleted() {
								view.refreshRoleContent();
							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(Void aVoid) {

							}
						})
		);
	}

}
