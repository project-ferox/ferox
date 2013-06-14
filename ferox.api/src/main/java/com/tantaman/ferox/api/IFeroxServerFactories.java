package com.tantaman.ferox.api;

import aQute.bnd.annotation.component.Component;

@Component
public interface IFeroxServerFactories {
	public IFeroxServerBuilder createServerBuilder();
}
