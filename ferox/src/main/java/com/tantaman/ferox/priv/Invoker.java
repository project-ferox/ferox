package com.tantaman.ferox.priv;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.api.request_response.IRequestChainer;
import com.tantaman.ferox.api.request_response.IResponse;
import com.tantaman.ferox.api.router.IRoute;
import com.tantaman.ferox.api.router.IRouteHandler;
import com.tantaman.ferox.api.router.IRouteHandlerFactory;
import com.tantaman.ferox.api.router.IRouteSegment;
import com.tantaman.ferox.api.router.IRouteSegment.Type;
import com.tantaman.ferox.util.IPair;

public class Invoker {
	private final List<IRouteHandler> handlers;
	private Map<String, String> urlParameters;
	private Map<String, List<String>> querystringParameters;
	private List<String> splats;
	private String catchall;
	private Response response;
	
	public Invoker(IRoute route, String method, String path, Map<String, List<String>> querystringParameters) {
		handlers = new LinkedList<>();
		
		for (IPair<Integer, IRouteHandlerFactory> f : route.getHandlers()) {
			handlers.add(f.getSecond().create());
		}
		
		this.querystringParameters = querystringParameters;
		
		urlParameters = new HashMap<String, String>();
		splats = new ArrayList<>();
		response = new Response();
		extractUrlParameters(route, path);
	}
	
	private void extractUrlParameters(IRoute route, String path) {
		String [] parts = path.split("/");
		int i = 0;
		for (IRouteSegment seg : route) {
			if (seg.type() == Type.CATCHALL) {
				StringBuilder sb = new StringBuilder();
				for (; i < parts.length; ++i) {
					sb.append(parts[i] + "/");
				}
				catchall = sb.toString();
			} else {
				if (i < parts.length)
					seg.extract(parts[i], urlParameters, splats);
			}
			++i;
		}
	}
	
	public void setContext(ChannelHandlerContext ctx) {
		response.setContext(ctx);
	}
	
	public boolean getClose() {
		return response.getClose();
	}

	public void request(HttpRequest request) {
		response.setRequest(request);
		new Chainer(handlers.iterator(), response).request(new com.tantaman.ferox.priv.HttpRequest(request, urlParameters, querystringParameters, splats, catchall));
	}
	
	public void content(HttpContent content) {
		new Chainer(handlers.iterator(), response).content(new com.tantaman.ferox.priv.HttpContent(content, urlParameters, querystringParameters, splats, catchall));
	}
	
	public void lastContent(HttpContent content) {
		new Chainer(handlers.iterator(), response).lastContent(new com.tantaman.ferox.priv.HttpContent(content, urlParameters, querystringParameters, splats, catchall));
	}
	
	private static class Chainer implements IRequestChainer {
		private final Iterator<IRouteHandler> handlers;
		private final IResponse response;
		
		public Chainer(Iterator<IRouteHandler> handlers, IResponse response) {
			this.handlers = handlers;
			this.response = response;
		}
		
		@Override
		public void request(IHttpRequest request) {
			if (handlers.hasNext()) {
				handlers.next().request(request, this.response, this);
			}
		}
		
		@Override
		public void content(IHttpContent content) {
			if (handlers.hasNext()) {
				handlers.next().content(content, this.response, this);
			}
		}
		
		@Override
		public void lastContent(IHttpContent content) {
			if (handlers.hasNext()) {
				handlers.next().lastContent(content, this.response, this);
			}
		}
	}
}
