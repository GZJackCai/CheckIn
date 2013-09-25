package com.checkin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

/**
 * ����״̬�ı�ʱ���չ㲥������������
 * @author Administrator
 *
 */
public class NetworkChangeReceiver extends BroadcastReceiver {


	public void onReceive(Context ctx, Intent intent) {

		if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {// �������wifi������״̬
			//Log.i("receiver","NETWORK_STATE_CHANGED_ACTION");
			Parcelable parcelableExtra = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (null != parcelableExtra) {
				NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
				State state = networkInfo.getState();
				if (state == State.CONNECTED) { // ��ʾwifi״̬�ı�
					Log.i("receiver", "wifi״̬�ı�");
					// start service
					Intent s = new Intent(ctx, MyService.class);
					ctx.startService(s);
				}

			}
			
		}

	

	}

}
