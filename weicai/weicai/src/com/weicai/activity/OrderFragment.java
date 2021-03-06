package com.weicai.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weicai.R;
import com.weicai.adapter.OrderItemListAdapter;
import com.weicai.api.CaiCai;
import com.weicai.bean.Order;
import com.weicai.util.tool.TodayYestorday;

public class OrderFragment extends Fragment implements OnClickListener {

	static final String tag = "OrderFragment";

	private Fragment lastFragment;
	private long order_id;
	private ListView orderItemsLV;
	private TextView sn, state, date, total;
	private LinearLayout order_info, items_header;
	

	public TextView amount_title;
	public TextView total_title, total_title2;
	
	private Context context;
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public void setLastFragment(Fragment lastFragment){
		this.lastFragment = lastFragment;
	}
	
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View orderLayout = inflater.inflate(R.layout.order_layout, container, false);
		
		orderLayout.findViewById(R.id.back).setOnClickListener(this);
		
		sn = (TextView)orderLayout.findViewById(R.id.sn);
		state = (TextView)orderLayout.findViewById(R.id.state);
		date = (TextView)orderLayout.findViewById(R.id.date);
		total = (TextView)orderLayout.findViewById(R.id.total);

		amount_title = (TextView)orderLayout.findViewById(R.id.amount_title);
		total_title = (TextView)orderLayout.findViewById(R.id.total_title);
		total_title2 = (TextView)orderLayout.findViewById(R.id.total_title2);
		
		orderItemsLV = (ListView) orderLayout.findViewById(R.id.orderItemsLV);
		
		order_info = (LinearLayout)orderLayout.findViewById(R.id.order_info);
		items_header = (LinearLayout)orderLayout.findViewById(R.id.items_header);


		order_info.setVisibility(View.GONE);
		items_header.setVisibility(View.GONE);	
		new refreshOrderTask().execute(0);
		
		return orderLayout;
	}
	
	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.hide(this);
		transaction.show(lastFragment);
		transaction.commit();
	}
	
    /**
     * 刷新订单列表
     */
	class refreshOrderTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			return CaiCai.order(order_id);
		}
		@Override
		protected void onPostExecute(String result) {

			order_info.setVisibility(View.VISIBLE);
			items_header.setVisibility(View.VISIBLE);
			
			JSONObject json = CaiCai.StringToJSONObject(result);
			Order order = Order.jsonToOrder(json);
			
			sn.setText(order.getSn());
			state.setText(order.getState_());
			date.setText(TodayYestorday.getTime(order.getCreatedAt()));
			total.setText(order.get_sum()+"");
			
			amount_title.setText(order.is_ship() ? "出货量" : "订购量");
			total_title.setText(order.is_ship() ? "出货金额" : "订购金额");
			total_title2.setText(order.is_ship() ? "出货金额" : "订购金额");
			
			List<Order> list = new ArrayList<Order>();
			list.add(order);
			
			OrderItemListAdapter orderItemListAdapter = new OrderItemListAdapter(context, order.getOrderItems(), OrderFragment.this);
			orderItemsLV.setAdapter(orderItemListAdapter);

			super.onPostExecute(result);
		}
	}


}
