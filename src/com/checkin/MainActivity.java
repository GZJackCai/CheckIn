package com.checkin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.checkin.service.MyService;
import com.checkin.service.UpdateUIReceiver;

public class MainActivity extends Activity {

	TextView tv;
	Button start_btn, stop_btn;
	UpdateUIReceiver updateuiRec;
	final String UPDATE_ACTION = "com.checkin.updateui";
	Intent s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		s = new Intent(this, MyService.class);

		tv = (TextView) findViewById(R.id.showstate);
		start_btn = (Button) findViewById(R.id.start);
		stop_btn = (Button) findViewById(R.id.stop);
		
		if(MyService.isCheck){
			tv.setText("��ע���ϰ�״̬");
		}else{
			tv.setText("δע���ϰ�״̬");
		}
		updateuiRec = new UpdateUIReceiver(tv);
		IntentFilter inf = new IntentFilter();
		inf.addAction(UPDATE_ACTION);
		this.registerReceiver(updateuiRec, inf);

		start_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.startService(s);
				Toast.makeText(getApplicationContext(), "��ʼ����", Toast.LENGTH_SHORT).show();
			}

		});

		stop_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.stopService(s);
				Toast.makeText(getApplicationContext(), "ֹͣ����", Toast.LENGTH_SHORT).show();
			}

		});

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
		case R.id.item1:

			startActivity(new Intent(this, Setting.class));
			break;

		case R.id.item2:

			startActivity(new Intent(this, RegistActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

}
