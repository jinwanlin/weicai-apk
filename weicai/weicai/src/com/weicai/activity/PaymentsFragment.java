package com.weicai.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.weicai.R;

public class PaymentsFragment extends Fragment implements OnClickListener {

	private TextView orderRow;
	private TableLayout table;
	private Context context;
	
	public void setContext(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.payments_layout, container, false);
		
		orderRow = (TextView) layout.findViewById(R.id.order1);
		orderRow.setOnClickListener(this);
		
		table = (TableLayout) layout.findViewById(R.id.table);
		showPayments();
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

	public void showPayments(){
		TableRow row = new TableRow(context);
		row.setId(0);

		LinearLayout td_1 = new LinearLayout(context);
		td_1.setOrientation(LinearLayout.VERTICAL);
		td_1.setPadding(10, 10, 10, 10);

		TextView pay_text = new TextView(context);
		pay_text.setText("付款");
		pay_text.setTextSize(18);
		td_1.addView(pay_text);

		TextView pay_date = new TextView(context);
		pay_date.setText("02-21");
		pay_date.setTextSize(13);
		pay_date.setTextColor(Color.rgb(112, 112, 112));
		td_1.addView(pay_date);
		
		TextView desc = new TextView(context);
		desc.setText("订单号：23423");
		desc.setTextSize(18);
		desc.setPadding(10, 10, 10, 10);

		TextView total = new TextView(context);
		total.setText("423.35");
		total.setTextSize(18);
		total.setHeight(50);
		total.setPadding(10, 10, 10, 10);
		total.setGravity(Gravity.RIGHT);
		total.setTypeface(Typeface.DEFAULT_BOLD);



		row.addView(td_1);
		row.addView(desc);
		row.addView(total);
		row.setGravity(Gravity.CENTER);
		
		table.addView(row);
	}
}