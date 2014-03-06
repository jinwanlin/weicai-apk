package com.weicai.activity;

import java.util.List;

import org.json.JSONArray;

import android.app.Fragment;
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
import com.weicai.activity.BaseActivity.NetTask;
import com.weicai.api.CaiCai;
import com.weicai.bean.Payment;
import com.weicai.util.tool.TodayYestorday;

public class PaymentsFragment extends Fragment {

	private TextView overage;
	private TableLayout table;
	private MainActivity mainActivity;


	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.payments_layout, container, false);

		overage = (TextView) layout.findViewById(R.id.overage);
		table = (TableLayout) layout.findViewById(R.id.table);

		new RefreshPaymentsTask().execute(0);
		return layout;
	}

//	@Override
//	public void onClick(View v) {
//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.hide(PaymentsFragment.this);
//
//		OrderFragment orderFragment = new OrderFragment();
//		orderFragment.setLastFragment(this);
//		MainActivity.orderFragment = orderFragment;
//
//		transaction.add(R.id.content, orderFragment);
//		transaction.commit();
//	}

	public void showPayments(List<Payment> payments) {
		// 显示余额
		if (payments.size() > 0) {
			Payment payment = payments.get(0);
			overage.setText(payment.getOverage() + "");
		}

		// 显示列表
		for (int i = 0; i < payments.size(); i++) {
			final Payment payment = payments.get(i);

			final TableRow row = new TableRow(mainActivity);
			row.setId(i);

			LinearLayout td_1 = new LinearLayout(mainActivity);
			td_1.setOrientation(LinearLayout.VERTICAL);
			td_1.setPadding(10, 10, 10, 10);

			TextView pay_text = new TextView(mainActivity);
			pay_text.setText(payment.getType());
			pay_text.setTextSize(18);
			td_1.addView(pay_text);

			TextView pay_date = new TextView(mainActivity);
			pay_date.setText(TodayYestorday.getTime(payment.getCreatedAt()));
			pay_date.setTextSize(13);
			pay_date.setTextColor(Color.rgb(112, 112, 112));
			td_1.addView(pay_date);

			TextView desc = new TextView(mainActivity);
			desc.setText(payment.getDesc());
			desc.setTextSize(18);
			desc.setPadding(10, 10, 10, 10);

			TextView total = new TextView(mainActivity);
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
					if(payment.getOrderId()!=0){
						mainActivity.showOrder(payment.getOrderId(), PaymentsFragment.this);
					}
				}
			});

			row.setBackgroundResource(R.drawable.leba_bg_top_selector);
			table.addView(row);

			// 分隔线
			TextView line = new TextView(mainActivity);
			line.setHeight(1);
			line.setBackgroundColor(Color.rgb(110, 110, 110));
			table.addView(line);
		}
 
	}


	/**
	 * 刷新订单列表
	 */
	class RefreshPaymentsTask extends NetTask {
		
		@Override
		protected String doInBackground(Integer... params) {
			return CaiCai.payments();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result == null || result.equals("")){
				return;
			}
			
			JSONArray json = CaiCai.StringToJSONArray(result);
			List<Payment> payments = Payment.jsonToList(json);

			showPayments(payments);
			super.onPostExecute(result);
		}
	}

}