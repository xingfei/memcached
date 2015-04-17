/**
 * 
 */
package com.xuetangx.memcached.servlet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingfei
 *
 */
public class DefaultServletContext implements ServletContext {
	private Map<String, Object> contextParams, attributes;

	public DefaultServletContext(Map<String, Object> contextParams) {
		super();
		this.contextParams =  contextParams;
		this.attributes = new HashMap<>();
	}
	
	public DefaultServletContext() {
		this(new HashMap<>());
	}

	@Override
	public Map<String, Object> getContextParams() {
		return contextParams;
	}

	@Override
	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String key) {
		return (T)this.attributes.get(key);
	}

	@Override
	public void clear() {
		this.attributes.clear();
	}

}
