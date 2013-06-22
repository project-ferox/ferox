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
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import com.tantaman.ferox.api.request_response.IHeaderBuilder;
import com.tantaman.ferox.api.request_response.IResponse;


public class Response implements IResponse {
	private MessageList<Object> messageList;
	private ChannelHandlerContext ctx;
	private boolean close = false;
	private boolean keepAlive = false;

	public Response() {
		messageList = MessageList.newInstance();
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

	private void sendFullResponse(String response, String contentType, HttpResponseStatus status) {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));

		httpResponse.headers().set(CONTENT_TYPE, contentType + "; charset=UTF-8");

		// keep alive stuff?
		if (keepAlive) {
			// Add 'Content-Length' header only for a keep-alive connection.
			httpResponse.headers().set(CONTENT_LENGTH, httpResponse.content().readableBytes());
			// Add keep alive header as per:
			// - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			httpResponse.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}

		messageList.add(httpResponse);
		write();
	}

	@Override
	public void send(String response, HttpResponseStatus status) {
		sendFullResponse(response, "text/html", status);
	}

	@Override
	public void send(String response, String contentType) {
		sendFullResponse(response, contentType, HttpResponseStatus.OK);
	}

	@Override
	public void send(String response, String contentType, HttpResponseStatus status) {
		sendFullResponse(response, contentType, status);
	}

	@Override
	public void send(Object response, String contentType, HttpResponseStatus status) {
		
	}

	@Override
	public void send(Object response, HttpResponseStatus status) {

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
	public IHeaderBuilder getHeaderBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHeaders getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(String response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Object response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Object response, String contentType) {
		// TODO Auto-generated method stub
		
	}
}
