package com.checkin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.checkin.MainActivity;
import com.checkin.R;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;
import com.checkin.utils.WifiAdmin;
import com.checkin.utils.WifiAdmin.WifiCipherType;

public class MyService extends Service {

	static int intCounter = 0;
	static boolean runFlag = true;
	static final int DELAY = 10000; // ˢ��Ƶ��
	final String UPDATE_ACTION = "com.checkin.updateui";// ����ǰ̨UI
	// String str_compSSID = "Connectify-me"; // ��˾��wifi��
	// String str_compKEY = "dianxin1212"; // ��˾��wifi���� ����������Ϊnull
	String tag = "wifi service";

	// �����������źŷ���ֵ�����°������Ӧ�ź�

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {

		super.onCreate();
		Log.i(tag, "��������");
		new ScanTask(this).start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(tag, "onBind()");
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		// objHandler.removeCallbacks(mTasks);
		super.onDestroy();
	}

	public class ScanTask extends Thread {

		private Context context;
		private WifiAdmin mwifiAdmin;
		private SocketUtil connect;
		private PreferGeter geter;

		public ScanTask(Context con) {
			this.context = con;
			geter = new PreferGeter(con);
		}

		public void run() {
			while (runFlag) {

				intCounter++;
				Log.i("CheckIn",
						"Service Counter:" + Integer.toString(intCounter));
				mwifiAdmin = new WifiAdmin(this.context);
				if (mwifiAdmin == null)
					Log.i(tag, "mwifiAdmin is null");
				if (isSendSignal()) { // �����źţ��ϰ�
					connect = new SocketUtil(this.context);
					if (!connect.isConnected) {
						try {
							connect.connectServer();
						} catch (Exception e) {
							Toast.makeText(this.context, "���ӷ�����ʧ�ܣ���������״��..",
									Toast.LENGTH_LONG).show();
							return;
						}
					}
					boolean isCheck = connect.sendCheck();
					connect.close();

				} else {
					// ����Ӧ�źţ���ԭ��״̬������
					break;
				}
				try {
					Log.i(tag, "Thread sleep");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.i(tag, "InterruptedException runflag = false");
					MyService.runFlag = false;
				}
			}

		}

		private boolean isSendSignal() {
			WifiCipherType TYPE;
			if (geter.getType() == 1) {
				TYPE = WifiCipherType.WIFICIPHER_WPA;
			} else if (geter.getType() == 2) {
				TYPE = WifiCipherType.WIFICIPHER_WEP;
			} else {
				TYPE = WifiCipherType.WIFICIPHER_NOPASS;
			}
			String SSID = geter.getSSID();
			String password = geter.getWifiPassword();

			mwifiAdmin.openWifi();
			while (true) {
				if (mwifiAdmin.getSSID().equals(SSID)) {
					return true;
				} else {
					if (mwifiAdmin.isCanScan(SSID)) { // �ж��Ƿ��ܹ�ɨ��õ�
						Log.i(tag, "�ܹ�ɨ�赽");
						// ����ǰ��������ĳ��wifi���ȶϿ�
						if (mwifiAdmin.getSSID() != null) {
							mwifiAdmin
									.disconnectWifi(mwifiAdmin.getNetworkId());
						}
						mwifiAdmin.Connect(SSID, password, TYPE);
						try {
							Thread.currentThread();
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;

					} else {
						
						Log.i(tag, "�޷�ɨ�赽");
						return false;
					}
				}
			}
		}

	}

	/*public void showNotif(Context context) {
		// TODO Auto-generated method stub

		String title;
		String content;

		// ��Ϣ֪ͨ��
		// ����NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		// ����֪ͨ��չ�ֵ�������Ϣ

		long when = System.currentTimeMillis();

		title = "��ע��ɹ�";
		content = "���ѳɹ�ע�ᵽ��˾";

		Notification notification = new Notification.Builder(context)
				.setContentTitle(title).setContentText(content)
				.setSmallIcon(R.drawable.ic_launcher).build();

		// ��������֪ͨ��ʱҪչ�ֵ�������Ϣ
		CharSequence contentTitle = "�ҵ�֪ͨ����չ������";
		CharSequence contentText = "�ҵ�֪ͨ��չ����ϸ����";
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// ��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ
		mNotificationManager.notify(1, notification);

	}*/
}