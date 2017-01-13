package com.buttering.roler.VO;

import com.buttering.roler.R;
import com.buttering.roler.util.MyApplication;
import com.buttering.roler.util.SecurePreferences;
import com.buttering.roler.util.SharePrefUtil;
import com.google.gson.Gson;

/**
 * Created by kinamare on 2016-12-29.
 */

public class MyInfoDAO {


	private static MyInfoDAO instance;
	private final SecurePreferences preferences;

	private User myUserInfo;

	private MyInfoDAO() {
		String myUserInfoJson = SharePrefUtil.getSharedPreference("myUserInfo");

		myUserInfo = new Gson().fromJson(myUserInfoJson, User.class);

		preferences = new SecurePreferences(MyApplication.getInstance().getContext(),
				MyApplication.getInstance().getPackageName(),
				MyApplication.getInstance().getString(R.string.key_alias),
				true);

	}

	public static MyInfoDAO getInstance() {
		if (instance == null) {
			instance = new MyInfoDAO();
		}

		return instance;
	}

	public  void setUserId(String id){
		preferences.put("userId",id);
	}

	public void setPwd(String pwd) {
		preferences.put("pwd", pwd);
	}

	public void setName(String name) {
		preferences.put("name", name);
	}

	public void setEmail(String email) {
		preferences.put("email", email);
	}

	public void setPicUrl(String picUrl){
		preferences.put("picture_url",picUrl);
	}


	public int getDeviceId() {
		return 0;
	}


	public String getToken() {
		return SharePrefUtil.getSharedPreference("token");
	}

	public void saveToken(String token) {
		SharePrefUtil.putSharedPreference("token", token);
	}

	private void saveInPrefs(User userinfo) {
		String json = new Gson().toJson(userinfo);
		SharePrefUtil.putSharedPreference("myUserInfo", json);
	}

	public void saveAccountInfo(String userId, String email, String pwd, String name, String picURL) {
		setEmail(email);
		setPwd(pwd);
		setName(name);
		setPicUrl(picURL);
		setUserId(userId);
	}


	public void loginAccountInfo(String userId, String email, String name, String picURL) {
		setEmail(email);
		setName(name);
		setPicUrl(picURL);
		setUserId(userId);
	}

	public void saveUserInfo(User user) {
		setEmail(user.getEmail());
		setPwd(user.getPassword());
		setName(user.getName());
		setPicUrl(user.getPicture_url());
		setUserId(user.getId());
	}

	public String getEmail() {
		return preferences.getString("email");
	}

	public String getPwd() {
		return preferences.getString("pwd");
	}

	public String getNickName() {
		return preferences.getString("name");
	}

	public String getPicUrl(){
		return preferences.getString("picture_url");
	}

	public String getUserId(){
		return preferences.getString("userId");
	}


	public User getMyUserInfo() {
		if (myUserInfo == null) {
			myUserInfo = new User();
		}
		return myUserInfo;
	}

	public void setMyUserInfo(User userInfo) {
		this.myUserInfo = userInfo;
		saveInPrefs(myUserInfo);
		//save2Server(userInfo);
	}

	public void deleteAccountInfo() {
		preferences.removeValue("email");
		preferences.removeValue("pwd");
		preferences.clear();
	}


	public String getMyUserId() {
		return myUserInfo != null ? myUserInfo.getId() : "";
	}




	public String getThumbnailPath() {
		return myUserInfo.getPicture_url();
	}



}
