package com.buttering.roler.VO;

import android.os.Build;

/**
 * Created by kinamare on 2016-12-29.
 */

public class Device {

	private int id;
	private String uuid;
	private String type;
	private String token;
	private String ipAddress;
	private String osVersion;
	private String osType;
	private String appVersion;
	private String appBuildVersion;
	private String locale;
	private String memo;
	private boolean isBlocked;


	public Device() {
		this.osType = "android";
		this.osVersion = Build.VERSION.CODENAME;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
