package com.tantaman.ferox.server;

import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.api.server.IFeroxServerFactories;

public class FeroxServerFactories implements IFeroxServerFactories {
	@Override
	public IFeroxServerBuilder createServerBuilder() {
		return new FeroxServerBuilder();
	}
}
