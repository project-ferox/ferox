package com.tantaman.ferox;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import com.tantaman.ferox.api.router.IRoute;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.priv.Invoker;

public class Ferox extends ChannelInboundHandlerAdapter {
	private final IRouter router;
	private Invoker invoker;
	
	public Ferox(IRouter router) {
		this.router = router;
	}
	
	@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> msgs) throws Exception {
        int size = msgs.size();
        try {
            for (int i = 0; i < size; i ++) {
            	messageReceived(ctx, msgs.get(i));
            	
            	ctx.fireMessageReceived(msgs.get(i));
            	
                if (invoker != null && invoker.getClose()) {
                    break;
                }
            }
        } finally {
        	// TODO: is this a safe place to do this?
            msgs.releaseAllAndRecycle();
        }
    }
	
	private void messageReceived(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest)msg;
			String uri = request.getUri();
			HttpMethod method = request.getMethod();
			
			QueryStringDecoder decoder = new QueryStringDecoder(uri);
			String path = decoder.path();
			IRoute route = router.lookup(method.name(), path);
			
			if (route == null) {
				ctx.fireMessageReceived(msg);
				return;
			}
			
			invoker = new Invoker(route, method.name(), path, decoder.parameters());
			invoker.setContext(ctx);
			invoker.request(request);
		}
		
		if (invoker == null) return;
		
		invoker.setContext(ctx);
		
		if (msg instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) msg;
			invoker.content(httpContent);
		}
	}
	
}
