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
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import com.tantaman.ferox.api.request_response.IResponse;

public class Response implements IResponse {
	private MessageList<Object> messageList;
	private ChannelHandlerContext ctx;
	private boolean close = false;
	private boolean keepAlive = false;
	private Object userData;
	
	private final HttpHeaders headers;

	public Response() {
		messageList = MessageList.newInstance();
		headers = new DefaultHttpHeaders();
	}

	void setContext(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	void setRequest(HttpRequest request) {
		keepAlive = isKeepAlive(request);
	}

	@Override
	public void add(Object msg) {
		messageList.add(msg);
	}

	public boolean getClose() {
		return close;
	}

	private void sendFullStringResponse(String response, String contentType, HttpResponseStatus status) {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
		
		// To allow users to specify their own headers
		httpResponse.headers().set(headers);
		httpResponse.headers().set(CONTENT_TYPE, contentType + "; charset=UTF-8");

		keepAlive(httpResponse);

		// need to send this as full response in order
		// to allow fancy transforms down the line
		messageList.add(httpResponse);
		write();
	}
	
	// Someone somewhere else down the line must finish filling out this response.
	private void sendFullObjectResponse(Object response, String contentType, HttpResponseStatus status) {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, status);
		
		// To allow users to specify their own headers
		httpResponse.headers().set(headers);
		httpResponse.headers().set(CONTENT_TYPE, contentType);

		keepAlive(httpResponse);

		// need to send this as full response in order
		// to allow fancy transforms down the line
		messageList.add(httpResponse);
		write();
	}
	
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
	public void send(String response, HttpResponseStatus status) {
		sendFullStringResponse(response, "text/html", status);
	}

	@Override
	public void send(String response, String contentType) {
		sendFullStringResponse(response, contentType, HttpResponseStatus.OK);
	}

	@Override
	public void send(String response, String contentType, HttpResponseStatus status) {
		sendFullStringResponse(response, contentType, status);
	}

	@Override
	public void send(Object response, String contentType, HttpResponseStatus status) {
		sendFullObjectResponse(response, contentType, status);
	}

	@Override
	public void send(Object response, HttpResponseStatus status) {
		sendFullObjectResponse(response, "application/json", status);
	}

	@Override
	public void send(String response) {
		sendFullStringResponse(response, "text/html", HttpResponseStatus.OK);
	}

	@Override
	public void send(Object response) {
		sendFullObjectResponse(response, "application/json", HttpResponseStatus.OK);
	}

	@Override
	public void send(Object response, String contentType) {
		sendFullObjectResponse(response, contentType, HttpResponseStatus.OK);
	}

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

	public ChannelFuture writePartial() {
		MessageList<Object> temp = messageList;
		messageList = MessageList.newInstance();
		ChannelFuture f = ctx.write(temp);

		return f;
	}

	@Override
	public HttpHeaders headers() {
		return headers;
	}
}
