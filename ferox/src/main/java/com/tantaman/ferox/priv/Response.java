package com.tantaman.ferox.priv;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tantaman.ferox.api.request_response.IFineGrainedResponse;
import com.tantaman.ferox.api.request_response.IResponse;

public class Response implements IResponse {
	private MessageList<Object> messageList;
	private ChannelHandlerContext ctx;
	private boolean close = false;
	private boolean keepAlive = false;
	private Object userData;
	private final FineGrainedControl fineGrained = new FineGrainedControl();
	
	private final HttpHeaders headers;

	public Response() {
		messageList = MessageList.newInstance();
		headers = new DefaultHttpHeaders();
		headers.set(HttpHeaders.Names.ACCEPT_RANGES, "bytes");
	}

	void setContext(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	void setRequest(HttpRequest request) {
		keepAlive = isKeepAlive(request);
		if (keepAlive) {
			headers.set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
	}

	public boolean getClose() {
		return close;
	}

	private ChannelFuture sendFullStringResponse(String response, String contentType, HttpResponseStatus status) {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
		
		// To allow users to specify their own headers
		httpResponse.headers().set(headers);
		httpResponse.headers().set(CONTENT_TYPE, contentType + "; charset=UTF-8");

		keepAlive(httpResponse);

		// need to send this as full response in order
		// to allow fancy transforms down the line
		messageList.add(httpResponse);
		return fineGrained.write();
	}
	
	// Someone somewhere else down the line must finish filling out this response.
	private ChannelFuture sendFullObjectResponse(Object response, String contentType, HttpResponseStatus status) {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, status);
		
		// To allow users to specify their own headers
		httpResponse.headers().set(headers);
		httpResponse.headers().set(CONTENT_TYPE, contentType);

		keepAlive(httpResponse);

		// need to send this as full response in order
		// to allow fancy transforms down the line
		messageList.add(httpResponse);
		return fineGrained.write();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getUserData() {
		return (T)userData;
	}
	@Override
	public void setUserData(Object data) {
		userData = data;
	}
	
	private void keepAlive(FullHttpResponse httpResponse) {
		if (keepAlive) {
			// Add 'Content-Length' header only for a keep-alive connection.
			httpResponse.headers().set(CONTENT_LENGTH, httpResponse.content().readableBytes());
			// Add keep alive header as per:
			// - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			httpResponse.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
	}

	@Override
	public ChannelFuture send(String response, HttpResponseStatus status) {
		return sendFullStringResponse(response, "text/html", status);
	}

	@Override
	public ChannelFuture send(String response, String contentType) {
		return sendFullStringResponse(response, contentType, HttpResponseStatus.OK);
	}

	@Override
	public ChannelFuture send(String response, String contentType, HttpResponseStatus status) {
		return sendFullStringResponse(response, contentType, status);
	}

	@Override
	public ChannelFuture send(Object response, String contentType, HttpResponseStatus status) {
		return sendFullObjectResponse(response, contentType, status);
	}

	@Override
	public ChannelFuture send(Object response, HttpResponseStatus status) {
		return sendFullObjectResponse(response, "application/json", status);
	}

	@Override
	public ChannelFuture send(String response) {
		return sendFullStringResponse(response, "text/html", HttpResponseStatus.OK);
	}

	@Override
	public ChannelFuture send(Object response) {
		return sendFullObjectResponse(response, "application/json", HttpResponseStatus.OK);
	}

	@Override
	public ChannelFuture send(Object response, String contentType) {
		return sendFullObjectResponse(response, contentType, HttpResponseStatus.OK);
	}
	
	@Override
	public ChannelFuture redirect(String uri,
			Map<String, String> queryParams) {
		if (queryParams != null && !queryParams.isEmpty()) {			
			if (!uri.contains("?")) {
				uri = uri + "?";
			} else {
				uri = uri + "&";
			}
			
			StringBuilder uriBuilder = new StringBuilder(uri);
			boolean first = true;
			
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				if (!first) {
					uriBuilder.append("&");
				} else {
					first = false;
				}
				
				try {
					uriBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			uri = uriBuilder.toString();
		}

		headers.set(HttpHeaders.Names.LOCATION, uri);
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
		
		httpResponse.headers().set(headers);

		keepAlive(httpResponse);

		messageList.add(httpResponse);
		return fineGrained.write();
	}

	@Override
	public HttpHeaders headers() {
		return headers;
	}
	
	@Override
	public IFineGrainedResponse fineGrained() {
		return fineGrained;
	}
	
	private class FineGrainedControl implements IFineGrainedResponse {
		private AtomicBoolean headersAdded = new AtomicBoolean(false);
		
		private void checkHeaderAddition() {
			if (!headersAdded.compareAndSet(false, true)) {
				throw new IllegalStateException("Headers already added");
			}
		}
		
		@Override
		public void addResponseHeaders() {
			addResponseHeaders(HttpResponseStatus.OK);
		}

		@Override
		public void addResponseHeaders(HttpResponseStatus status) {
			checkHeaderAddition();
			DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
			response.headers().set(headers);
			messageList.add(response);
		}

		@Override
		public void add(Object msg) {
			messageList.add(msg);
		}

		// TODO: make writePartial thread safe?
		@Override
		public ChannelFuture write() {
			MessageList<Object> temp = messageList;
			messageList = MessageList.newInstance();
			ChannelFuture f = ctx.write(temp);

			if (!keepAlive) {
				close = true;
				f.addListener(ChannelFutureListener.CLOSE);
			}

			return f;
		}

		@Override
		public ChannelFuture writePartial() {
			MessageList<Object> temp = messageList;
			messageList = MessageList.newInstance();
			ChannelFuture f = ctx.write(temp);

			return f;
		}
		
		@Override
		public void close() {
			ctx.close();
		}
	}
}
