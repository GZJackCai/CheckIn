package com.checkin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.checkin.MainActivity;
import com.checkin.R;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;
import com.checkin.utils.WifiAdmin.WifiCipherType;

public class MyService extends Service {

	static int intCounter ;
	static boolean runFlag = true;
	static final int DELAY = 10000; // ˢ��Ƶ��5����
	static int noSignCounter ;
	final String UPDATE_ACTION = "com.checkin.updateui";// ����ǰ̨UI
	String tag = "wifi service";
	public static boolean isCheck  = false;
	ScanTask task;
	Intent in;

	// �����������źŷ���ֵ�����°������Ӧ�ź�
	Handler hd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Log.i(tag, "�����״�ǩ���ɹ�֪ͨ");
				showNotifArri(MyService.this);
				in =new Intent();
				in.setAction(UPDATE_ACTION);
				in.putExtra("state", 0);
				sendBroadcast(in);
				break;
			case 1:
				Log.i(tag, "�����뿪֪֪ͨͨ");
				showNotifLeav(MyService.this);
				in =new Intent();
				in.setAction(UPDATE_ACTION);
				in.putExtra("state", 1);
				sendBroadcast(in);
				break;

			}
		}
	};

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		
		super.onStart(intent, startId);
		Log.i(tag, "onStart");
		noSignCounter = intCounter = 0;
		isCheck = false;
		runFlag = true;
		task = new ScanTask(this);
		task.start();
	}

	@Override
	public void onCreate() {

		super.onCreate();
		Log.i(tag, "��������");
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(tag, "onBind()");
		return null;
	}

	@Override
	public void onDestroy() {

		// TODO Auto-generated method stub
		Log.i(tag,"onDestroy");
		hd.removeCallbacks(task);
		runFlag = false;
		super.onDestroy();

	}

	public class ScanTask extends Thread {

		private SocketUtil connect;
		private PreferGeter geter;
		private String ip;
		private String username, password, workcode;
		WifiCipherType TYPE;
		boolean get;

		public ScanTask(Context con) {
			geter = new PreferGeter(con);
			ip = geter.getIP();
			username = geter.getUnm();
			password = geter.getPwd();
			workcode = geter.getWcd();
		}

		public void run() {
			Looper.prepare();
			while (runFlag) {

				get = false;
				intCounter++;
				Log.i("CheckIn",
						"Service Counter:" + Integer.toString(intCounter));
				connect = new SocketUtil(ip);
				if (!connect.isConnected) {
					try {
						connect.connectServer();
						get = connect.sendCheck(username, password, workcode);
						connect.close();
					} catch (Exception e) {
						e.printStackTrace();
						Log.i(tag,"noSignCounter="+noSignCounter);
						noSignCounter++; // ����ʧ�ܴ���ͳ��
					}
				}

				if (!isCheck && get) { // �״�ǩ��
					isCheck = true;

					Message tempMessage = new Message();
					tempMessage.what = 0;
					MyService.this.hd.sendMessage(tempMessage);
					

				} else if (isCheck && !get) { // �뿪
					isCheck = false;
					Message tempMessage = new Message();
					tempMessage.what = 1;
					MyService.this.hd.sendMessage(tempMessage);
				}

				// ����5������Ӧ�����������
				if (noSignCounter >= 5) {
				
					Log.i(tag,"��������ָ��");
					MyService.this.stopSelf();
				}

				try {
					Log.i(tag, "Thread sleep");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.i(tag, "InterruptedException runflag = false");
					//MyService.runFlag = false;
				}
			}

		}
	}

	public void showNotifArri(Context context) {

		String title;
		String content;

		// ��Ϣ֪ͨ�� // ����NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// ����֪ͨ��չ�ֵ�������Ϣ

		long when = System.currentTimeMillis();

		title = "ע��ɹ�";
		content = "���ѳɹ�ע�ᵽ��˾";

		Notification notification = new Notification.Builder(context)
				.setContentTitle(title).setContentText(content)
				.setSmallIcon(R.drawable.ic_launcher).build();

		// ��������֪ͨ��ʱҪչ�ֵ�������Ϣ
		CharSequence contentTitle = "ע��ɹ�";
		CharSequence contentText = "���ѳɹ�ע�ᵽ��˾";
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// ��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ
		mNotificationManager.notify(1, notification);

	}
	
	public void showNotifLeav(Context context) {

		String title;
		String content;

		// ��Ϣ֪ͨ�� // ����NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// ����֪ͨ��չ�ֵ�������Ϣ

		long when = System.currentTimeMillis();

		title = "�뿪";
		content = "�����뿪��˾";

		Notification notification = new Notification.Builder(context)
				.setContentTitle(title).setContentText(content)
				.setSmallIcon(R.drawable.ic_launcher).build();

		// ��������֪ͨ��ʱҪչ�ֵ�������Ϣ
		CharSequence contentTitle = "�뿪";
		CharSequence contentText = "�����뿪��˾";
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// ��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ
		mNotificationManager.notify(1, notification);

	}

}