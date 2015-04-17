/**
 * 
 */
package com.xuetangx.memcached;

import io.netty.channel.ChannelHandlerContext;

import com.xuetangx.memcached.codec.ResponseContent;
import com.xuetangx.memcached.codec.ResponseContent.ClientErrorContent;
import com.xuetangx.memcached.codec.ResponseContent.EndContent;
import com.xuetangx.memcached.codec.ResponseContent.ErrorContent;
import com.xuetangx.memcached.codec.ResponseContent.ServerErrorContent;
import com.xuetangx.memcached.codec.ResponseContent.ValueContent;

/**
 * @author xingfei
 *
 */
public class ChannelMemcacheResponse implements MemcacheResponse {
	private ChannelHandlerContext ctx;
	
	private ResponseContent endResp = new EndContent(),
			errorResp = new ErrorContent();

	public ChannelMemcacheResponse(ChannelHandlerContext ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public void close() {
		ctx.close();
	}

	@Override
	public void writeEnd() {
		ctx.writeAndFlush(endResp);
	}

	@Override
	public void writeError() {
		ctx.writeAndFlush(errorResp);
	}

	@Override
	public void writeServerError(String msg) {
		ctx.writeAndFlush(new ServerErrorContent(msg == null ? "" : msg));
	}

	@Override
	public void writeClientError(String msg) {
		ctx.writeAndFlush(new ClientErrorContent(msg == null ? "" : msg));
	}

	@Override
	public void writeValue(String key, byte[] data, long flags) {
		ctx.write(new ValueContent(key, data, flags));
	}

	@Override
	public void writeValue(String key, byte[] data, long flags, long casUnique) {
		ctx.write(new ValueContent(key, data, flags, casUnique));
	}

}
