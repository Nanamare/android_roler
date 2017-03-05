package com.buttering.roler.plan;

/**
 * Created by kinamare on 2016-12-31.
 */

public interface IPlanPresenter {
	void loadToList(int roleId);
	void getRoleContent();
	void addTodo(String content,int todoOrder
			,String todoDate,int role_id,boolean isDone);
	void deleteTodo(int id);
	void setDone(int todoId,boolean isDone);
	void updateRoleContent(int movePosition);
	void updateProgress(int role_id);
}
