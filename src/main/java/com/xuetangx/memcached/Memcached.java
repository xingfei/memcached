/**
 * 
 */
package com.xuetangx.memcached;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import com.xuetangx.memcached.codec.MemcacheRequestDecoder;
import com.xuetangx.memcached.codec.MemcacheValueEncoder;
import com.xuetangx.memcached.servlet.ServletContainer;
import com.xuetangx.memcached.servlet.ServletInitializer;

/**
 * @author xingfei
 *
 */
public class Memcached extends ChannelInitializer<NioSocketChannel> implements GenericFutureListener<Future<Void>> {

	static final String PORT = "21211";

	private int port = 21211;
	private ServletContainer container;

	public Memcached(int port) throws Exception {
		this.port = port;
		
		ServletInitializer init = new ServletInitializer();
		container = init.initServletContainer();
	}

	public Memcached() throws Exception {
		this(Integer.parseInt(System.getProperty("port", PORT)));
	}

	@Override
	public void initChannel(NioSocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		// p.addLast(new LoggingHandler(LogLevel.INFO));
		p.addLast("requestDecoder", new MemcacheRequestDecoder());
		p.addLast("serverHandler", new ServerHandler(container));

		p.addFirst("messageEncoder", new MemcacheValueEncoder());
	}

	private void run() throws Exception {
		container.start();

		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap()
					.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(this);

			// Start the server.
			ChannelFuture f = b.bind(port).sync();

			// Wait until the server socket is closed.
			f.channel().closeFuture().addListener(this).sync();
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new Memcached().run();
	}

	@Override
	public void operationComplete(Future<Void> future) throws Exception {
		if (container != null) {
			container.destroy();
		}
	}

}
