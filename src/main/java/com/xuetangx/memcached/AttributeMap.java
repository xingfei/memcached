/**
 * 
 */
package com.xuetangx.memcached;

/**
 * @author xingfei
 *
 */
public interface AttributeMap {
	
	public void setAttribute(String key, Object value);
	
	public <T> T getAttribute(String key);
	
	public void clear();

}
