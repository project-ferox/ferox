package com.tantaman.ferox.channel_middleware.message_types;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tantaman.ferox.api.IDisposable;
import com.tantaman.ferox.api.router.IRouteHandler;
import com.tantaman.ferox.channel_middleware.BodyParser;

/**
 * An HTTP request that is being tracked by Ferox.
 * 
 * Regular http requests are converted to {@link ITrackedHttpRequest}.
 * The reason for this is that it gives Ferox an object that it can
 * attach information that pertains to the lifetime of the connection.
 * 
 * The {@link BodyParser} would attach decoded fields to the tracked
 * request as it receives them, for example.
 * 
 * If an {@link IRouteHandler} generates some objects that need to be
 * disposed when the connection terminates it can attach
 * those objects to the {@link ITrackedHttpRequest} by calling addDisposable.
 * 
 * @author tantaman
 *
 */
public interface ITrackedHttpRequest {
	public HttpRequest getRawRequest();
	public Map<String, Attribute> body();
	public List<FileUpload> files();
	public void addDisposable(IDisposable disposable);
	public void dispose();
	public Set<Cookie> getCookies();
}
