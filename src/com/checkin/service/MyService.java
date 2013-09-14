package com.checkin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.checkin.MainActivity;
import com.checkin.R;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;
import com.checkin.utils.WifiAdmin;
import com.checkin.utils.WifiAdmin.WifiCipherType;

public class MyService extends Service {

	static int intCounter = 0;
	static boolean runFlag = true;
	static final int DELAY = 100000; // ˢ��Ƶ��
	final String UPDATE_ACTION = "com.checkin.updateui";// ����ǰ̨UI
	String str_compSSID = "Connectify-me"; // ��˾��wifi��
	String str_compKEY = "dianxin1212"; // ��˾��wifi���� ����������Ϊnull

	// �����������źŷ���ֵ�����°������Ӧ�ź�

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {

		super.onCreate();
		Log.i("test", "��������");
		new ScanTask(this).start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("test", "onBind()");
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
		private SocketUtil mynetcon;
		private PreferGeter geter;

		public ScanTask(Context con) {
			this.context = con;
			mynetcon = new SocketUtil(con);
			geter = new PreferGeter(con);
		}

		public void run() {
			while (runFlag) {

				intCounter++;
				Log.i("CheckIn",
						"Service Counter:" + Integer.toString(intCounter));
				mwifiAdmin = new WifiAdmin(this.context);
				if (mwifiAdmin == null)
					Log.i("test", "mwifiAdmin is null");
				if (isSendSignal()) { // �����źţ��ϰ�

					mynetcon.sendMSG("check;" + geter.getUsername() + ";");

				} else {
					// ����Ӧ�źţ���ԭ��״̬������
					Log.i("test", "���跢��");
					break;

				}
				try {
					Log.i("test", "Thread sleep");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.i("test", "InterruptedException runflag = false");
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

			mwifiAdmin.openWifi();
			while (true) {
				if (mwifiAdmin.getSSID().equalsIgnoreCase(str_compSSID)) {
					return true;
				} else {
					if (mwifiAdmin.isCanScan(str_compSSID)) { // �ж��Ƿ��ܹ�ɨ��õ�
						Log.i("test", "�ܹ�ɨ�赽");
						// ����ǰ��������ĳ��wifi���ȶϿ�
						if (mwifiAdmin.getSSID() != null) {
							mwifiAdmin
									.disconnectWifi(mwifiAdmin.getNetworkId());
							mwifiAdmin.Connect(geter.getSSID(),
									geter.getWifiPassword(), TYPE);
							try {
								Thread.currentThread();
								Thread.sleep(8000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							continue;
						}
					} else {
						return false;
					}
				}
			}
		}

	}

	public void showNotif(Context context) {
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

	}
}