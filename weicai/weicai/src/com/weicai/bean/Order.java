package com.weicai.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weicai.R;
import com.weicai.daoCore.Id;
import com.weicai.daoCore.Table;
import com.weicai.daoCore.Transient;


/**
 * @author jiuwuerliu@sina.com
 * 
 *         数据库实体对象
 */
@Table(name = "t_order")
public class Order {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 主键字段
	 */
	@Id
	private int id;
	private Date createdAt;
	private Date updatedAt;

	private String sn;
	private double orderSum;
	private double shipSum;
	private String state;
	private List<OrderItem> orderItems;

	/**
	 * 非数据库字段
	 */
	@Transient
	private String detail;

	@Transient
	private String[] amountArray;

	public Order() {
	}

	public Order(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String[] getAmountArray() {
		return amountArray;
	}

	public void setAmountArray(String[] amountArray) {
		this.amountArray = amountArray;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}


	public static Order jsonToOrder(JSONObject p) {
		Order order = new Order();
		try {
			order.setId(p.getInt("id"));
			order.setSn(p.getString("sn"));
			order.setOrderSum(p.getDouble("order_sum"));
			order.setShipSum(p.getDouble("ship_sum"));
			order.setState(p.getString("state"));

			try {
				Date created_at = sdf.parse(p.getString("created_at"));
				order.setCreatedAt(created_at);
			} catch (ParseException e) {
			}
			try {
				Date updated_at = sdf.parse(p.getString("updated_at"));
				order.setCreatedAt(updated_at);
			} catch (ParseException e) {
			}

			if (p.has("order_items")){
				JSONArray order_items = p.getJSONArray("order_items");
				List<OrderItem> items = OrderItem.jsonToList(order_items);
				order.setOrderItems(items);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return order;
	}
	
	
	public static List<Order> jsonToList(JSONArray array) {

		List<Order> list = new ArrayList<Order>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				try {
				JSONObject p = array.getJSONObject(i);
				Order order = jsonToOrder(p);
				list.add(order);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public String getState_() {
		switch (State.toState(state.toUpperCase())) {
		case PENDING:
			return "未提交";
		case OPEN:
			return "待出库";
		case SHIP:
			return "配送中";
		case DONE:
			return "交易成功";
		case CANCELED:
			return "已取消";
		default:
			return "";
		}
	}

	public enum State {
		PENDING, OPEN, SHIP, DONE, CANCELED, NULL;
		public static State toState(String str) {
			try {
				return valueOf(str);
			} catch (Exception ex) {
				return NULL;
			}
		}
	}
}