/**
 * 
 */
package com.xuetangx.memcached;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.xuetangx.memcached.codec.MemcacheRequest;
import com.xuetangx.memcached.exceptions.MemcacheException;
import com.xuetangx.memcached.servlet.ServletContainer;

/**
 * @author xingfei
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
	private ServletContainer container;
	
	public ServerHandler(){}

	public ServerHandler(ServletContainer container) {
		super();
		this.container = container;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		MemcacheRequest request = (MemcacheRequest)msg;
		ChannelMemcacheResponse resp = new ChannelMemcacheResponse(ctx);
		
		request.setServletContext(container.getContext());
		container.getServlet().service(request, resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (cause instanceof MemcacheException) {
			new ChannelMemcacheResponse(ctx).writeServerError(cause.getMessage());
		} else {
			cause.printStackTrace();
		}
		ctx.close();
	}
}
