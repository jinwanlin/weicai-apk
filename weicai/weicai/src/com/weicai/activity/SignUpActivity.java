package com.weicai.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.weicai.R;
import com.weicai.activity.MainActivity;
import com.weicai.api.CaiCai;
import com.weicai.bean.User;
import com.weicai.dao.UserDao;
import com.weicai.util.tool.SIMCardInfo;


public class SignUpActivity extends Activity {
	static final String tag = "SignUpActivity";
	private EditText userNameText, passwordText;
	private Button btSignUp;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		
		userNameText = (EditText) findViewById(R.id.phone);
		SIMCardInfo siminfo = new SIMCardInfo(SignUpActivity.this);
		userNameText.setText(siminfo.getNativePhoneNumber().replace("+86", ""));
		
		passwordText = (EditText) findViewById(R.id.password);
		
		btSignUp = (Button) findViewById(R.id.sign_up);
		btSignUp.setOnClickListener(new ClickViewHandler());
	}

	public class ClickViewHandler implements OnClickListener {
		@Override
		public void onClick(View v) {
			SignInTask dTask = new SignInTask();
			dTask.execute(0);
		}
	}

	class SignInTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String userName = userNameText.getText().toString();
			String password = passwordText.getText().toString();
			return CaiCai.sign_up(userName, password);
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i(tag, "sign_in result: " + result);
			JSONObject json = CaiCai.StringToJSONObject(result);

			int status = -1;
			String message = "";
			JSONObject userObj = null;
			try {
				status = json.getInt("status");
				message = json.getString("status");
				userObj = json.getJSONObject("user");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (status == 0) {

				User user = User.jsonToUser(userObj);
				UserDao.create(user);

				Intent intent = new Intent();
				intent.setClass(SignUpActivity.this, MainActivity.class);
				startActivity(intent);
				finish();// 停止当前的Activity,如果不写,则按返回键会跳转回原来的Activity
				
			} else {
				Log.i(tag, "sign_in error: " + message);
			}
			super.onPostExecute(result);
		}

	}
}
