package com.tantaman.ferox.server;

import java.util.LinkedList;
import java.util.List;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxServer;
import com.tantaman.ferox.api.IFeroxServerBuilder;
import com.tantaman.ferox.server.priv.FeroxChannelInitializer;
import com.tantaman.ferox.util.IPair;
import com.tantaman.ferox.util.Pair;


public class FeroxServerBuilder implements IFeroxServerBuilder {
	private int port;
	private boolean ssl;
	private final List<IPair<String, IChannelHandlerFactory>> handlerFactories;
	
	public FeroxServerBuilder() {
		handlerFactories = new LinkedList<>();
	}
	
	/*
	 * 
        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //p.addLast("ssl", new SslHandler(engine));

        p.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        //p.addLast("aggregator", new HttpObjectAggregator(1048576));
        p.addLast("encoder", new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        //p.addLast("deflater", new HttpContentCompressor());
        p.addLast("handler", new HttpSnoopServerHandler());
        p.addLast("chunkedWriter", new ChunkedWriteHandler());
	 */
	
	public FeroxServerBuilder port(int port) {
		this.port = port;
		return this;
	}
	
	public FeroxServerBuilder ssl(boolean useSsl) {		
		this.ssl = useSsl;
		return this;
	}
	
	// So we can decide where in the pipeline to put it.
	public FeroxServerBuilder useFerox(final IChannelHandlerFactory ferox) {
		use("ferox", ferox);
		
		return this;
	}
	
	public FeroxServerBuilder use(String identifier, IChannelHandlerFactory factory) {
		handlerFactories.add(new Pair<String, IChannelHandlerFactory>(identifier, factory));
		return this;
	}
	
	public IFeroxServer build() {
		FeroxChannelInitializer fci = new FeroxChannelInitializer(handlerFactories);
		return new FeroxServer(port, fci);
	}
}
