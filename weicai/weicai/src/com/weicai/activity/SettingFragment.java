package com.weicai.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.weicai.R;

public class SettingFragment extends Fragment implements OnClickListener {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View settingLayout = inflater.inflate(R.layout.setting_layout, container, false);
		
//		settingImage = (ImageView) settingLayout.findViewById(R.id.setting_1);
//		settingText = (TextView) settingLayout.findViewById(R.id.setting_2);
//		settingImage.setOnClickListener(this);

		return settingLayout;
	}
	
	@Override
	public void onClick(View arg0) {
//		settingText.append("1");
		
	}

}
