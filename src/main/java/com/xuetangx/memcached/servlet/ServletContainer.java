/**
 * 
 */
package com.xuetangx.memcached.servlet;

/**
 * @author xingfei
 *
 */
public class ServletContainer {
	
	private MemcacheServlet servlet;
	
	private ServletListener listener;
	
	private ServletContext context;

	public void start() throws Exception {
		if (listener != null) {
			listener.contextStart(context);
		}
		
		servlet.init(context);
	}
	
	public void destroy() {
		servlet.destroy();

		if (listener != null) {
			listener.destroy();
		}
		
		servlet = null;
		context = null;
		listener = null;
	}

	public MemcacheServlet getServlet() {
		return servlet;
	}

	public void setServlet(MemcacheServlet servlet) {
		this.servlet = servlet;
	}

	public ServletListener getListener() {
		return listener;
	}

	public void setListener(ServletListener listener) {
		this.listener = listener;
	}

	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}
	
	
}
