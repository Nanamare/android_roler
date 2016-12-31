package com.buttering.roler.composition.baseservice;

import com.buttering.roler.VO.MyInfoDAO;

/**
 * Created by kinamare on 2016-12-29.
 */
public class RolerRequest {


	private int deviceId;
	private String action;
	private String param;
	private String userId;

	private RolerRequest(Builder builder) {
		this.deviceId = builder.deviceId;
		this.action = builder.action;
		this.param = builder.param;
		this.userId = builder.userId;
	}



	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserIdT() {
		return userId;
	}

	public void setUserIdT(String userId) {
		this.userId = userId;
	}

	public static class Builder {
		private int userinfoId;
		private int deviceId;
		private String action;
		private String param;
		private String userId;

		public Builder() {
			this.userId = MyInfoDAO.getInstance().getMyUserInfo().getId();
			this.deviceId = MyInfoDAO.getInstance().getDeviceId();
		}

		public Builder setParam(String param) {
			this.param = param;
			return this;
		}

		public Builder setAction(String action) {
			this.action = action;
			return this;
		}

		public RolerRequest build() {
			return new RolerRequest(this);
		}


	}


}
