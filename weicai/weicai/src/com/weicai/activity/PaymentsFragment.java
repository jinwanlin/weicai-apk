package com.weicai.activity;

import java.util.List;

import org.json.JSONArray;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import com.weicai.api.CaiCai;
import com.weicai.bean.Payment;
import com.weicai.util.tool.TodayYestorday;

public class PaymentsFragment extends Fragment implements OnClickListener {

	private TextView overage;
	private TableLayout table;
	private MainActivity context;

	public void setContext(MainActivity context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.payments_layout, container, false);

		overage = (TextView) layout.findViewById(R.id.overage);
		table = (TableLayout) layout.findViewById(R.id.table);

		new refreshPaymentsTask().execute(0);
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

	public void showPayments(List<Payment> payments) {
		// 显示余额
		if (payments.size() > 0) {
			Payment payment = payments.get(0);
			overage.setText(payment.getOverage() + "");
		}

		// 显示列表
		for (int i = 0; i < payments.size(); i++) {
			final Payment payment = payments.get(i);

			final TableRow row = new TableRow(context);
			row.setId(i);

			LinearLayout td_1 = new LinearLayout(context);
			td_1.setOrientation(LinearLayout.VERTICAL);
			td_1.setPadding(10, 10, 10, 10);

			TextView pay_text = new TextView(context);
			pay_text.setText(payment.getType());
			pay_text.setTextSize(18);
			td_1.addView(pay_text);

			TextView pay_date = new TextView(context);
			pay_date.setText(TodayYestorday.getTime(payment.getCreatedAt()));
			pay_date.setTextSize(13);
			pay_date.setTextColor(Color.rgb(112, 112, 112));
			td_1.addView(pay_date);

			TextView desc = new TextView(context);
			desc.setText(payment.getDesc());
			desc.setTextSize(18);
			desc.setPadding(10, 10, 10, 10);

			TextView total = new TextView(context);
			total.setText(payment.getAmount() + "");
			total.setTextSize(18);
			total.setHeight(50);
			total.setPadding(10, 10, 10, 10);
			total.setGravity(Gravity.RIGHT);
			total.setTypeface(Typeface.DEFAULT_BOLD);

			row.addView(td_1);
			row.addView(desc);
			row.addView(total);

			row.setGravity(Gravity.CENTER);
			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// table.removeView(table.findViewById(0));
					showOrder(payment.getOrderId());
				}
			});

			row.setBackgroundResource(R.drawable.leba_bg_top_selector);
			table.addView(row);

			// 分隔线
			TextView line = new TextView(context);
			line.setHeight(1);
			line.setBackgroundColor(Color.rgb(110, 110, 110));
			table.addView(line);
		}

	}

	public void showOrder(long order_id) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.hide(PaymentsFragment.this);

		OrderFragment orderFragment = new OrderFragment();
		orderFragment.setLastFragment(this);
		orderFragment.setOrder_id(order_id);
		orderFragment.setContext(context);
		MainActivity.orderFragment = orderFragment;

		transaction.add(R.id.content, orderFragment);
		transaction.commit();
	}


	/**
	 * 刷新订单列表
	 */
	class refreshPaymentsTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			return CaiCai.payments();
		}

		@Override
		protected void onPostExecute(String result) {
			JSONArray json = CaiCai.StringToJSONArray(result);
			List<Payment> payments = Payment.jsonToList(json);

			showPayments(payments);
			super.onPostExecute(result);
		}
	}

}