package com.tantaman.ferox.server;

import com.tantaman.ferox.api.server.IFeroxServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class FeroxServer implements IFeroxServer {
	private final int port;
	private final ChannelInitializer<SocketChannel> channelInitializer;
	private volatile Channel channel;

	FeroxServer(int port, ChannelInitializer<SocketChannel> channelInitializer) {
		this.port = port;
		this.channelInitializer = channelInitializer;
	}

	public void run() {
		try {
			runInCurrentThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void runInCurrentThread() throws InterruptedException {
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(channelInitializer);

			channel = b.bind(port).sync().channel();
			channel.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	@Override
	public void shutdown() {
		channel.close();
	}
}
