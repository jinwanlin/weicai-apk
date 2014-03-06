package com.weicai.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weicai.R;
import com.weicai.api.CaiCai;
import com.weicai.bean.User;
import com.weicai.dao.UserDao;
import com.weicai.util.tool.SIMCardInfo;

public class SignInActivity extends BaseActivity {
	static final String tag = "SignInActivity";
	private EditText userNameText, passwordText;
	private Button btSignIN;
	private TextView btSignUp, message;
	private UserDao userDao;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		userDao = UserDao.getInstance();
		
		User user = userDao.first();
		if (user != null) { // 已登录，跳转到首页
			Intent intent = new Intent();
			intent.setClass(SignInActivity.this, MainActivity.class);
			startActivity(intent);
			finish();// 停止当前的Activity,如果不写,则按返回键会跳转回原来的Activity
			return;
		}

		setContentView(R.layout.sign_in);
		message = (TextView) findViewById(R.id.message);

		userNameText = (EditText) findViewById(R.id.phone_number);
		userNameText.setText(new SIMCardInfo(SignInActivity.this).getNativePhoneNumber().replace("+86", ""));

		passwordText = (EditText) findViewById(R.id.password);

		btSignIN = (Button) findViewById(R.id.sign_in);
		btSignIN.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SignInTask().execute(0);
			}
		});

		btSignUp = (TextView) findViewById(R.id.sign_up);
		btSignUp.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SignInActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});

		// /**
		// * 监听输入的手机号码是否正确
		// */
		// TextWatcher phoneTextWatcher = new TextWatcher()
		// {
		//
		// @Override
		// public void afterTextChanged(Editable s)
		// {
		// // TODO Auto-generated method stub
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after)
		// {
		// // TODO Auto-generated method stub
		// }
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count)
		// {
		// Log.i("@@@", userNameText.getText().toString());
		//
		// // TODO Auto-generated method stub
		// // phoneNumber = userNameText.getText().toString();
		// // if (isPhoneNumberValid(phoneNumber) == true)
		// // {
		// // checkNextButton.setEnabled(true);
		// // Log.e("@@@", "ture");
		// // }
		// // else
		// // {
		// // checkNextButton.setEnabled(false);
		// // Log.e("@@@", "false");
		// // }
		// }
		//
		// };
		// userNameText.addTextChangedListener(phoneTextWatcher);
	}

	/**
	 * 后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型
	 * 
	 * @author jinwanlin
	 * 
	 */
	class SignInTask extends NetTask {

		@Override
		protected String doInBackground(Integer... params) {

			String userName1 = userNameText.getText().toString();
			String password = passwordText.getText().toString();
			return CaiCai.sign_in(userName1, password);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null || result.equals("")) {
				return;
			}

			Log.i(tag, "sign_in result: " + result);
			JSONObject json = CaiCai.StringToJSONObject(result);

			int status = -1;
			String msg = "";
			JSONObject userObj = null;
			try {
				status = json.getInt("status");
				msg = json.getString("message");
				userObj = json.getJSONObject("user");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (status == 0) {

				User user = User.jsonToUser(userObj);
				userDao.insert(user);

				Intent intent = new Intent();
				intent.setClass(SignInActivity.this, MainActivity.class);
				startActivity(intent);
				finish();// 停止当前的Activity,如果不写,则按返回键会跳转回原来的Activity

			} else {
				message.setText(msg);
			}
		}

	}

}
