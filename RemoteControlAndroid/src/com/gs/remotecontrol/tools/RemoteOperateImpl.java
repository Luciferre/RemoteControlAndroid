package com.gs.remotecontrol.tools;
//远程命令
import java.io.IOException;

import com.gs.remotecontrol.net.Connector;
import com.gs.remotecontrol.net.INetCallBack;
import com.gs.remotecontrol.utils.GlobalVariables;


public class RemoteOperateImpl implements IRemoteOperate{

	private Connector connector = null;
	private INetCallBack iNetCallBack = null;
	private String tag = "RemoteOperateImpl";

	public RemoteOperateImpl(Connector Connector, INetCallBack iNetCallBack) {
		this.connector = Connector;
		this.iNetCallBack = iNetCallBack;
	}

	@Override
	public String moveMouse(float x, float y) {
		try {
			String message = buildMessage(EventStringSet.MOVE_MOUSE, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			iNetCallBack.OnIntercepted(tag);
		}

		return null;
	}

	public String mouseAdd(float x, float y) {
		try {
			String message = buildMessageMouseAdd(EventStringSet.MOVE_ADD, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			iNetCallBack.OnIntercepted(tag);
		}
		return null;
	}

	@Override
	public String click(float x, float y) {

		try {
			String message = buildMessage(EventStringSet.CLICK, x, y);
			this.connector.writeMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			iNetCallBack.OnIntercepted(tag);
		}
		return null;
	}

	@Override
	public String doubleClick(float x, float y) {

		try {
			String message = buildMessage(EventStringSet.DOUBLE_CLICK, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iNetCallBack.OnIntercepted(tag);
		}

		return null;
	}

	@Override
	public String rightClick(float x, float y) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			String message = buildMessage(EventStringSet.RIGHT_CLICK, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}

		return null;
	}

	public String rightClick2(float x, float y) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			String message = buildMessage(
					GlobalVariables.STRING_COMMAND_RIGHT_CLICK, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String keyDown(int keyCode) {
		// TODO Auto-generated method stub
		try {
			String message = buildMessage(EventStringSet.KEY_DOWN, keyCode);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String keyUp(int keyCode) {
		try {
			String message = buildMessage(EventStringSet.KEY_UP, keyCode);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String mouseDown(float x, float y) {
		try {
			if (x != -1 && y != -1) {
				this.moveMouse(x, y);
			}
			String message = buildMessage(EventStringSet.MOUSE_DOWN, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String mouseUp(float x, float y) {
		try {
			if (x != -1 && y != -1) {
				this.moveMouse(x, y);
			}
			String message = buildMessage(EventStringSet.MOUSE_UP, x, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String mouseWheel(int y) {
		try {
			String message = buildWheelMessage(EventStringSet.MOUSE_WHEEL, y);
			this.connector.writeMessage(message);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendCommand(String command) {
		try {
			this.connector.writeMessage("#" + command);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	public String sendCommandDos(String dos) {
		// TODO Auto-generated method stub
		try {
			this.connector.writeMessage("$" + dos);
		} catch (IOException e) {
			iNetCallBack.OnIntercepted(tag);
			e.printStackTrace();
		}
		return null;
	}

	// -----------------------------------------
	public String buildMessage(String eventString, float x, float y) {
		return eventString + ":" + (int) (x * GlobalVariables.FLOAT_SCALE)
				+ ":" + (int) (y * GlobalVariables.FLOAT_SCALE)+EventStringSet.Split;
	}

	// -----------------------------------------
	public String buildMessageMouseAdd(String eventString, float x, float y) {
		return eventString + ":" + (int) (GlobalVariables.INT_MOUSE_SENSE*x) + ":" + (int) (GlobalVariables.INT_MOUSE_SENSE*y)+EventStringSet.Split;
	}

	public String buildMessage(String eventString, int code) {
		return eventString + ":" + (char) code+EventStringSet.Split;
	}
    public String buildWheelMessage(String eventString, int code){
    	return eventString + ":" +code+EventStringSet.Split;
    }
	public class EventStringSet {

		public static final String MOVE_MOUSE = "moveMouse";
		public static final String MOVE_ADD = "moveAdd";
		public static final String CLICK = "click";
		public static final String DOUBLE_CLICK = "doubleClick";
		public static final String RIGHT_CLICK = "rightClick";
		public static final String KEY_DOWN = "keydown";
		public static final String KEY_UP = "keyup";
		public static final String MOUSE_DOWN = "mouseDown";
		public static final String MOUSE_UP = "mouseUp";
		public static final String MOUSE_WHEEL="mouse_wheel";
		public static final String Split="|";

	}
}
