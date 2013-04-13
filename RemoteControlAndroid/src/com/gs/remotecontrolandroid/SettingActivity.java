package com.gs.remotecontrolandroid;

import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gs.remotecontrol.net.Connector;
import com.gs.remotecontrol.net.ConnectorPool;
import com.gs.remotecontrol.utils.GlobalVariables;
import com.gs.remotecontrol.utils.SharePersistent;
import com.gs.remotecontrol.utils.StringUtils;


public class SettingActivity extends Activity{
	
	private EditText mEditTextIpInput;
	private EditText mEditTextPortInput, mEditTextScreenPort;
	private Button buttonLink;
	private int mIntPort = GlobalVariables.INT_DEFAULT_PORT_COMMAND;
	private LinkHostTask linkHostTask;
	private ProgressDialog mProgressDialog = null;
	private String tag = "SettingActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		setUpView();
	}
	void setUpView() {
		setContentView(R.layout.setting);	
		mEditTextIpInput = (EditText) findViewById(R.id.editTextIp);
		mEditTextPortInput = (EditText) findViewById(R.id.editTextPort);
		mEditTextPortInput.setText(String.valueOf(GlobalVariables.INT_DEFAULT_PORT_COMMAND));
		mEditTextScreenPort = (EditText) findViewById(R.id.editTextPortScreen);
		mEditTextScreenPort.setText(String.valueOf(GlobalVariables.INT_DEFAULT_PORT_IMG));
		
		String oldIp = SharePersistent.getPerference(this,GlobalVariables.STRING_IP_KEY);
		if (!StringUtils.isEmpty(oldIp)) {
			mEditTextIpInput.setText(oldIp);
		}
		
		buttonLink = (Button) findViewById(R.id.buttonLink);

		buttonLink.setOnClickListener(
			new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				linkHostTask = new LinkHostTask();
				// /^\d+$/ 正则表达，数字匹配了一个以上
				String portString = mEditTextPortInput.getText().toString();

				if (portString.matches("//^\\d+$//")) {
						mIntPort = Integer.parseInt(portString);					
				}
				
				String portScreen = mEditTextScreenPort.getText().toString();
				if (portScreen.matches("//^\\d+$//")) {
						GlobalVariables.INT_DEFAULT_PORT_IMG = Integer.parseInt(portString);					
				}

				String hostIp = mEditTextIpInput.getText().toString();
				if (!StringUtils.isEmpty(hostIp)|| hostIp.matches(GlobalVariables.STRING_IP_REGX)) {
					linkHostTask.execute(hostIp);
					GlobalVariables.STRING_HOST_IP = hostIp;// 修改默认的ip
					SharePersistent.savePerference(SettingActivity.this,GlobalVariables.STRING_IP_KEY, hostIp);

				}
			}
		});
		
		buttonLink.requestFocus();

		mProgressDialog = new ProgressDialog(SettingActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("Connect");
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Connecting PC...");
		mProgressDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				buttonLink.setEnabled(true);
				if(null!=linkHostTask){
					linkHostTask.cancel(true);
				}
			}
		});
	}
	//socket线程
	class LinkHostTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute() {
			// 预处理
			buttonLink.setEnabled(false);

			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... param) {
			// 后台计算
			String host = param[0];
			try {
				ConnectorPool.clearPool();
				mIntPort = Integer.parseInt(mEditTextPortInput.getText().toString().trim());
				Socket socket = new Socket(InetAddress.getByName(host),mIntPort);
				Connector conector = new Connector(socket);
				ConnectorPool.putConnector(conector.getmStringRemoteHost(),conector);
			} catch (Exception e) {
				Log.e(tag, "message:" + e.getMessage());
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// 处理结果
			buttonLink.setEnabled(true);
			mProgressDialog.dismiss();

			if (ConnectorPool.getConnectorPoolSize() > 0) {
				// 连接成功
				Toast.makeText(SettingActivity.this, "Connect completed！", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				// 连接失败
				Toast.makeText(SettingActivity.this, "Connect failed!",Toast.LENGTH_SHORT).show();
			}

		}

	};
}
