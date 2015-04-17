/**
 * 
 */
package com.xuetangx.memcached.codec;

import com.xuetangx.memcached.servlet.ServletContext;

/**
 * @author xingfei
 *
 */
public interface MemcacheRequest {
	// get command
	String GET = "get", GETS = "gets";

	// storage command
	String SET = "set", ADD = "add", CAS = "cas", APPEND = "append",
			PREPEND = "prepend", REPLACE = "replace";

	// delete command
	String DELETE = "delete";

	// incr/decr
	String INCR = "incr", DECR = "decr";
	
	// other
	String QUIT = "quit", STATS = "stats";

	String getCommand();
	
	default boolean needData() {
		return false;
	}
	
	ServletContext getServletContext();
	
	void setServletContext(ServletContext context);
	
}
