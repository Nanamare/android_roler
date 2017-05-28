package com.buttering.roler.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kinamare on 2017-05-27.
 */

public class TokenAlarmReceive extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		Intent mServiceIntent = new Intent(context, TokenRefreshService.class);
		context.startService(mServiceIntent);
	}

}
