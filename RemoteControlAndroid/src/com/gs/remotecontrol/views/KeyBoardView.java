package com.gs.remotecontrol.views;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.gs.remotecontrol.tools.RemoteOperateImpl;
import com.gs.remotecontrol.utils.GlobalVariables;
import com.gs.remotecontrolandroid.R;

public class KeyBoardView extends View implements OnLongClickListener{
	private String tag = "KeyBoardView";

	private Paint mPaint = null;
	private ArrayList<PointFBean> mArrayListPointbeans;
	private RemoteOperateImpl remoteOperateImpl = null;

	private int KEY_DOWN = 0;
	private int KEY_UP = 1;
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private Handler handler = null;

	public KeyBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		this.setOnLongClickListener(this);
		init();
		TypedArray params = context.obtainStyledAttributes(attrs,R.styleable.mouseView);
		// String title=params.getString(R.styleable.mouseView_title);
		int backGroundColor = params.getColor(R.styleable.mouseView_back_ground_color, 0xffffff);
		setBackgroundColor(backGroundColor);
		params.recycle();
	}

	public KeyBoardView(Context context, ArrayList<PointFBean> pointflist) {
		super(context);
		this.mArrayListPointbeans = pointflist;
		this.setOnLongClickListener(this);
		init();
	}

	void init() {
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setAlpha(DRAWING_CACHE_QUALITY_AUTO);
		mPaint.setColor(Color.RED);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}

	public ArrayList<PointFBean> getmArrayListPointbeans() {
		return mArrayListPointbeans;
	}

	public void setmArrayListPointbeans(
			ArrayList<PointFBean> mArrayListPointbeans) {
		this.mArrayListPointbeans = mArrayListPointbeans;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/**
		 * 注意事件响应顺序 action.down//第一次按下 pointer.down//第二次按下
		 * pointer.up//第一次抬起（触发actionPointer事件，无法确定是那个手指触发） action.up//第二次抬起
		 */

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			{
				float px = event.getX();
				float py = event.getY();
				int id = event.getPointerId(0);

				if (getPreesedCount() < 2) {
					PointFBean pbf = findPointByCoordinate(px, py);
					if (null == pbf) {
						
					} else {
						handActionDown(pbf, id);
					}
				}
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN: {

			clearPressed();// 重置所有的点

			float x0 = event.getX(0);
			float y0 = event.getY(0);

			PointFBean p0 = findPointByCoordinate(x0, y0);
			if (null != p0) {
				handActionDown(p0, 0);
			} else {
				Log.e(tag, " 找不到p0点");
			}

			int pointIndex = event.getPointerCount() - 1;
			int id = event.getPointerId(pointIndex);

			float x1 = event.getX(pointIndex);
			float y1 = event.getY(pointIndex);

			PointFBean p1 = findPointByCoordinate(x1, y1);
			if (null != p1) {
				handActionDown(p1, id);
			} else {
				Log.i(tag, "找不到p1点");
			}
		}
			break;

		case MotionEvent.ACTION_MOVE: {

			if (!GlobalVariables.BOOLEAN_LOCK_KEYBOAED) {
				float x0 = event.getX(0);
				float y0 = event.getY(0);

				givePoint2PointFBean(x0, y0, 0);

				float x1 = event.getX(event.getPointerCount() - 1);
				float y1 = event.getY(event.getPointerCount() - 1);

				int pointIndex = event.getPointerCount() - 1;
				int id = event.getPointerId(pointIndex);

				givePoint2PointFBean(x1, y1, id);
			}
		}
			break;
		case MotionEvent.ACTION_POINTER_UP: {

			int pointIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> 8;
			int id = event.getPointerId(pointIndex);

			PointFBean p1 = findPointByIndex(id);
			if (null != p1) {
				handActionUp(p1);
			} else {
				Log.i(tag, "p1按到了边上去了吧");
			}

		}

			break;

		case MotionEvent.ACTION_UP:
			Log.i(tag, "action_up");
			long dt = event.getDownTime();
			long et = event.getEventTime();

			Log.i(tag, "down time:" + dt + " " + et);
			clearPressed();
			break;
		}

		invalidate();
		return true;
	}

	/**
	 * 找到一个被按下的点
	 * 
	 * @return
	 */
	public PointFBean findAPressedPoint() {
		PointFBean pfb = null;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean tpf = mArrayListPointbeans.get(i);
			if (tpf.isPressd()) {
				pfb = tpf;
				break;
			}
		}
		return pfb;
	}

	/**
	 * 根据坐标确定点
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public PointFBean findPointByCoordinate(float x, float y) {
		PointFBean pfb = null;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean tpf = mArrayListPointbeans.get(i);
			if (isInPoint(x, y, tpf, GlobalVariables.FLOAT_BUTTON_RADIUS)) {

				pfb = tpf;
			}
		}
		return pfb;
	}

	/**
	 * 按下某个点索引为index
	 * 
	 * @param x
	 * @param y
	 * @param index
	 * @return
	 */
	public PointFBean pressPointDown(float x, float y, int index) {
		// 按下某个点索引为index
		PointFBean pfb = null;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean temppfb = mArrayListPointbeans.get(i);
			if (isInPoint(x, y, temppfb, GlobalVariables.FLOAT_BUTTON_RADIUS)) {
				temppfb.setPressd(true);
				temppfb.setIndex(index);
				pfb = temppfb;
				break;
			}
		}
		return pfb;
	}

	/**
	 * 释放某个点
	 * 
	 * @param x
	 * @param y
	 * @param index
	 * @return
	 */
	public PointFBean releasePointUp(float x, float y, int index) {
		// 释放某个点
		PointFBean pfb = null;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean temppfb = mArrayListPointbeans.get(i);
			if (temppfb.getIndex() == index /*
											 * && isInPoint(x, y, temppfb,
											 * GlobalVariables
											 * .FLOAT_BUTTON_RADIUS)
											 */) {
				temppfb.setPressd(false);
				temppfb.setIndex(index);
				pfb = temppfb;
				break;
			}
		}

		if (pfb == null) {
		}
		return pfb;
	}

	/**
	 * 发送按键消息daoactivity
	 * 
	 * @param locations
	 */
	void sendMessage(String[] locations) {

		if (handler != null) {
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putStringArray(GlobalVariables.STRING_LOCATION_KEY,
					locations);
			msg.setData(data);
			handler.sendMessage(msg);
		}

	}

	/**
	 * 该方法只能在actionDown处调用，
	 * 
	 * @param px
	 * @param py
	 * @param index
	 */
	public void handActionDown(PointFBean pointfBean, int index) {
		// 重新检测应该有得index
		pointfBean.setIndex(index);
		pointfBean.setPressd(true);
		if (handler != null) {
			// 向外发送按键消息
			sendMessageOutKeyAction(pointfBean, KEY_DOWN);
		}
	}

	/**
	 * 处理手指抬起事件
	 * 
	 * @param p1
	 * @param p2
	 */
	public void handActionUp(PointFBean pbf) {
		pbf.setPressd(false);
		// remoteOperateImpl.keyUp((char) (pbf.getKeyCode()));
		sendMessageOutKeyAction(pbf, KEY_UP);
	}

	public int findIndexCount(int index) {
		int count = 0;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean pfb = mArrayListPointbeans.get(i);
			if (index == pfb.getIndex()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 计算出一个坐标离指定点得位置
	 * 
	 * @param x
	 * @param y
	 * @param pfb
	 * @return
	 */
	public double caculateDistance(float x, float y, PointFBean pfb) {
		double radCircle = Math.sqrt((double) (((x - pfb.getX()) * (x - pfb
				.getX())) + (y - pfb.getY()) * (y - pfb.getY())));
		return radCircle;
	}

	/**
	 * 找出被按下的点
	 * 
	 * @return
	 */
	public PointFBean findPressedPoint(int index) {
		PointFBean pfb = null;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean pointfbean = mArrayListPointbeans.get(i);
			if (pointfbean.getIndex() == index) {
				if (pointfbean.isPressd()) {
					pfb = pointfbean;
					// Log.i(tag, "找了了 index=="+index+" 被按下的点!");
				} else {
					// Log.e(tag, "找了了 index=="+index+" 没有被按下被按下的点!");
				}
			}
		}
		return pfb;
	}

	/**
	 * 根据索引查找点
	 * 
	 * @return
	 */
	public PointFBean findPointByIndex(int index) {
		PointFBean pfb = null;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean pointfbean = mArrayListPointbeans.get(i);
			if (pointfbean.getIndex() == index) {
				pfb = pointfbean;
			}
		}
		return pfb;
	}

	/**
	 * 判断手指是否按到了某一个点上
	 * 
	 * @param px
	 *            按下事件的x坐标
	 * @param py
	 * @param pintF
	 *            自定义的PointFBean
	 * @param R
	 *            圆形按钮的半径,也是判断是否按键的临界值
	 * @return
	 */
	public boolean isInPoint(float px, float py, PointFBean pintF, float R) {
		boolean isIn = false;

		float ballX = pintF.getX();
		float ballY = pintF.getY();

		// 求两点之间的距离
		double radCircle = Math
				.sqrt((double) (((ballX - px) * (ballX - px)) + (ballY - py)
						* (ballY - py)));
		if (radCircle > R) {
			isIn = false;
		} else {
			// 触摸点离圆圈中心的位置小于等于半径，说明手指按到了这个点上面了
			isIn = true;
		}
		return isIn;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mArrayListPointbeans != null) {
			for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
				// 描绘出所有的点
				mPaint.setColor(Color.BLUE);
				PointFBean pointfBean = mArrayListPointbeans.get(i);

				if (pointfBean.isPressd() && pointfBean.getIndex() != -1) {
					mPaint.setStyle(Style.FILL);
				} else {
					mPaint.setStyle(Style.STROKE);
				}
				canvas.drawCircle(pointfBean.getX(), pointfBean.getY(),
						pointfBean.getRadius(), mPaint);
				canvas.drawColor(0xf0f0f0);
				mPaint.setColor(Color.RED);
				mPaint.setTextSize(18f);

				canvas.drawText(mArrayListPointbeans.get(i).getStringKey(),pointfBean.getX() - 8, pointfBean.getY()+2, mPaint);// 刚好让坐标显示到中间
			}

		}
		super.onDraw(canvas);
	}

	/**
	 * 把某个坐标赋予指定索引点
	 * 
	 * @param x
	 * @param y
	 * @param index
	 */
	public void givePoint2PointFBean(float x, float y, int index) {
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean pb = mArrayListPointbeans.get(i);
			if (null != pb
					&& pb.getIndex() == index
					&& isInPoint(x, y, pb,
							GlobalVariables.FLOAT_BUTTON_RADIUS)) {
				pb.setX(x);
				pb.setY(y);
				break;
			}
		}

	}

	public RemoteOperateImpl getRemoteOperateImpl() {
		return remoteOperateImpl;
	}

	public void setRemoteOperateImpl(RemoteOperateImpl remoteOperateImpl) {
		this.remoteOperateImpl = remoteOperateImpl;
	}

	public int getPreesedCount() {
		int count = 0;
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean pb = mArrayListPointbeans.get(i);
			if (pb.isPressd()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 清除所有按下的状态
	 */
	public void clearPressed() {
		for (int i = 0, isize = mArrayListPointbeans.size(); i < isize; i++) {
			PointFBean pb = mArrayListPointbeans.get(i);
			if (pb.isPressd()) {
				if (handler != null) {
					// 向外发送按键消息
					sendMessageOutKeyAction(pb, KEY_UP);
					Log.e(tag, "send:" + pb.getKeyCode());
				}
			}
			pb.setPressd(false);
			pb.setIndex(-1);
		}
	}

	/**
	 * 发送消息到网络或者activity
	 * 
	 * @param pfb
	 */
	public void sendMessageOutKeyAction(PointFBean pfb, int action) {
		String locations[] = new String[GlobalVariables.INT_LOCATIONS_SIZE];
		locations[GlobalVariables.INT_LOCATIONS_X] = String.valueOf(pfb
				.getX());
		locations[GlobalVariables.INT_LOCATIONS_Y] = String.valueOf(pfb
				.getY());
		locations[GlobalVariables.INT_LOCATIONS_KEY_STRING] = String
				.valueOf(pfb.getStringKey());
		sendMessage(locations);
		if (null != remoteOperateImpl && action == KEY_DOWN) {
			remoteOperateImpl.keyDown((char) (pfb.getKeyCode()));
		} else if (null != remoteOperateImpl && action == KEY_UP) {
			remoteOperateImpl.keyUp((char) (pfb.getKeyCode()));
		}
	}

	@Override
	public boolean onLongClick(View v) {

		return false;
	}

}
