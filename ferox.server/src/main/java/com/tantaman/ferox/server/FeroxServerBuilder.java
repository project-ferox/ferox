package com.tantaman.ferox.server;

import java.util.LinkedList;
import java.util.List;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.server.IFeroxServer;
import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.server.priv.FeroxChannelInitializer;
import com.tantaman.ferox.util.IPair;
import com.tantaman.ferox.util.Pair;


public class FeroxServerBuilder implements IFeroxServerBuilder {
	private int port;
	private final List<IPair<String, IChannelHandlerFactory>> handlerFactories;
	
//	private static class LazySSL {
//		private static final SSLContext SERVER_CONTEXT;
//
//	    static {
//	        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
//	        if (algorithm == null) {
//	            algorithm = "SunX509";
//	        }
//
//	        SSLContext serverContext;
//	        SSLContext clientContext;
//	        try {
//	            KeyStore ks = KeyStore.getInstance("JKS");
//	            ks.load(SecureChatKeyStore.asInputStream(),
//	                    SecureChatKeyStore.getKeyStorePassword());
//
//	            // Set up key manager factory to use our key store
//	            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
//	            kmf.init(ks, SecureChatKeyStore.getCertificatePassword());
//
//	            // Initialize the SSLContext to work with our key managers.
//	            serverContext = SSLContext.getInstance(PROTOCOL);
//	            serverContext.init(kmf.getKeyManagers(), null, null);
//	        } catch (Exception e) {
//	            throw new Error(
//	                    "Failed to initialize the server-side SSLContext", e);
//	        }
//
//	        SERVER_CONTEXT = serverContext;
//	    }
//	}
	
	public FeroxServerBuilder() {
		handlerFactories = new LinkedList<>();
	}
	
	@Override
	public IFeroxServerBuilder ssl(boolean useSsl) {
//		if (useSsl) {
//			final SSLEngine engine = LazySSL.SERVER_CONTEXT.createSSLEngine();
//			engine.setUseClientMode(false);
//			handlerFactories.add(0, new Pair<String, IChannelHandlerFactory>("ssl", new IChannelHandlerFactory() {
//				
//				@Override
//				public ChannelHandler create() {
//					return new SslHandler(engine);
//				}
//			}));
//		}
		
		return this;
	}

	public FeroxServerBuilder port(int port) {
		this.port = port;
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
