package com.tantaman.ferox.api;


public interface IRouter {
	public IRoute lookup(String method, String route);
}
