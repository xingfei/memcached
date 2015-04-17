/**
 * 
 */
package com.xuetangx.memcached.servlet;

/**
 * @author xingfei
 *
 */
public interface ServletListener {
	
	public void contextStart(ServletContext context);
	
	public void destroy();

}
