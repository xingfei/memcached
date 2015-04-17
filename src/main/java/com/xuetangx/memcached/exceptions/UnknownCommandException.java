/**
 * 
 */
package com.xuetangx.memcached.exceptions;

/**
 * @author xingfei
 *
 */
public class UnknownCommandException extends MemcacheException {

	public UnknownCommandException(String command) {
		super("unknown memcache command:" + command);
	}

	private static final long serialVersionUID = -4394311583136839800L;

}
