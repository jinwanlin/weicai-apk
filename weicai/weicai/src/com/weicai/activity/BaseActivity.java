package com.weicai.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.weicai.util.net.NetStateUtils;

public abstract class BaseActivity extends Activity {
	static final String tag = "BaseActivity";
	public static NetStateUtils netStateUtils;

	public static BaseActivity baseActivity;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		netStateUtils = new NetStateUtils(cm, lm);

		MyApplication.getInstance().addActivity(this);

	}

	/**
	 * 后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型
	 * 
	 * @author jinwanlin
	 * 
	 */
	public static abstract class NetTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected void onPostExecute(String result) {
			Log.i(tag, "sign_in result: " + result);
			if (result == null || result.equals("")) {
				if (!netStateUtils.isNetConnected()) {
					new AlertDialog.Builder(baseActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("提示").setMessage("无法访问网络，请检查WIFI和3G是否打开！").setPositiveButton("确定", null).show();// show很关键
					return;
				} else {
					new AlertDialog.Builder(baseActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("提示").setMessage("服务器异常！").setPositiveButton("确定", null).show();// show很关键
					return;
				}
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从栈中移除该Activity
		MyApplication.getInstance().finishActivity(this);
	}

}
