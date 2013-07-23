package com.tantaman.ferox.api.server;

public interface IFeroxServer extends Runnable {
	public void runInCurrentThread() throws InterruptedException;
	public void shutdown();
}
