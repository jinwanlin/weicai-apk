package com.weicai.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.weicai.R;

public class ChangePasswordFragment extends Fragment implements OnClickListener {

	private TextView oldPassword, newPassword;
	private Button submit;
	private MainActivity mainActivity;

	
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View settingLayout = inflater.inflate(R.layout.change_password, container, false);

		settingLayout.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainActivity.back();
			}
		});
		
		oldPassword = (TextView) settingLayout.findViewById(R.id.oldPassword);
		newPassword = (TextView) settingLayout.findViewById(R.id.newPassword);
		submit = (Button) settingLayout.findViewById(R.id.submit);
		
		submit.setOnClickListener(this);

		return settingLayout;
	}

	@Override
	public void onClick(View arg0) {
		
	}
	
	

}
