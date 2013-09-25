package com.checkin;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.checkin.service.MyService;
import com.checkin.service.UpdateUIReceiver;
import com.checkin.utils.PreferGeter;

/**
 * ������
 * @author Administrator
 *
 */
public class MainActivity extends Activity {

	TextView tv;
	TextView user;
	// Button start_btn, stop_btn;
	UpdateUIReceiver updateuiRec;
	final String UPDATE_ACTION = "com.checkin.updateui";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.showstate);
		user = (TextView) findViewById(R.id.user);
	
		// ע����µ�ǩ����Ϣ�Ĺ㲥
		updateuiRec = new UpdateUIReceiver(tv);
		IntentFilter inf = new IntentFilter();
		inf.addAction(UPDATE_ACTION);
		this.registerReceiver(updateuiRec, inf);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(updateuiRec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		/*
		 * case R.id.item1:
		 * 
		 * startActivity(new Intent(this, Setting.class)); break;
		 */

		case R.id.item1:

			startActivity(new Intent(this, RegistActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	public void onResume() {
		super.onResume();
		// �жϷ����Ƿ�������������������������Ҳ�������Ĵ���
		/*if(!isWorked()){
			System.out.println("���񲻴��ڣ�����");
			startService(new Intent(this,MyService.class));
		}*/
		user.setText(new PreferGeter(this).getUnm());
		if (MyService.isCheck) {
			tv.setText("��ע���ϰ�״̬");
			tv.setTextColor(R.color.green);
		} else {
			tv.setText("δע���ϰ�״̬");
			tv.setTextColor(R.color.red);
		}
	}

	/**
	 * �жϸ÷����Ƿ����
	 * ������������
	 * @return
	 */
	public boolean isWorked() {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
        		
        this.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList 
                   = activityManager.getRunningServices(30);

        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
        	System.out.println(serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().equals("com.checkin.service.MyService") == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
	

}
