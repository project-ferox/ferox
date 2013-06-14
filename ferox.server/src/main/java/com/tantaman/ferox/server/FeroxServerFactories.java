package com.tantaman.ferox.server;

import aQute.bnd.annotation.component.Component;

import com.tantaman.ferox.api.IFeroxServerBuilder;
import com.tantaman.ferox.api.IFeroxServerFactories;

@Component
public class FeroxServerFactories implements IFeroxServerFactories {
	@Override
	public IFeroxServerBuilder createServerBuilder() {
		return new FeroxServerBuilder();
	}
}
