package com.tantaman.ferox.api.request_response;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.IDisposable;
import com.tantaman.ferox.api.router.IRouteHandler;

/**
 * Super interface for {@link IHttpContent} and {@link IHttpRequest}
 * 
 * This represents the parameters available to a {@link IRouteHandler}
 * when receiving an HTTP request and associated content.
 * 
 * These methods can be called on any {@link IHttpReception} object and will
 * return valid values.
 * 
 * @author tantaman
 *
 */
public interface IHttpReception {
	/**
	 * The URL parameters that were extracted from the requested URI.<br/><br/>
	 * 
	 * e.g.:<br/><br/>
	 * 
	 * <code>
	 * routerBuilder.get("/:collection/:id", handler);
	 * </code><br/><br/>
	 * 
	 * <code>getUrlParam("collection")</code> will return the collection that was extracted from the URL.<br/>
	 * <code>getUrlParam("id")</code> will return the id that was extracted from the URL.
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public String getUrlParam(String key);
	
	/**
	 * Returns the value of the query string parameter associated with the given key.<br/><br/>
	 * 
	 * E.g.:<br/>
	 * If the URL is: <code>http://example.com/?user=james&id=1</code><br/>
	 * <br/>
	 * then <code>getQueryParam("user")</code> will return "james"
	 * <code>getQueryParam("id")</code> will return "1"
	 * <br/><br/>
	 * If no value is associated with the provided key then the empty string is returned.
	 * 
	 * @param key Key
	 * @return value associated with key
	 */
	public String getQueryParam(String key);
	
	/**
	 * Same as <code>getQueryParam(String key)</code> except it allows a default value to be specified when
	 * a mapping for the given key does not exist.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getQueryParam(String key, String defaultValue);
	
	/**
	 * Same as <code>getQueryParam(String key)</code> except this will return all
	 * values that map to the given key since a query string could look like:
	 * <br/>
	 * <code>http://example.com/?user=james&user=jack&user=c3po</code>
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getQueryParams(String key);
	
	/**
	 * Returns the strings that matched the splats in the route.<br/><br/>
	 * 
	 * E.g.:<br/>
	 * If a route is defined as:<br/>
	 * <code>/documents/ * /private/*</code>
	 * <br/><br/>
	 * then <code>getSplats().get(0)</code> will return the first matching segment<br/>
	 * and <code>getSplats().get(1)</code> will return the second matching segment.
	 * 
	 * @return
	 */
	public List<String> getSplats();
	
	/**
	 * Gets the string matched by the catchall.<br/><br/>
	 * 
	 * E.g.:<br/>
	 * registered route:<br/>
	 * <code>/home/**</code><br/>
	 * requested URL:<br/>
	 * <code>/home/james/documents/school</code><br/>
	 * 
	 * <code>request.getCatchall() --> "james/documents/school"</code>
	 * 
	 * @return
	 */
	public String getCatchall();
	
	/**
	 * Returns the HTTP verb used in the request (e.g.: GET, PUT, POST, DELETE, PATCH, OPTIONS, etc.)
	 * @return
	 */
	public HttpMethod getMethod();
	
	/**
	 * Whether or not this is the last {@link IHttpReception} for the given Http request
	 * @return
	 */
	public boolean isLast();
	
	/**
	 * The headers that were sent with the Http request
	 * @return
	 */
	public HttpHeaders getHeaders();
	
	/**
	 * Returns the decoded path of the URI.  Query string parameters are removed from the URI.
	 * The URI is URLDecoded.<br/><br/>
	 * 
	 * <code>
	 * Requested URL: "/home?a=1&b=2"
	 * assert content.getPath().equals("/home")
	 * </code>
	 * 
	 * @return
	 */
	public String getPath();
	
	/**
	 * The URL encoded URI as it was sent in the request.
	 * 
	 * @return
	 */
	public String getUri();
	
	/**
	 * The underlying netty {@link HttpContent} or {@link HttpRequest} object.
	 * @return
	 */
	public Object getRaw();
	
	/**
	 * Any files that may have been uploaded with the request.<br/><br/>
	 * BodyParser or BodyParserHandler would need to have been attached to the channel or route
	 * pipeline for this to be filled in.
	 * 
	 * @return
	 */
	public List<FileUpload> getFiles();
	
	/**
	 * Form parameters that were sent with the request<br/><br/>
	 * 
	 * BodyParser or BodyParserHandler would need to have been attached to the channel or route
	 * pipeline for this to be filled in.
	 * @return
	 */
	public Map<String, Attribute> getBody();
	
	/**
	 * Attach an object to this request that should be disposed of once processing for the route has
	 * completed.
	 * @param disposable
	 */
	public void addDisposable(IDisposable disposable);
	
	/**
	 * Only needs to be called on the last processed
	 * data and only if form data was received or disposables were attached to the request.
	 */
	public void dispose();
}
