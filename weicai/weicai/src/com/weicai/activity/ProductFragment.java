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
import com.weicai.adapter.ProductListAdapter;
import com.weicai.api.CaiCai;
import com.weicai.bean.Product;

public class ProductFragment extends Fragment  {
	static final String tag = "ProductFragment";

	private ListView productItemLV;
	private Context context;
	
	public void setContext(Context context){
		this.context = context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.products_layout, container, false);

		productItemLV = (ListView) messageLayout.findViewById(R.id.productItem);
		new refreshProductsTask("Vegetable").execute(0);

		return messageLayout;
	}
	
    
    /**
     * 刷新商品列表
     */
	class refreshProductsTask extends AsyncTask<Integer, Integer, String> {
		String type;
		
		public refreshProductsTask(String type){
			this.type = type;
		}
		@Override
		protected String doInBackground(Integer... params) {
			return CaiCai.productsStr(type);
		}
		@Override
		protected void onPostExecute(String result) {
			JSONArray json = CaiCai.StringToJSONArray(result);
			List<Product> products = Product.jsonToList(json);
			
			ProductListAdapter productListAdapter = new ProductListAdapter(context, products);
			Log.i(tag, (productItemLV == null)+"");
			productItemLV.setAdapter(productListAdapter);

			super.onPostExecute(result);
		}

	}

}
