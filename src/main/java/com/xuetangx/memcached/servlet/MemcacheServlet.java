/**
 * 
 */
package com.xuetangx.memcached.servlet;

import com.xuetangx.memcached.MemcacheResponse;
import com.xuetangx.memcached.codec.MemcacheRequest;
import com.xuetangx.memcached.codec.MemcacheRequests.CasRequest;
import com.xuetangx.memcached.codec.MemcacheRequests.GetRequest;
import com.xuetangx.memcached.codec.MemcacheRequests.QuitRequest;
import com.xuetangx.memcached.codec.MemcacheRequests.SetRequest;
import com.xuetangx.memcached.exceptions.MemcacheException;

/**
 * @author xingfei
 *
 */
public interface MemcacheServlet {
	
	default void init(ServletContext context) throws Exception {
		
	}
	
	default void destroy() {
		
	}

	default void service(MemcacheRequest req, MemcacheResponse resp) {
		System.out.println("MemcacheServlet req:" + req + " command:"
				+ req.getCommand());

		try {
			switch (req.getCommand()) {
			case MemcacheRequest.GET:
				doGet((GetRequest) req, resp);
				break;
			case MemcacheRequest.SET:
				doSet((SetRequest) req, resp);
				break;
			case MemcacheRequest.CAS:
				doCas((CasRequest) req, resp);
				break;
			case MemcacheRequest.QUIT:
				doQuit((QuitRequest) req, resp);
				break;
			default:
				resp.writeError();
			}
		} catch (MemcacheException me) {
			resp.writeServerError(me.getMessage());
		}
	}

	default void doGet(GetRequest req, MemcacheResponse resp) {
	}

	default void doQuit(QuitRequest req, MemcacheResponse resp) {
		resp.close();
	}

	// set add replace prepend append
	default void doSet(SetRequest req, MemcacheResponse resp) {

	}

	// cas
	default void doCas(CasRequest req, MemcacheResponse resp) {

	}

}
