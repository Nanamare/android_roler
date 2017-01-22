package com.buttering.roler.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by kinamare on 2017-01-21.
 */

public final class BusProvider {
	private static final Bus BUS = new Bus();

	public static Bus getInstance() {
		return BUS;
	}

	private BusProvider() {
		// No instances.
	}
}

