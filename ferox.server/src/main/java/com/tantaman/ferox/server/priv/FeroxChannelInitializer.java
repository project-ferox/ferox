package com.tantaman.ferox.server.priv;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.Collection;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.util.IPair;

public class FeroxChannelInitializer extends ChannelInitializer<SocketChannel> {
	private final Collection<IPair<String, IChannelHandlerFactory>> handlerFactories;
	
	public FeroxChannelInitializer(Collection<IPair<String, IChannelHandlerFactory>> handlerFactories) {
		this.handlerFactories = new ArrayList<>(handlerFactories);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
		for (IPair<String, IChannelHandlerFactory> entries : handlerFactories) {
			p.addLast(entries.getFirst(), entries.getSecond().create());
		}
	}

}
