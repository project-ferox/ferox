package com.tantaman.ferox.channel_middleware;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import com.tantaman.ferox.channel_middleware.message_types.TrackedHttpRequest;
import com.tantaman.ferox.route_middelware.BodyParserHandler;

/**
 * Parses PUT and POST requests.  This will
 * parse all PUT and POST requests on the channel, regardless of what
 * route they are arriving on.
 * 
 * {@link BodyParserHandler} can be attached at the route level
 * to only parse form fields for specific routes.
 * 
 * The {@link HttpRequestConverter} needs to be added to the pipeline before
 * {@link BodyParser}.
 * 
 * @author tantaman
 *
 */
public class BodyParser extends ChannelInboundHandlerAdapter {
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(262144);
	private HttpPostRequestDecoder decoder;
	private TrackedHttpRequest trackedRequest;

	static {
		DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
		// on exit (in normal exit)
		DiskFileUpload.baseDirectory = null; // system temp directory
		DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
		// exit (in normal exit)
		DiskAttribute.baseDirectory = null; // system temp directory
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (decoder != null) {
			decoder.cleanFiles();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		try {
			if (msg instanceof TrackedHttpRequest) {
				trackedRequest = (TrackedHttpRequest)msg;
				HttpRequest request = trackedRequest.getRawRequest();
				// reset msg for correct instanceof check and processing later.
				// FullHttpRequest would come in as a TrackedHttpRequest.
				msg = request;

				try {
					decoder = new HttpPostRequestDecoder(factory, request);
					trackedRequest.setDecoder(decoder);
				} catch (ErrorDataDecoderException e1) {
					e1.printStackTrace();
					ctx.channel().close();
					return;
				} catch (IncompatibleDataDecoderException e1) {
					return;
				}
			}

			if (decoder != null) {
				if (msg instanceof HttpContent) {
					// New chunk is received
					HttpContent chunk = (HttpContent) msg;
					try {
						decoder.offer(chunk);
					} catch (ErrorDataDecoderException e1) {
						e1.printStackTrace();
						ctx.channel().close();
						return;
					}

					readHttpDataChunkByChunk();

					if (chunk instanceof LastHttpContent) {
						reset();
					}
				}
			}
		} finally {
			ctx.fireChannelRead(msg);
		}
	}

	private void readHttpDataChunkByChunk() {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (data != null) {
					// new value
					handleHttpData(data);
				}
			}
		} catch (EndOfDataDecoderException e1) {
			// no more data.
		}
	}

	private void handleHttpData(InterfaceHttpData data) {
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			trackedRequest.addAttribute(attribute);
		} else {
			if (data.getHttpDataType() == HttpDataType.FileUpload) {
				FileUpload fileUpload = (FileUpload) data;
				trackedRequest.addFile(fileUpload);
			}
		}
	}

	private void reset() {
		decoder = null;
	}
}
