package com.gs.remotecontrolandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.gs.remotecontrol.bean.GridViewAdapter;
import com.gs.remotecontrol.net.Connector;
import com.gs.remotecontrol.net.ConnectorPool;
import com.gs.remotecontrol.utils.GlobalVariables;


public class MainActivity extends Activity{
	private Button ConnectButton;
	
	private GridView mGridView;
	private int INT_TOOL_COUNT = 6;
	private final int INT_TOOL_KEYBORAD = 0;
	private final int INT_TOOL_MOUSE = 1;
	private final int INT_TOOL_PPT = 2;	
	private final int INT_TOOL_REMOTE = 3;
	private final int INT_TOOL_SCREEN = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GlobalVariables.globalActivitys.add(this);
		GlobalVariables.BOOLEAN_LOCK_KEYBOAED = true;
		setUpView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void setUpView() {
		
		mGridView = (GridView) findViewById(R.id.gridViewTools);
		String[] descs = new String[INT_TOOL_COUNT];

		descs[INT_TOOL_KEYBORAD] = "Virtual Keyboard";
		descs[INT_TOOL_MOUSE] = "Virtual Mouse";
		descs[INT_TOOL_PPT] = "PPT Controller";
		descs[INT_TOOL_REMOTE] = "Remote Cmd";
		descs[INT_TOOL_SCREEN] = "Virtual Screen";

		int[] drables = new int[INT_TOOL_COUNT];

		drables[INT_TOOL_KEYBORAD] = R.drawable.keyboard;
		drables[INT_TOOL_MOUSE] = R.drawable.mouse;
		drables[INT_TOOL_PPT] = R.drawable.ppt;
		drables[INT_TOOL_REMOTE] = R.drawable.cmd;
		drables[INT_TOOL_SCREEN] = R.drawable.screen;
		
		ConnectButton = (Button) findViewById(R.id.connectbutton);
		ConnectButton.setOnClickListener(
			new OnClickListener() {
			@Override
			public void onClick(View v) {				
				startOtherActivity(SettingActivity.class);
			}
		});
		
		GridViewAdapter gridAdapter = new GridViewAdapter(this, descs, drables);
		mGridView.setAdapter(gridAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int p,long arg3) {
				Connector ckm = ConnectorPool.getConnectorByKey(ConnectorPool.STRING_CKEY);
				switch (p) {
					case INT_TOOL_KEYBORAD:
						if (null == ckm) {
							notifyNoNet();
							return;
						} else {
							startOtherActivity(KeyBoardActivity.class);
						}
						break;
					case INT_TOOL_MOUSE:
						if (null == ckm) {
							notifyNoNet();
							return;
						} else {
							//Tool.startOtherActivity(MainActivity.this,MouseSelectedActivity.class);
						}
						break;
					case INT_TOOL_PPT:

						if (null == ckm) {
							notifyNoNet();
							return;
						} else {
							//Tool.startOtherActivity(MainActivity.this,PptAssistantActivity.class);
						}
						break;
					case INT_TOOL_REMOTE:
						if (null == ckm) {
							notifyNoNet();
							return;
						} else {
							//Tool.startOtherActivity(MainActivity.this,RemoteToolActivity.class);
						}
						break;				
				default:
					break;
				}

			}
		});
	}
	public void startOtherActivity(Class claz) {
		Intent intent = new Intent();
		intent.setClass(this, claz);
		startActivity(intent);
	}
	
	public void notifyNoNet() {
		Toast.makeText(MainActivity.this,"NO CONNECTION!",Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
