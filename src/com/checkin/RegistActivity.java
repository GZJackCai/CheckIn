package com.checkin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.service.MyService;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

/**
 * ע�����
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends Activity {

	boolean regist_group_rs = false;

	private EditText regist_edt_account;
	private EditText regist_edt_wcd;
	private EditText regist_edt_pwd, regist_edt_pwd2;
	private Button regist_btn_regist;
	private Button regist_btn_clean;

	private String username, password, password2, workcode,ip;
	private SocketUtil connect;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		// �ؼ���ʼ��
		regist_edt_account = (EditText) this.findViewById(R.id.regist_account);
		regist_edt_wcd = (EditText) this.findViewById(R.id.regist_wcd);
		regist_edt_pwd = (EditText) this.findViewById(R.id.regist_psw);
		regist_edt_pwd2 = (EditText) this.findViewById(R.id.regist_psw2);
		regist_btn_regist = (Button) this.findViewById(R.id.regist_btn_account);
		regist_btn_clean = (Button) this.findViewById(R.id.regist_clean_table);

	}

	public void onResume() {

		super.onResume();
		// ע�ᰴť�ļ���
		regist_btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = regist_edt_account.getText().toString();
				workcode = regist_edt_wcd.getText().toString();
				password = regist_edt_pwd.getText().toString();
				password2 = regist_edt_pwd2.getText().toString();

				if (username.trim().length() == 0
						| workcode.trim().length() == 0
						| password.trim().length() == 0
						| password2.trim().length() == 0) {
					Toast.makeText(RegistActivity.this, "�������Ϊ��^^",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!password.equalsIgnoreCase(password2)) {
					Toast.makeText(RegistActivity.this, "�����������벻һ��",
							Toast.LENGTH_SHORT).show();
					regist_edt_pwd.setText("");
					regist_edt_pwd2.setText("");
					return;
				}
				new RegistTask().execute((Void) null);

			}

		});

		regist_btn_clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clean();
			}
		});

	}

	/**
	 * ���������
	 */
	private void clean() {
		regist_edt_account.setText("");
		regist_edt_wcd.setText("");
		regist_edt_pwd.setText("");
		regist_edt_pwd2.setText("");
	}

	

	/**
	 * ��ʼ��������
	 */
	private void initProgressDialog() {

		pd = new ProgressDialog(this);// ����ProgressDialog����
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���ý�������񣬷��ΪԲ�Σ���ת��
		pd.setTitle("ע��");// ����ProgressDialog ����
		pd.setMessage("������...");// ����ProgressDialog��ʾ��Ϣ
		/*
		 * pd.setIcon(R.drawable.ic_la);// ����ProgressDialog����ͼ�� //
		 * ����ProgressDialog
		 */// �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ
		pd.setIndeterminate(false);
		pd.setCancelable(true); // ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��
	}

	/**
	 * ִ��ע��ĺ�̨����
	 * 
	 * @author Administrator
	 * 
	 */
	public class RegistTask extends AsyncTask<Void, Void, Integer> {

		// Ԥִ��
		protected void onPreExecute() {

			initProgressDialog();
			pd.show();
			ip = new PreferGeter(RegistActivity.this).getIP();

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
			boolean isSuccess = connect.register(username, password, workcode);// ע���û�
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
				Toast.makeText(RegistActivity.this, "���ӷ�����ʧ�ܣ���������״��..",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(RegistActivity.this, "�ʻ�" + username + "ע��ɹ���",
						Toast.LENGTH_LONG).show();

				// �����Ի���
				new AlertDialog.Builder(RegistActivity.this)
						.setCancelable(false)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("ע��ɹ�")
						.setMessage(
								"���ѳɹ�ע���û���" + username + "\n" + "���ţ�"
										+ workcode)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										startActivity(new Intent(
												RegistActivity.this,
												LoginActivity.class));
									}
								}).show();
				break;
			case 2:
				Toast.makeText(RegistActivity.this, "�ʻ��Ѵ��ڣ�������ע��",
						Toast.LENGTH_LONG).show();
				clean();
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

}
