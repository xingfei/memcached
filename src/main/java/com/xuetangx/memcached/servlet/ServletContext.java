package com.xuetangx.memcached.servlet;

import java.util.Map;

import com.xuetangx.memcached.AttributeMap;

/**
 * @author xingfei
 *
 */
public interface ServletContext extends AttributeMap {
	
	Map<String, Object> getContextParams();
	
}
