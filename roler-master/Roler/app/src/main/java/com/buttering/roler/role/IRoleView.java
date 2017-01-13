package com.buttering.roler.role;

import com.buttering.roler.VO.Role;

import java.util.List;

/**
 * Created by kinamare on 2017-01-13.
 */

public interface IRoleView {
	void setRoleContent(List<Role> roleList);
	void showLoadingBar();
	void hideLoadingBar();
}
