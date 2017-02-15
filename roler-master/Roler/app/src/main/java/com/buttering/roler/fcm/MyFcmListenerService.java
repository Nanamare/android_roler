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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.buttering.roler.R;
import com.buttering.roler.plan.PlanActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFcmListenerService extends FirebaseMessagingService {
	private static final String TAG = "FirebaseMsgService";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage){
		sendNotification(remoteMessage.getData().get("message"));
	}

	private void sendNotification (String messageBody){
		Intent intent = new Intent(this, PlanActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

		Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.icon_roler)
				.setContentTitle("푸시 테스트")
				.setContentText(messageBody)
				.setSound(defaultUri)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager =
				(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0,notificationBuilder.build());

	}


}