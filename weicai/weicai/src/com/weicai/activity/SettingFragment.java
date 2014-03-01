package com.weicai.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weicai.R;

public class SettingFragment extends Fragment implements OnClickListener {

	private TextView call;
	private MainActivity context;

	public void setContext(MainActivity context) {
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View settingLayout = inflater.inflate(R.layout.setting_layout, container, false);

		call = (TextView) settingLayout.findViewById(R.id.call);
		call.setOnClickListener(this);

		return settingLayout;
	}

	@Override
	public void onClick(View arg0) {
		context.call("15657715360");
	}

}
