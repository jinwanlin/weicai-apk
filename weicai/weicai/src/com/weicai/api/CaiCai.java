package com.weicai.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weicai.dao.UserDao;
import com.weicai.util.net.HttpUtils;

public class CaiCai {
	
	private static UserDao userDao;

	static{
		userDao = UserDao.getInstance();
	}

	private static final String BASE_URL = "http://192.168.0.103:3000";
//	 private static final String BASE_URL = "http://115.28.160.65";

	/**
	 * String 转 JSONObject
	 * 
	 * @param str
	 * @return
	 */
	public static JSONObject StringToJSONObject(String str) {
		if (str.equals("")) {
			return null;
		}

		Log.i("mylog", "请求结果-->" + str);

		JSONObject jsobj = null;
		try {
			jsobj = new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("mylog", "登陆结果-->" + str);
		}
		return jsobj;
	}

	/**
	 * String 转 JSONArray
	 * 
	 * @param str
	 * @return
	 */
	public static JSONArray StringToJSONArray(String str) {
		if (str == null) {
			return null;
		}

		JSONArray json = null;
		try {
			json = new JSONArray(str);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("mylog", "StringToJSONArray失败，str-->" + str);
		}
		return json;
	}

	
	
	
	
	
	
	
	
	/**
	 * 商品列表
	 * 
	 * @return
	 */
	public static String productsStr(String type) {
		String url = BASE_URL + "/api/v2/products/list";
		Map<String, String> map = new HashMap<String, String>();
		map.put("user[id]", userDao.first().getId()+"");
		if(type!=null){
			map.put("type", type);
		}
		return HttpUtils.doPost(url, map);
	}

//	/**
//	 * 商品列表
//	 * 
//	 * @return
//	 */
//	public static List<Product> products() {
//		List<Product> products = new ArrayList<Product>();
////		url = BASE_URL + "products?" + URLEncoder.encode("user[id]", "UTF-8") + "=" + 1 + "&" + URLEncoder.encode("user[level]", "UTF-8") + "=" + 1 + "&type=Vegetable";
//		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("user[id]", 1+"");
//		
//		String url = BASE_URL + "/api/v2/products/list";
//		JSONArray json = StringToJSONArray(HttpUtils.doPost(url, map));
//		Product.jsonToList(json);
//		return products;
//	}

	/**
	 * 订单列表
	 * 
	 * @return
	 */
	public static String orders() {
		String url = BASE_URL + "/api/v2/orders/list";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("user[id]", userDao.first().getId()+"");
		
		return HttpUtils.doPost(url, map);
	}
	

	/**
	 * 订单明细
	 * 
	 * @return
	 */
	public static String order(long id) {
		String url = BASE_URL + "/api/v2/orders/"+id;
		return HttpUtils.doGet(url);
	}
	
	/**
	 * 账单列表
	 * 
	 * @return
	 */
	public static String payments() {
		String url = BASE_URL + "/api/v2/payments/list";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("user[id]", userDao.first().getId()+"");
		
		return HttpUtils.doPost(url, map);
	}
	
	/**
	 * 注册是否需要验证码
	 * @return
	 */
	public static String has_validate_code() {
		String url = BASE_URL + "/api/v2/users/has_validate_code";
		return HttpUtils.doGet(url);
	}
	
	
	/**
	 * 发送注册验证码
	 * @return
	 */
	public static String get_validate_code(String phone) {
		String url = BASE_URL + "/api/v2/users/get_validate_code";
		Map<String, String> map = new HashMap<String, String>();
		map.put("user[phone]", phone);
		return HttpUtils.doPost(url, map);
	}

	/**
	 * 登陆
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	public static String sign_in(String phone, String password) {
		String url = BASE_URL + "/api/v2/users/sign_in";

		Map<String, String> map = new HashMap<String, String>();
		map.put("user[phone]", phone);
		map.put("user[password]", password);

		return HttpUtils.doPost(url, map);
	}

	/**
	 * 注册
	 * 
	 * @param phone
	 * @return
	 */
	public static String sign_up(String phone, String password) {
		String url = BASE_URL + "/api/v2/users/sign_up";

		Map<String, String> map = new HashMap<String, String>();
		map.put("user[phone]", phone);
		map.put("user[password]", password);

		return HttpUtils.doPost(url, map);
	}

	/**
	 * 验证注册
	 * 
	 * @param phone
	 * @param password
	 * @param validate_code
	 * @return
	 */
	public static JSONObject validate(String phone, String password, String validate_code) {
		String url = "/api/v2/users/validate";

		Map<String, String> map = new HashMap<String, String>();
		map.put("user[phone]", phone);
		map.put("user[password]", password);
		map.put("user[validate_code]", validate_code);

		return StringToJSONObject(HttpUtils.doPost(url, map));
	}
	
	/**
	 * 购买
	 * 
	 * @param product_id
	 * @param amount
	 * @return
	 */
	public static String buy(String product_id, String amount) {
		String url = BASE_URL + "/api/v2/order_items";

		Map<String, String> map = new HashMap<String, String>();
		map.put("order_item[product_id]", product_id);
		map.put("order_item[order_amount]", amount);
		map.put("user[id]", userDao.first().getId()+"");

		return HttpUtils.doPost(url, map);
	}

}
