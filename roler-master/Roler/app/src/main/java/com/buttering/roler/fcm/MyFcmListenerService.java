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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.buttering.roler.R;
import com.buttering.roler.plan.PlanActivity;
import com.buttering.roler.timetable.BaseActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFcmListenerService extends FirebaseMessagingService {
	private static final String TAG = "FirebaseMsgService";

	private List<String> list = new ArrayList<>();
	String longText;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {

		Map data = remoteMessage.getData();

		String[] datas = (String[]) data.values().toArray(new String[]{});
		String title = remoteMessage.getNotification().getTitle();
		int badgeCount = 1;
		String pushMessage = datas[1];

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(pushMessage);
		JsonArray jsonArray = element.getAsJsonArray();
		for (int loop = 0; loop < jsonArray.size(); loop++) {
			list.add(jsonArray.get(loop).getAsJsonObject().get("content").toString());
		}

		sendBadgeUpdateIntent(badgeCount);
		sendNotification(title, list);

	}

	private void sendNotification(String title, List<String> list) {

		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,PlanActivity.class),0);

		Notification.Builder mBuilder = new Notification.Builder(this);
		mBuilder.setSmallIcon(R.drawable.icon_roler);
		mBuilder.setTicker("오늘의 스케쥴 알림이 왔습니다");
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setContentTitle(title);
		mBuilder.setContentText("오늘의 일정");
		mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setAutoCancel(true);

		Notification.InboxStyle style = new Notification.InboxStyle(mBuilder);
		if(list.size()!=0) {
			for (int i = 0; i < list.size(); i++) {
				style.addLine(list.get(i));
			}
		} else {
			style.addLine("없네요 오늘의 스케쥴을 설정해보세요.");
		}
		style.setSummaryText("더보기");
		mBuilder.setStyle(style);

		nm.notify(555,mBuilder.build());

	}

	private void sendBadgeUpdateIntent(int badgeCount) {
		Intent intent = new Intent("badgeCount");
		intent.putExtra("badgeCount", badgeCount);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}


}