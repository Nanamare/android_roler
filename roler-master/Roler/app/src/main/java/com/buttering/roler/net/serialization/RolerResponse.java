package com.buttering.roler.net.serialization;

/**
 * Created by kinamare on 2016-12-17.
 */

public class RolerResponse {
	private String message;
	private String param;
	private int statusCode;


	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public String toString() {
		return param;
	}
}
