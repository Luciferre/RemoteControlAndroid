package com.gs.remotecontrol.net;

import java.util.HashMap;
import java.util.Map.Entry;


public class ConnectorPool {
	public static final String STRING_CKEY="ConnectorPool";

	public static HashMap<String, Connector> mapConnectorPool = new HashMap<String, Connector>();
	
	public static void putConnector(String key,Connector connector){
		//参数传进来的key，留待后用
		clearPool();
		mapConnectorPool.put(STRING_CKEY, connector);
	}
	
    public static Connector getConnectorByKey(String key){
    	return mapConnectorPool.get(key);
    }
    
    public static int getConnectorPoolSize(){
    	return mapConnectorPool.size();
    }
    public static void clearPool(){
    	for(Entry<String,Connector> entry:mapConnectorPool.entrySet()){
    		entry.getValue().killSelf();
    	}
    	mapConnectorPool.clear();
    }
}
