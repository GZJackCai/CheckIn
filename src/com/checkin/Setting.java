package com.checkin;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
/**
 * ���ý��棨��ʱ���ã�
 * @author Administrator
 *
 */
public class Setting extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ĵ�ֵ�����Զ����浽SharePreferences
		addPreferencesFromResource(R.xml.setting);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return false;
	}

}
