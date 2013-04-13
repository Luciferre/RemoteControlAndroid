package com.gs.remotecontrol.views;

import java.io.Serializable;


public class PointFBean implements Serializable {
	/**
	 * 记录用户当前点击的点
	 */
	private static final long serialVersionUID = 1L;
	private int radius;
	private String stringKey = "";
	private float X;
	private float Y;
	private int index = -1;
	private boolean isPressd = false;

	public PointFBean(float x, float y) {
		// TODO Auto-generated constructor stub
		this.X = x;
		this.Y = y;
	}

	public PointFBean() {
		// TODO Auto-generated constructor stub
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public String getStringKey() {
		return stringKey;
	}

	public void setStringKey(String stringKey) {
		this.stringKey = stringKey;
	}

	public int getKeyCode() {
		return stringKey.toCharArray()[0];
	}

	public void setKeyCode(int keyCode) {
	}

	@Override
	public boolean equals(Object o) {
		PointFBean pointf = (PointFBean) o;
		if (pointf.getX() == this.X && pointf.getY() == this.Y) {
			return true;
		} else {
			return false;
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isPressd() {
		return isPressd;
	}

	public void setPressd(boolean isPressd) {
		this.isPressd = isPressd;
	}
}
