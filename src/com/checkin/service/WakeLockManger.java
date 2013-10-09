package com.checkin.service;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeLockManger {
	
	private Context mContext;
	// ��WakeLock
	private WakeLock mWakeLock;

	// ��������ʱacquire���˳�ʱrelease

	public WakeLockManger(Context mContext) {
		this.mContext = mContext;
	}

	// �����豸��Դ��
	public void acquireWakeLock() {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) mContext
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "com.checkin");
			if (null != mWakeLock) {
				mWakeLock.acquire();
			}
		}
	}

	// �ͷ��豸��Դ��
	public void releaseWakeLock() {
		if (null != mWakeLock) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

}
