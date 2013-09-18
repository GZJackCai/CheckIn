package com.checkin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;

/**
 * socket������
 * @author linwei
 *
 */
public class SocketUtil {

	Socket socket;
	String IP;
	BufferedReader in;
	PrintWriter out;

	public boolean isConnected = false;	

	public SocketUtil(String ip) {

		this.IP = ip;
		
	}

	/**
	 * ������������
	 * @throws Exception
	 */
	public void connectServer() throws Exception {

		if (socket != null) {
			return;
		}
		try {
			// socket = new Socket("192.168.0.1",9999);
			socket = new Socket(IP, 9000);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			isConnected = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connect fail...check connectServer() method");
			isConnected = false;
			throw e;
			// ����server�Ƿ��Ѿ�����
		}
	}

	/**
	 * ע���û�
	 * @param username
	 * @param pwd
	 * @param workcode
	 * @return isSuccess
	 */
	public boolean register(String username, String pwd, String workcode) {
		boolean isSuccess = false;
		try {
			out.println("create;" + username + ";" + pwd + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("registget="+getstr);
			if (getstr.equals("USERCREATED")) {
				isSuccess = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * ����ǩ����Ϣ
	 * @return isSuccess
	 */
	public boolean sendCheck(String username,String password,String workcode) {

		boolean isSuccess = false;

		try {
			out.println("check;" + username + ";" + password + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("checkget="+getstr);
			if(getstr.equals("LOGINSUCCESS")){
				isSuccess = true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * �رշ���������
	 */
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
