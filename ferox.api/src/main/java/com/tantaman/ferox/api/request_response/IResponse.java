package com.tantaman.ferox.api.request_response;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.concurrent.EventExecutor;

import java.util.Map;

import com.tantaman.ferox.api.router.IRouteHandler;

/**
 * Object used by {@link IRouteHandler}s to construct an Http response.<br/><br/>
 * 
 * Several convenience methods are provided for the common cases
 * of sending text content or an object as JSON.<br/><br/>
 * 
 * When using the <code>send</code> methods <code>write</code> does not need to be called.
 * 
 * @author tantaman
 *
 */
public interface IResponse {
	/**
	 * Get the executor that is responsible for servicing the given route.<br/><br/>
	 * 
	 * Say you make some call to a service that calls you back on a different thread.
	 * If you want to keep things simple and not deal with locking you can throw that
	 * callback on the response's executor.
	 * 
	 * @return
	 */
	public EventExecutor executor();
	
	/**
	 * The headers that will be sent as a part of the response.
	 * 
	 * @return
	 */
	public HttpHeaders headers();
	
	/**
	 * Send a 400, text/html response whose body is represented by the provided string.
	 * 
	 * @param response The response body
	 * @return
	 */
	public ChannelFuture send(String response);	// assumes html
	
	/**
	 * Send a 400, contentType response whose body is represented by the provided string.
	 * 
	 * @param response The response body
	 * @param contentType The content type (e.g., "text/html", "application/json")
	 * @return
	 */
	public ChannelFuture send(String response, String contentType);
	
	/**
	 * Send the string as the response body with the given {@link HttpResponseStatus} and
	 * content-type: text/html
	 * 
	 * @param response
	 * @param status
	 * @return
	 */
	public ChannelFuture send(String response, HttpResponseStatus status);
	
	/** 
	 * @param response Content
	 * @param contentType type
	 * @param status status
	 * @return
	 */
	public ChannelFuture send(String response, String contentType, HttpResponseStatus status);
	
	/**
	 * Send a 400, "application/json" response with the given object converted to JSON.<br/><br/>
	 * 
	 * Requires a JSON converter in the channel pipeline or route pipeline.
	 * 
	 * @param response Object to be converted to JSON
	 * @return
	 */
	public ChannelFuture send(Object response);	// assumes json
	
	/**
	 * Send the object with the specified content type.  Object would generally be converted
	 * by someone else in the channel pipeline.
	 * @param response
	 * @param contentType
	 * @return
	 */
	public ChannelFuture send(Object response, String contentType);
	public ChannelFuture send(Object response, HttpResponseStatus status);
	public ChannelFuture send(Object response, String contentType, HttpResponseStatus status);
	
	/**
	 * Attach some user data to the response object.  This user data can be pulled out and
	 * used by {@link IRouteHandler}s further down the pipeline.
	 * @param data
	 */
	public void setUserData(Object data);
	/**
	 * Get the user data that was set by an {@link IRouteHandler} earlier in the pipeline.
	 * @return
	 */
	public <T> T getUserData();
	
	/**
	 * Get the interface that provides finer control over the constructed http response.
	 * @return
	 */
	public IFineGrainedResponse fineGrained();

	/**
	 * Send a redirect to the provided path with the specified query params.
	 * @param uri
	 * @param queryParams
	 * @return
	 */
	public ChannelFuture redirect(String path, Map<String, String> queryParams);
}
