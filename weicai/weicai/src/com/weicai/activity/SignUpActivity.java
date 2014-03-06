package com.weicai.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weicai.R;
import com.weicai.api.CaiCai;
import com.weicai.bean.User;
import com.weicai.dao.UserDao;
import com.weicai.util.tool.SIMCardInfo;

public class SignUpActivity extends BaseActivity implements OnClickListener{
	static final String tag = "SignUpActivity";
	private EditText userNameText, passwordText;
	private TextView phoneNumber, validate_code_text, message;
	private Button validate_and_sign_up, next_or_sign_up, resend_validate_code;
	private LinearLayout sign_up_ly, validate_ly;
	public boolean hasValidateCode = false;
	private UserDao userDao;
	private String validateCode;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_up);
		
		userDao = UserDao.getInstance();
		
//		是否需要验证码
		new HasValidateCodeTask().execute(0);

		sign_up_ly = (LinearLayout)findViewById(R.id.sign_up_ly);
		validate_ly = (LinearLayout)findViewById(R.id.validate_ly);
		
		userNameText = (EditText) findViewById(R.id.phone);
		userNameText.setText(new SIMCardInfo(SignUpActivity.this).getNativePhoneNumber().replace("+86", ""));
		passwordText = (EditText) findViewById(R.id.password);

		phoneNumber = (TextView) findViewById(R.id.phone_no);
		validate_code_text = (TextView) findViewById(R.id.validate_code_text);
		

		message = (TextView) findViewById(R.id.message);
		
//		返回上个activity
//		findViewById(R.id.back).setOnClickListener(this);
		
//		下一步 或 注册
		next_or_sign_up = (Button)findViewById(R.id.next_or_sign_up);
		next_or_sign_up.setOnClickListener(this);
		
//		注册
		validate_and_sign_up = (Button)findViewById(R.id.validate_and_sign_up);
		validate_and_sign_up.setOnClickListener(this);
		

		
//		从新发送验证码
		resend_validate_code = (Button)findViewById(R.id.resend_validate_code);
		resend_validate_code.setOnClickListener(this);
//		更换手机号
		findViewById(R.id.change_phone).setOnClickListener(this);		
		findViewById(R.id.sign_in).setOnClickListener(this);
		findViewById(R.id.find_password).setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.next_or_sign_up:
			if (hasValidateCode) {
				phoneNumber.setText(userNameText.getText().toString());
				sign_up_ly.setVisibility(View.GONE);
				validate_ly.setVisibility(View.VISIBLE);
				new GetValidateCodeTask().execute(0);
			} else {
				userNameText.getText().toString();
				new SignUpTask().execute(0);
			}
			break;
		case R.id.validate_and_sign_up:
			if (validate_code_text.getText().toString().equals(validateCode)){
				new SignUpTask().execute(0);
			}else{
				Log.i(tag, "validate_code not eques");
				message.setText("验证码错误");
			}
			break;
		case R.id.change_phone:
			sign_up_ly.setVisibility(View.VISIBLE);
			validate_ly.setVisibility(View.GONE);
			break;
		case R.id.resend_validate_code:
			new GetValidateCodeTask().execute(0);
			break;
		case R.id.sign_in:
			Intent intent = new Intent();
			intent.setClass(SignUpActivity.this, SignInActivity.class);
			startActivity(intent);
			break;
		case R.id.find_password:
			Intent intent2 = new Intent();
			intent2.setClass(SignUpActivity.this, SignInActivity.class);
			startActivity(intent2);
			break;
			
			
		default:
			break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	class SignUpTask extends NetTask {
		@Override
		protected String doInBackground(Integer... params) {
			String userName = userNameText.getText().toString();
			String password = passwordText.getText().toString();
			return CaiCai.sign_up(userName, password);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("")) {
				return;
			}
			
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
				userDao.insert(user);

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
	
	
	
	
	
	
	class HasValidateCodeTask extends NetTask {

		@Override
		protected String doInBackground(Integer... params) {
			return CaiCai.has_validate_code();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("")) {
				return;
			}
				
			
			Log.i("aa--", result);
			JSONObject json = CaiCai.StringToJSONObject(result);
			
			try {
				hasValidateCode = json.getBoolean("has_validate_code");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (hasValidateCode) {
				next_or_sign_up.setText("下一步");
			} else {
				next_or_sign_up.setText("注册");
			}
			super.onPostExecute(result);
		}

	}
	
	class GetValidateCodeTask extends NetTask {

		@Override
		protected String doInBackground(Integer... params) {
			String userName = userNameText.getText().toString();
			return CaiCai.get_validate_code(userName);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("")) {
				message.setText("验证码发送失败");
				return;
			}
				
			
			Log.i("aa--", result);
			JSONObject json = CaiCai.StringToJSONObject(result);
			
			try {
				if(json.getBoolean("state")){
					validateCode = json.getString("validate_code");
				}
				message.setText(json.getString("message"));
			} catch (JSONException e) {
				message.setText("验证码发送失败");
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

	}
}
