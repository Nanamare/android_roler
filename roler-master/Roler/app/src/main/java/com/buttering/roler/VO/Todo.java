package com.buttering.roler.VO;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ichaeeun on 2016. 7. 30..
 */
public class Todo implements Serializable {

	private int id;
	private String content;
	private int todoOrder;
	private String todoDate;
	private int isDone;
	private int role_id;
	private int user_id;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDone() {
		return isDone;
	}

	public void setDone(int done) {
		isDone = done;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getTodoDate() {
		return todoDate;
	}

	public void setTodoDate(String todoDate) {
		this.todoDate = todoDate;
	}

	public int getTodoOrder() {
		return todoOrder;
	}

	public void setTodoOrder(int todoOrder) {
		this.todoOrder = todoOrder;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public static Type getListType() {
		return new TypeToken<List<Todo>>() {
		}.getType();
	}
}
