package com.tantaman.ferox.server;

import com.tantaman.ferox.api.IFeroxServerBuilder;
import com.tantaman.ferox.api.IFeroxServerFactories;

public class FeroxServerFactories implements IFeroxServerFactories {
	@Override
	public IFeroxServerBuilder createServerBuilder() {
		return new FeroxServerBuilder();
	}
}
