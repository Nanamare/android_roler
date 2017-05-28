package com.vocketlist.android.network.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import com.vocketlist.android.roboguice.log.Ln;

import java.util.ArrayList;

public final class NetworkState {
    public static final int MAX_LEVELS = 10;
	private static final String NETWORK_STATE_PREF = "NETWORK_STATE_PREF";
	private static final String KEY_ROAMING_STATE = "KEY_ROMING_STATE";

	private volatile static NetworkState instance = null;

	public static void init(Context context) {
		synchronized (NetworkState.class) {
			if (instance == null) {
				instance = new NetworkState(context);
			}
		}
	}

	public static NetworkState getInstance() {
		return instance;
	}

	public static void destroy() {
		if (instance == null) {
			return;
		}

		instance.unregisterConnectivityReceiver();
		instance.clearWifiStateChangedListener();
		instance = null;
	}

	public interface INetworkStateChangedListener {
		public void onNetworkStateChanged();
	};

	private Context context;
	private BroadcastReceiver connectivityReceiver = null;
	private ArrayList<INetworkStateChangedListener> listeners;
	private Handler handler;
	private Runnable networkChangedRunnable;
	private ConnectivityManager connMan;
	private PhoneStateListener phoneStateListener = null;

	private NetworkState(Context context) {
		this.context = context;
		handler = new Handler();
		listeners = new ArrayList<INetworkStateChangedListener>();

		networkChangedRunnable = new Runnable() {
			@Override
			public void run() {
				if (listeners == null) {
					return;
				}
				for (INetworkStateChangedListener listener : listeners) {
					listener.onNetworkStateChanged();
				}
			}
		};

		connectivityReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
					handleNetworkChanged();
				}
			}
		};
		registerConnectivityReceiver();
	}

	private void handleNetworkChanged() {
		handler.post(networkChangedRunnable);
	}

	/**
	 * Network 연결 상태 변경될 때, BroadCast Event 를 받을 수 있도록 Action 을 등록한다.  
	 */
	private void registerConnectivityReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(connectivityReceiver, intentFilter);
	}

	/**
	 * Network 연결 상태 BroadCast Event 를 받을 필요 없을 때, 등록 되었던 Action 을 해제한다.
	 */
	private void unregisterConnectivityReceiver() {
		try {
			context.unregisterReceiver(connectivityReceiver);
		} catch (Exception e) {
			Ln.w("unregisterForWifiBroadcasts - exception.");
		}
	}

	public boolean addWifiStateChangedListener(INetworkStateChangedListener listener) {
		if (listeners.contains(listener)) {
			return true;
		}
		return listeners.add(listener);
	}

	public boolean removeWifiStateChangedListener(INetworkStateChangedListener listener) {
		if (listeners.size() == 0) {
			return false;
		}
		return listeners.remove(listener);
	}

	private void clearWifiStateChangedListener() {
		listeners.clear();
	}

	/**
	 * WIFI 연결 상태 인지 알려준다.
	 * @return WIFI 연결 상태 (true : 연결됨, false : 연결되지 않음)
	 */
	public boolean isWifiConnected() {
		try {
			if (connMan == null) {
				connMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			}

			NetworkInfo netInfo = connMan.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected() && (netInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
				return true;
			}
		} catch (Exception e) {
			Ln.e("Exception during isWifiConnected(). - " + e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * WIFI 활성화 상태 인지 알려준다.
	 * @return WIFI 활성화 상태 (true : 활성화 됨, false : 활성화 되지 않음)
	 */
	public boolean isWifiEnabled() {
		try {
			WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			return wm.isWifiEnabled();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Mobile 3G Network 연결 상태 인지 알려준다.
	 * @return Mobile 3G Network 연결 상태 (true : 연결됨, false : 연결되지 않음)
	 */
	public boolean isMobileConnected() {
		try {
			if (connMan == null) {
				connMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			}
			NetworkInfo mobileNetworkInfo = connMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileNetworkInfo == null) {
				Ln.d("mobileNetworkInfo is null.");
				return false;
			}

			return mobileNetworkInfo.isConnected();
		} catch (Exception e) {
			Ln.e("Exception during isMobileConnected(). - " + e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * Mobile 3G Network or Wifi 연결 상태 인지 알려준다.
	 * @return Mobile 3G Network or Wifi 연결 상태 (true : 연결됨, false : 연결되지 않음)
	 */
	public boolean isNetworkConnected() {
		try {
			if (connMan == null) {
				connMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			}

			NetworkInfo netInfo = connMan.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				return true;
			}

			return false;
		} catch (Exception e) {
			Ln.e("Exception during isNetworkConnected(). - " + e.getLocalizedMessage());
		}
		return false;
	}

    /**
     * 현재 연결된 wifi의 이름을 가져온다.
     *
     * @return
     */
    public String getConnectedWifiNetowkName() {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ssidName = null;

        if (manager.isWifiEnabled() == false) {
            return null;
        }

        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }

        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
        if (state == NetworkInfo.DetailedState.CONNECTED) {
            ssidName =  wifiInfo.getSSID();
        }

        Ln.d("networkInfo : " + ssidName);
        return ssidName;
    }

    /**
     * 현재 연결된 wifi의 신호 세기 값을 가져온다.
     * @return
     */
    public int getConntectedWifiSignalLevel() {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int signalLevel = 0;

        if (manager.isWifiEnabled() == false) {
            return signalLevel;
        }

        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo == null) {
            return signalLevel;
        }

        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
        if (state == NetworkInfo.DetailedState.CONNECTED ) {
            signalLevel =  WifiManager.calculateSignalLevel(wifiInfo.getRssi(), MAX_LEVELS);
        }

        Ln.d("wifiInfo.getRssi : " + signalLevel);
        return signalLevel;
    }

	public String getProxyHost() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return android.net.Proxy.getHost(context);
		}

		return System.getProperty("http.proxyHost");
	}

	public String getProxyPort() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return "" + android.net.Proxy.getPort(context);
		}

		return System.getProperty("http.proxyPort");
	}

	/**
	 * 비행기 모드 상태인지 알려준다.
	 * @return 비행기 모드 상태 여부 (true : 비행기 모드 상태, false : 그렇지 않음)
	 */
	public boolean isAirplaneModeOn() {
		try {
			return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * SIM 상태를 얻는다.
	 * @return SIM State (DEV Guide 참고, TelephonyManager) 
	 */
	public int getSimState() {
		try {
			TelephonyManager telMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			final int simState = telMgr.getSimState();
			return simState;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return TelephonyManager.SIM_STATE_UNKNOWN;
	}

	public void listenRoamingState() {
		if (phoneStateListener == null) {
			phoneStateListener = new PhoneStateListener() {
				@Override
				public void onServiceStateChanged(ServiceState serviceState) {
					super.onServiceStateChanged(serviceState);

					try {
						final int state = serviceState.getState();
						if (state == ServiceState.STATE_IN_SERVICE || state == ServiceState.STATE_POWER_OFF) {
							final boolean roamingState = serviceState.getRoaming();
							if (roamingState != isRoamingOn()) {
								setRoamingOn(roamingState);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}

		TelephonyManager telMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		telMgr.listen(phoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);
	}

	public boolean isRoamingOn() {
		try {
			SharedPreferences prefs = context.getSharedPreferences(NETWORK_STATE_PREF, 0);
			return prefs.getBoolean(KEY_ROAMING_STATE, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private void setRoamingOn(boolean bRoamingOn) {
		try {
			SharedPreferences.Editor prefs = context.getSharedPreferences(NETWORK_STATE_PREF, 0).edit();
			prefs.putBoolean(KEY_ROAMING_STATE, bRoamingOn);
			prefs.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
