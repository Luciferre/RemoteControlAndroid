package com.gs.remotecontrol.net;

public interface INetCallBack {

	
	public void OnStart();
	public void OnFinish();
	public void OnIntercepted(String source);
}
