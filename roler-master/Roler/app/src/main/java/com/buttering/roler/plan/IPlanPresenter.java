package com.buttering.roler.plan;

import com.buttering.roler.VO.Role;

import java.util.List;
import rx.Observable;

/**
 * Created by kinamare on 2016-12-31.
 */

public interface IPlanPresenter {
	void loadToList(int userId, int roleId);
	void getRoleContent(int id);
}
