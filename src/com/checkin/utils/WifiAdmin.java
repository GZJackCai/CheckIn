package com.checkin.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;


/**
 * wifi�����ࣨ���ã�
 * @author Administrator
 *
 */
public class WifiAdmin {
	// ����WifiManager����
	private WifiManager mWifiManager;
	// ����WifiInfo����
	private WifiInfo mWifiInfo;
	// ɨ��������������б�
	private List<ScanResult> mWifiList = null;
	// �����úõ����������б�
	private List<WifiConfiguration> mWifiConfiguration;
	// ����һ��WifiLock
	WifiLock mWifiLock;

	// ���弸�ּ��ܷ�ʽ��һ����WEP��һ����WPA������û����������
	public enum WifiCipherType {
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}

	

	// ������
	public WifiAdmin(Context context) {
		// ȡ��WifiManager����
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// ȡ��WifiInfo����
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	// ��WIFI
	public boolean openWifi() {
		boolean bRet = true;
		if (!mWifiManager.isWifiEnabled()) {
			bRet = mWifiManager.setWifiEnabled(true);
			while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
				try {
					// Ϊ�˱������һֱwhileѭ��������˯��100�����ڼ�⡭��
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException ie) {
				}
			}
		}
		return bRet;
	}

	// �ر�WIFI
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// ��鵱ǰWIFI״̬
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	// ����WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// ����WifiLock
	public void releaseWifiLock() {
		// �ж�ʱ������
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// ����һ��WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// �õ����úõ�����
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	// ָ�����úõ������������
	public void connectConfiguration(int index) {
		// �����������úõ�������������
		if (index > mWifiConfiguration.size()) {
			return;
		}
		// �������úõ�ָ��ID������
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
	}

	public void startScan() {
		mWifiManager.startScan();
		// �õ�ɨ����
		mWifiList = mWifiManager.getScanResults();
		// �õ����úõ���������
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	// �õ������б�
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	// �鿴ɨ����
	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		//openWifi();
		startScan();
		System.out.println("wifi length="+mWifiList.size());
		
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder
					.append("Index_" + new Integer(i + 1).toString() + ":");
			// ��ScanResult��Ϣת����һ���ַ�����
			// ���аѰ�����BSSID��SSID��capabilities��frequency��level
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	// �õ�MAC��ַ
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// �õ�������SSID
	public String getSSID() {
		
		mWifiInfo = mWifiManager.getConnectionInfo();
		if(mWifiInfo.getSSID()==null){
			Log.i("test", "mWifiInfo.getSSID()==null");
			return "NULL";
		}
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
	}

	// �õ�IP��ַ
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// �õ����ӵ�ID
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// �õ�WifiInfo��������Ϣ��
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	// ���һ�����粢����
	public void addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);

	}

	// �Ͽ�ָ��ID������
	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	// �ṩһ���ⲿ�ӿڣ�����Ҫ���ӵ�������
	public boolean Connect(String SSID, String Password, WifiCipherType Type) {
		if (!this.openWifi()) {
			return false;
		}
		// ����wifi������Ҫһ��ʱ��(�����ֻ��ϲ���һ����Ҫ1-3������)������Ҫ�ȵ�wifi
		// ״̬���WIFI_STATE_ENABLED��ʱ�����ִ����������
		while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
			try {
				// Ϊ�˱������һֱwhileѭ��������˯��100�����ڼ�⡭��
				Thread.currentThread();
				Thread.sleep(100);
			} catch (InterruptedException ie) {
			}
		}

		WifiConfiguration wifiConfig = this
				.CreateWifiInfo(SSID, Password, Type); // ����wifi������ϢConfiguration

		if (wifiConfig == null) {

			return false;
		}

		WifiConfiguration tempConfig = this.IsExsits(SSID); // �ж�wifi��Ϣ���б����Ǵ���

		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		int netID = mWifiManager.addNetwork(wifiConfig);
		boolean bRet = mWifiManager.enableNetwork(netID, false);
		return bRet;
	}

	// �鿴��ǰ�Ƿ�Ҳ���ù��������
	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	private WifiConfiguration CreateWifiInfo(String SSID, String Password,
			WifiCipherType Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (Type == WifiCipherType.WIFICIPHER_WEP) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (Type == WifiCipherType.WIFICIPHER_WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.status = WifiConfiguration.Status.ENABLED;
		} else {
			Log.i("tag", "no type");
			return null;
		}

		//Log.i("tag", config.toString());
		return config;
	}

	public boolean isCanScan(String str_compSSID) {
		// TODO Auto-generated method stub
		startScan();
		if(mWifiList != null){
			for(ScanResult re: mWifiList){
				if(re.SSID.equalsIgnoreCase(str_compSSID)){
					return true;
				}
			}
		}
		else{
			Log.i("test", "mWifiList is null");
		}
		
		return false;
	}

}
