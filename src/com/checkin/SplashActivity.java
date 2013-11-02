package com.checkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.checkin.utils.PreferGeter;

/**
 * �������棬����������
 * δע���¼��������ע����棬�ѵ�¼�Ľ���������
 * @author Administrator
 *
 */

public class SplashActivity extends Activity{
	
	private final int SPLASH_DISPLAY_LENGHT = 1000; // �ӳ�2����ת
	protected Intent intent =new Intent();
	PreferGeter geter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		geter = new PreferGeter(this);
		
		// �ж��Ƿ��ѵ�½������ת����¼ע�����
		if (geter.getUnm().equalsIgnoreCase("NULL")) {		
			intent.setClass(getApplicationContext(), LoginActivity.class);
		}
		else{
			intent.setClass(getApplicationContext(), MainActivity.class);
		}
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				SplashActivity.this.finish();
				SplashActivity.this.startActivity(intent);
				
			}

		}, SPLASH_DISPLAY_LENGHT);
		
	}

}
