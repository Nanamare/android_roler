package com.buttering.roler.api;


import android.support.test.runner.AndroidJUnit4;

import com.buttering.roler.VO.Role;
import com.buttering.roler.apimanager.MockRoleService;
import com.buttering.roler.net.baseservice.RoleService;
import com.buttering.roler.net.baseservice.lazymock.LazyMockInterceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by kinamare on 2017-03-06.
 */
@RunWith(AndroidJUnit4.class)
public class RoleServie_Test {

	List<Role> roleList = null;
	MockRoleService roleService = null;

	public static final LazyMockInterceptor mockInterceptor = new LazyMockInterceptor();

	@Before
	public void setup() {

		roleList = new ArrayList<>();
		roleService = new MockRoleService();

		mockInterceptor.setResponse(null);

		RxJavaPlugins.getInstance().reset();
		RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
			@Override
			public Scheduler getComputationScheduler() {
				return Schedulers.immediate();
			}
		});
	}

	@Test
	public void role_역할_불러오기_Test() {

		roleService.getRoleContent()
				.subscribe(new Subscriber<List<Role>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(List<Role> roles) {
						roleList = roles;
					}
				});


		assertNotNull(roleList);

		for (Role role : roleList) {
			assertNotNull(role.getId());
			assertTrue(role.getId() > 0);
			assertNotNull(role.getRole_id());
			assertTrue(role.getRole_id() > 0);
			assertNotNull(role.getRoleContent());
			assertNotNull(role.getRolePrimary());
			assertTrue(role.getRolePrimary() > 0);
			assertNotNull(role.getRoleName());
			assertNotNull(role.getUser_id());
			assertTrue(role.getUser_id() > 0);
			assertNotNull(role.getProgress());
			assertTrue(role.getProgress() > 0);
		}

	}


}
