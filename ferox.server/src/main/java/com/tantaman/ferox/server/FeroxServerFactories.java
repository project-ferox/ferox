package com.tantaman.ferox.server;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.api.server.IFeroxServerFactories;

public class FeroxServerFactories implements IFeroxServerFactories {
	@Override
	public IFeroxServerBuilder createServerBuilder() {
		return new FeroxServerBuilder();
	}
}
