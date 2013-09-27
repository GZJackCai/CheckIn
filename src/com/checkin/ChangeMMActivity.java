package com.checkin;

import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeMMActivity extends Activity {

	EditText edt_username, edt_new_pass, edt_old_pass,edt_new_pass2;
	Button bt_action;
	ProgressDialog pd;
	private SocketUtil connect;
	private String username, new_password, old_password,new_password2 ,ip;

	public void onCreate(Bundle bd) {
		super.onCreate(bd);
		setContentView(R.layout.activity_changemm);
		edt_username = (EditText) findViewById(R.id.username);
		edt_new_pass = (EditText) findViewById(R.id.new_psw);
		edt_new_pass2 = (EditText) findViewById(R.id.new_psw2);
		edt_old_pass = (EditText) findViewById(R.id.old_psw);
		bt_action = (Button) findViewById(R.id.action);

		bt_action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new_password = edt_new_pass.getText().toString();
				new_password2 = edt_new_pass2.getText().toString();
				old_password = edt_old_pass.getText().toString();
				if (new_password.trim().length() == 0|new_password2.trim().length() == 0
						| old_password.trim().length() == 0) {
					Toast.makeText(ChangeMMActivity.this, "�������Ϊ��^^",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(new_password.equals(old_password)){
					Toast.makeText(ChangeMMActivity.this, "������������벻����ͬ",
							Toast.LENGTH_SHORT).show();
					edt_old_pass.setText("");
					edt_new_pass.setText("");
					edt_new_pass2.setText("");
					return;
				}
				
				if(!new_password.equals(new_password2)){
					Toast.makeText(ChangeMMActivity.this, "�����������벻һ��",
							Toast.LENGTH_SHORT).show();
					edt_new_pass.setText("");
					edt_new_pass2.setText("");
					return;
				}
				new ChangeTask().execute((Void) null);
			}

		});
	}

	public void onResume() {
		super.onResume();
		username = ip = new PreferGeter(this).getUnm();
		edt_username.setText(username);
	}

	/**
	 * ִ��ע��ĺ�̨����
	 * 
	 * @author Administrator
	 * 
	 */
	public class ChangeTask extends AsyncTask<Void, Void, Integer> {

		// Ԥִ��
		protected void onPreExecute() {

			initProgressDialog();
			pd.show();
			ip = new PreferGeter(ChangeMMActivity.this).getIP();

		}

		// ��̨�߳�
		@Override
		protected Integer doInBackground(Void... params) {

			Looper.prepare();
			connect = new SocketUtil(ip);
			if (!connect.isConnected) {
				try {
					connect.connectServer();
				} catch (Exception e) {
					
					return 0;
				}
			}
			boolean isSuccess = connect.changeMM(username, old_password, new_password);// ע���û�
			connect.close();
			if (isSuccess)
				return 1;
			else
				return 2;

		}

		// �������
		@Override
		protected void onPostExecute(final Integer result) {

			pd.cancel();
			switch (result) {
			case 0:
				Toast.makeText(ChangeMMActivity.this, "���ӷ�����ʧ�ܣ���������״��..",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(ChangeMMActivity.this,
						"�����޸ĳɹ���", Toast.LENGTH_LONG).show();

				// �����Ի���
				new AlertDialog.Builder(ChangeMMActivity.this)
						.setCancelable(false)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("�����޸ĳɹ���")
						.setMessage("���ѳɹ��޸��û� " + username + " ������")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ChangeMMActivity.this.finish();
										startActivity(new Intent(
												ChangeMMActivity.this,
												MainActivity.class));
									}
								}).show();
				break;
			case 2:
				Toast.makeText(ChangeMMActivity.this, "�����������û�������",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

		// ȡ��
		@Override
		protected void onCancelled() {
			System.out.println("onCancelled");
			pd.cancel();
		}
	}

	/**
	 * ��ʼ��������
	 */
	private void initProgressDialog() {

		pd = new ProgressDialog(this);// ����ProgressDialog����
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���ý�������񣬷��ΪԲ�Σ���ת��
		pd.setTitle("�޸�����");// ����ProgressDialog ����
		pd.setMessage("������...");// ����ProgressDialog��ʾ��Ϣ
		/*
		 * pd.setIcon(R.drawable.ic_la);// ����ProgressDialog����ͼ�� //
		 * ����ProgressDialog
		 */// �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ
		pd.setIndeterminate(false);
		pd.setCancelable(true); // ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��
	}

}
