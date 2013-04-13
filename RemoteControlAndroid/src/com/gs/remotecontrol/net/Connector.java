package com.gs.remotecontrol.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.graphics.Point;
import android.os.Build;
import android.util.Log;

import com.gs.remotecontrol.utils.GlobalVariables;


public class Connector {

	private Socket mSocketReceive;
	private String mStringRemoteHost;
	private int mIntRemotePort;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
    private String tag="Connector";
    
	public Connector(Socket socket) {
		try {
			this.mSocketReceive = socket;
			this.mSocketReceive.setSendBufferSize(4);
		    this.mStringRemoteHost=socket.getInetAddress().getHostAddress();
		    this.mIntRemotePort=socket.getPort();//1992
			this.dataInputStream = new DataInputStream(this.mSocketReceive.getInputStream());
			this.dataOutputStream = new DataOutputStream(this.mSocketReceive.getOutputStream());
			
			byte[] buffer=new byte[1024];
			this.dataInputStream.read(buffer);
			//返回来的格式为:screen:width:height
			String utf=new String(buffer,0,buffer.length);
			String[] datas=utf.trim().split(":");
			int width=Integer.parseInt(datas[1]);
			int height=Integer.parseInt(datas[2]);
			Point pointScreen=new Point(width,height);
			GlobalVariables.pointHostScreen=pointScreen;
			this.dataOutputStream.write(Build.MODEL.getBytes());//发送手机型号:
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "e:"+e.getMessage());

		}
	}
	public void killSelf() {
//		mBooleanAlive = false;
		try {
			if (null != mSocketReceive && mSocketReceive.isClosed() == false) {
				mSocketReceive.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeMessage(String utf) throws IOException {
		 if(GlobalVariables.BOOLEAN_LOCK_KEYBOAED){
			 //锁定状态才发送命令
//			 this.dataOutputStream.writeUTF(utf);
			 this.dataOutputStream.write(utf.getBytes());
			 this.dataOutputStream.flush();
		 }
	 
}
@Override
	protected void finalize() throws Throwable {
		super.finalize();
		killSelf();
	}
	public String getmStringRemoteHost() {
		return mStringRemoteHost;
	}
}
