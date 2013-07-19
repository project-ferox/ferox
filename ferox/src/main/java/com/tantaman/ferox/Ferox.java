package com.tantaman.ferox;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;

import com.tantaman.ferox.api.router.IRoute;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.channel_middleware.message_types.TrackedHttpRequest;
import com.tantaman.ferox.priv.Invoker;

public class Ferox extends ChannelInboundHandlerAdapter {
	private final IRouter router;
	private Invoker invoker;
	
	static {
		DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
		// on exit (in normal exit)
		DiskFileUpload.baseDirectory = "resources/temp";
		DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
		// exit (in normal exit)
		DiskAttribute.baseDirectory = "resources/temp";
	}
	
	public Ferox(IRouter router) {
		this.router = router;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof TrackedHttpRequest) {
			invoker = null;
			TrackedHttpRequest trackedRequest = (TrackedHttpRequest)msg;
			HttpRequest request = trackedRequest.getRawRequest();
			// so instanceof checks work correclty below
			msg = request;
			
			String uri = request.getUri();
			HttpMethod method = request.getMethod();
			
			QueryStringDecoder decoder = new QueryStringDecoder(uri);
			String path = decoder.path();
			IRoute route = router.lookup(method.name(), path);
			
			if (route == null) {
				return;
			}
			
			invoker = new Invoker(route, method.name(), path, decoder.parameters(), trackedRequest);
			invoker.setContext(ctx);
			invoker.request(request);
		}
		
		if (invoker == null) return;
		
		invoker.setContext(ctx);
		
		if (msg instanceof LastHttpContent) {
			HttpContent httpContent = (HttpContent) msg;
			invoker.lastContent(httpContent);
		} else if (msg instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) msg;
			invoker.content(httpContent);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		invoker.exceptionCaught(cause);
		invoker.dispose();
		ctx.close();
		cause.printStackTrace();
	}
}
