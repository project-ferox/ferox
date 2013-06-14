package com.tantaman.ferox.api;

public interface IFeroxServer extends Runnable {
	public void runInCurrentThread() throws InterruptedException;
}
