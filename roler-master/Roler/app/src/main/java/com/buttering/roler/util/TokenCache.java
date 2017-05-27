package com.buttering.roler.util;

/**
 * Created by kinamare on 2017-05-27.
 * 아직 구현되다 말았습니다.
 */

public class TokenCache {

	//singletone
	private TokenCache(){}

	private static TokenCache mInstance;

	public static synchronized TokenCache getInstance(){
		if(mInstance != null){
			return mInstance;
		} else {
			mInstance = new TokenCache();
			return mInstance;
		}
	}
	/*
	* 15분 주기로 리프레쉬
	*/
	private static final long TIME_REFRESH = 1500;

	long currentTime;
	long oldestTime;

	void register(){

		currentTime = System.currentTimeMillis();
		oldestTime = currentTime;

	}

	void refreshToken(){

		// 토큰을 갱신한다.
		if ((currentTime - oldestTime) > TIME_REFRESH) {

		}
	}
}
