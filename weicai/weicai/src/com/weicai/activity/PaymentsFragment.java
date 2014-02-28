package com.weicai.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weicai.R;

public class PaymentsFragment extends Fragment implements OnClickListener {

	private TextView orderRow;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.payments_layout, container, false);
		
		orderRow = (TextView) layout.findViewById(R.id.order1);
		orderRow.setOnClickListener(this);
		
		return layout;
	}
	
	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.hide(PaymentsFragment.this);
		
		OrderFragment orderFragment = new OrderFragment();
		orderFragment.setLastFragment(this);
		MainActivity.orderFragment = orderFragment;
		
		transaction.add(R.id.content, orderFragment);
		transaction.commit();
	}

}