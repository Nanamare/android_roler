package com.buttering.roler.role;

import android.widget.Button;
import android.widget.EditText;

import com.buttering.roler.VO.Role;

import java.util.List;

import rx.Observable;

/**
 * Created by kinamare on 2017-01-13.
 */


public interface IRolePresenter {
	void getRoleContent();
	void addRole(int rolePrimary,String roleName,String roleContent,int user_id);
	void check_blank(Button activity_edit_primaryBtn, EditText activity_edit_roleTile
			,EditText activity_edit_roleSubTitle);
	void deleteRole(int role_id);
	void editRole(int rolePrimary,String roleName,String roleContent,int role_id);
}
