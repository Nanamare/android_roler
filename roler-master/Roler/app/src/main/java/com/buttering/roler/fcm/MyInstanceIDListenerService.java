/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buttering.roler.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

	private static final String TAG = "MyFirebaseIDService";

	@Override
	public void onTokenRefresh() {

		String token = FirebaseInstanceId.getInstance().getToken();
		sendRegistrationToServer(token);

	}

	private void sendRegistrationToServer(String token) {

		OkHttpClient client = new OkHttpClient();
		RequestBody body = new FormBody.Builder()
				.add("Token",token)
				.build();

		Request request = new Request.Builder()
				.url("http://52.78.65.255:3000/fcm/register")
				.post(body)
				.build();

		try {
			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}