package com.gs.remotecontrolandroid;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.remotecontrol.bean.AdapterCharactor;
import com.gs.remotecontrol.net.ConnectorPool;
import com.gs.remotecontrol.net.INetCallBack;
import com.gs.remotecontrol.tools.RemoteOperateImpl;
import com.gs.remotecontrol.tools.Tool;
import com.gs.remotecontrol.utils.GlobalVariables;
import com.gs.remotecontrol.views.KeyBoardView;
import com.gs.remotecontrol.views.PointFBean;


public class KeyBoardActivity extends Activity implements INetCallBack{

	private LinearLayout mLinearLayoutKeylist;
	private KeyBoardView mKeyBoradView;
	private TextView textView;
	private ListView listViewCharactor;
	public  String tag="KeyBoardActivity";
	private ArrayList<PointFBean> mArrayListpointFlist = null;
	private Animation animationSlideInRight,animationSlideOutRight;
	protected Character[] keys={
			'A','B','C','D','E','F','G',
			'H','I','J','K','L','M','N',
			'O','P','Q','R','S','T','U',
			'V','W','X','Y','Z'
			};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//常亮

		setContentView(R.layout.keyboard);
		mArrayListpointFlist=new ArrayList<PointFBean>();
	
		GlobalVariables.BOOLEAN_LOCK_KEYBOAED=false;//键盘解锁
		setUpView();
		initKeyBoard();
	}
	public void setUpView(){
		mKeyBoradView = (KeyBoardView) findViewById(R.id.mouseView);
		textView = (TextView) findViewById(R.id.textViewNotify);
		listViewCharactor=(ListView)findViewById(R.id.listViewCharactor);
		mLinearLayoutKeylist=(LinearLayout)findViewById(R.id.linearLayoutKeylist);


		listViewCharactor.setAdapter(new AdapterCharactor(this, Arrays.asList(keys)));
		listViewCharactor.setScrollbarFadingEnabled(true);
		listViewCharactor.setVerticalScrollBarEnabled(false);
		listViewCharactor.setScrollbarFadingEnabled(true);
		listViewCharactor.setItemsCanFocus(false);
		mKeyBoradView.setRemoteOperateImpl(new RemoteOperateImpl(ConnectorPool.getConnectorByKey(ConnectorPool.STRING_CKEY),this));
		listViewCharactor.setOnItemClickListener(new OnItemClickListener() {//添加自定义键
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Character var = keys[position];			
				int indexres=isKeyCodeExistInList(var);
				if(indexres!=-1){
					//存在就取消
					mArrayListpointFlist.remove(indexres);
				}else{
					//不存在就添加
					PointFBean pointF = new PointFBean();
					Point randPoint = Tool.getRandomPoint(new Point(200,300));
					pointF.setX(randPoint.x+50);
					pointF.setY(randPoint.y+50);
				
					int radius=(int)GlobalVariables.FLOAT_BUTTON_RADIUS;
					
					pointF.setRadius(radius);
					pointF.setStringKey(String.valueOf(keys[position]));
					
					mArrayListpointFlist.add(pointF);
				}
				mKeyBoradView.invalidate();	
			}
		});

	}
	
	  	
	@Override
	protected void onResume() {
		super.onResume();		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_keyboard, menu);
		return true;
	}
	public void initKeyBoard(){
		mKeyBoradView.setmArrayListPointbeans(mArrayListpointFlist);
		mKeyBoradView.setHandler(mHandler);
	}
	/**
	 * 判断是否存在某个
	 * @param charac
	 * @return
	 */
    public int isKeyCodeExistInList(Character charac){
    	int index=-1;
    	for(int i=0,isize=mArrayListpointFlist.size();i<isize;i++){
    		int chac = mArrayListpointFlist.get(i).getKeyCode();
    		if(charac==chac){
    			index=i;
    			break;
    		}
    	}
    	return index;
    }	

	/**
	 * 当用户选择了当前ItmeSelected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(tag, item.getItemId() + ":" + item.getItemId());
		switch (item.getItemId()) {

		case R.id.menuItemLock:		
			Log.i(tag, "锁定");
			if(!GlobalVariables.BOOLEAN_LOCK_KEYBOAED ){
				GlobalVariables.BOOLEAN_LOCK_KEYBOAED = true;
				mLinearLayoutKeylist.startAnimation(animationSlideOutRight);
				animationSlideOutRight.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						mLinearLayoutKeylist.setVisibility(View.GONE);
					}
				});
			}else{
				Toast.makeText(this,"Keyboard locked!", Toast.LENGTH_SHORT).show();
			}						
			break;
		case R.id.menuItemUnLock:
			Log.i(tag, "解锁");
			if(GlobalVariables.BOOLEAN_LOCK_KEYBOAED){
				GlobalVariables.BOOLEAN_LOCK_KEYBOAED = false;
				mLinearLayoutKeylist.setVisibility(View.VISIBLE);
				mLinearLayoutKeylist.startAnimation(animationSlideInRight);	
			}else{
				Toast.makeText(this, "Keyboard unlocked!", Toast.LENGTH_SHORT).show();
			}
			
			break;			
		default:
			break;
		}
		return false;
	}
	/*
	 用来接受keyboradview发送过来的事件
	 */
	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			String[] arrays = bundle.getStringArray(GlobalVariables.STRING_LOCATION_KEY);
			textView.setText(/*arrays[0] + ":" + arrays[1] + ":" +*/"按下:"+ arrays[2]);
		};
	};
	
	@Override
	public void OnFinish() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnIntercepted(String source) {
		// TODO Auto-generated method stub
//		Toast.makeText(this, getResources().getString(R.string.app_lost_host), Toast.LENGTH_SHORT).show();
//		this.finish();
		
	}
	@Override
	public void OnStart() {
		// TODO Auto-generated method stub
		
	}


}
