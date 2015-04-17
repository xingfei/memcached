/**
 * 
 */
package com.xuetangx.memcached.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author xingfei
 *
 */
public class MemcacheValueEncoder extends MessageToByteEncoder<ResponseContent> {

	@Override
	public void encode(ChannelHandlerContext ctx, ResponseContent msg, ByteBuf out)
			throws Exception {
		System.out.println("writing memcache value:" + msg);
		byte[] bytes = msg.toBytes();
		System.out.println("    value :" + new String(bytes));
		out.writeBytes(bytes);
	}
}
