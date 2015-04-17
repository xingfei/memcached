/**
 * 
 */
package com.xuetangx.memcached;


/**
 * @author xingfei
 *
 */
public interface MemcacheResponse {
	
	void close();
	
	void writeValue(String key, byte[] data, long flags);
	
	void writeValue(String key, byte[] data, long flags, long casUnique);
	
	void writeEnd();
	
	void writeError();
	
	void writeServerError(String msg);
	
	void writeClientError(String msg);

}
