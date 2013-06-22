package com.tantaman.ferox.api.router;



public interface IRouter {
	public IRoute lookup(String method, String route);
}
