package com.buttering.roler.VO;

/**
 * Created by ichaeeun on 2016. 7. 30..
 */
public class Todo {

    private int id;
    private String content;
    private int todoOrder;
    private String todoDate;
    private Boolean isDone;
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

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
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
}
