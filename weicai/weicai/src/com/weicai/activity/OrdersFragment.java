package com.weicai.activity;

import java.util.List;

import org.json.JSONArray;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.weicai.R;
import com.weicai.adapter.OrderListAdapter;
import com.weicai.api.CaiCai;
import com.weicai.bean.Order;

public class OrdersFragment extends Fragment {

	static final String tag = "OrdersFragment";

	private ListView orderListLV;
	private Context context;
	
	private MainActivity mainActivity;


	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.orders_layout, container, false);
		
		orderListLV = (ListView) layout.findViewById(R.id.orderListLV);
		new refreshOrdersTask().execute(0);
		
		return layout;
	}
	
    
    /**
     * 刷新订单列表
     */
	class refreshOrdersTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			return CaiCai.orders();
		}
		@Override
		protected void onPostExecute(String result) {
			JSONArray json = CaiCai.StringToJSONArray(result);
			List<Order> orders = Order.jsonToList(json);
			
			OrderListAdapter orderListAdapter = new OrderListAdapter(context, orders, OrdersFragment.this);
			Log.i(tag, (orderListLV == null)+"");
			orderListLV.setAdapter(orderListAdapter);

			super.onPostExecute(result);
		}
	}

}
