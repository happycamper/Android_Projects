package com.ericsson.interfaces;

import java.util.List;
//import ericsson.msag.ObservedData;

interface CommunicatorInterface {
	String[] getDesc();
	String[] getData();
	String[] getCurrentCache();
	String getCacheItem( String name );
	boolean store( String filename, String data );
	String getLatestCacheItem( String sensorType, String deviceId );
	String[] getCacheItemsInPeriod( String sensorType, String deviceId, String t1, String t2 );
}
