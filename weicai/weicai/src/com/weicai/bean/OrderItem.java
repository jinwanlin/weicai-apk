package com.weicai.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;


/**
 * @author jiuwuerliu@sina.com
 * 
 *         数据库实体对象
 */
public class OrderItem {
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	private long id;
	private Date createdAt;
	private Date updatedAt;
	
	private int productId;
	private String productName;
	private String productUnit;
	private int orderId;
    
	private double price;
	private int orderAmount;
	private double shipAmount;
	private double orderSum;
	private double shipSum;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getShipAmount() {
		return shipAmount;
	}

	public void setShipAmount(double shipAmount) {
		this.shipAmount = shipAmount;
	}

	public double getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(double orderSum) {
		this.orderSum = orderSum;
	}

	public double getShipSum() {
		return shipSum;
	}

	public void setShipSum(double shipSum) {
		this.shipSum = shipSum;
	}

	public static OrderItem jsonToOrderItem(JSONObject json) {
		OrderItem orderItem = new OrderItem();
		try {
			orderItem.setId(json.getLong("id"));
			orderItem.setProductId(json.getInt("product_id"));
			orderItem.setProductName(json.getString("product_name"));
			orderItem.setProductUnit(json.getString("product_unit"));
			orderItem.setOrderId(json.getInt("order_id"));
			orderItem.setPrice(json.getDouble("price"));
			orderItem.setOrderAmount(json.getInt("order_amount"));
			orderItem.setShipSum(json.getDouble("ship_amount"));
			orderItem.setOrderSum(json.getDouble("order_sum"));
			orderItem.setShipSum(json.getDouble("ship_sum"));
			try {
				Date created_at = sdf.parse(json.getString("created_at"));
				orderItem.setCreatedAt(created_at);
			} catch (ParseException e) {
			}
			try {
				Date updated_at = sdf.parse(json.getString("updated_at"));
				orderItem.setUpdatedAt(updated_at);
			} catch (ParseException e) {
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderItem;
	}
	
	public static List<OrderItem> jsonToList(JSONArray array) {

		List<OrderItem> list = new ArrayList<OrderItem>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject json = array.getJSONObject(i);
					OrderItem item = jsonToOrderItem(json);
					list.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

}