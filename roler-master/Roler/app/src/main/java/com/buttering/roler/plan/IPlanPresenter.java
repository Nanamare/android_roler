package com.buttering.roler.plan;

import android.content.Context;

import com.buttering.roler.VO.Role;
import com.buttering.roler.VO.Todo;

import java.util.List;
import rx.Observable;

/**
 * Created by kinamare on 2016-12-31.
 */

public interface IPlanPresenter {
	void loadToList(int userId, int roleId);
	void getRoleContent(int id);
	void addTodo(String content,int todoOrder
			,String todoDate,int role_id,int user_id,boolean isDone);
	void deleteTodo(int id);
	void setDone(int todoId,boolean isDone);
	void updateRoleContent(int id, int movePosition);
}
