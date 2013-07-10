package com.tantaman.ferox.middleware.message_types;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// written to by 1 thread, read by n threads.
public class TrackedHttpRequest implements ITrackedHttpRequest {
	private final HttpRequest raw;
	private final List<FileUpload> files;
	private final Map<String, Attribute> body;
	private HttpPostRequestDecoder decoder;
	
	public TrackedHttpRequest(HttpRequest rawRequest) {
		raw = rawRequest;
		files = new CopyOnWriteArrayList<>();
		body = new ConcurrentHashMap<>();
	}
	
	public HttpRequest getRawRequest() {
		return raw;
	}
	
	public void addAttribute(Attribute data) {
		body.put(data.getName(), data);
	}
	
	// fileUpload.isInMemory();// tells if the file is in Memory
	// or on File
	// fileUpload.renameTo(dest); // enable to move into another
	// File dest
	// decoder.removeFileUploadFromClean(fileUpload); //remove
	// the File of to delete file
	// isUploadComplete
	public void addFile(FileUpload file) {
		files.add(file);
	}

	@Override
	public Map<String, Attribute> body() {
		return body;
	}

	@Override
	public List<FileUpload> files() {
		return files;
	}
	
	public void dispose() {
		if (decoder != null)
			decoder.cleanFiles();
	}

	public void setDecoder(HttpPostRequestDecoder decoder) {
		this.decoder = decoder;
	}
}
