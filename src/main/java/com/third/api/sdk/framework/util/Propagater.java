package com.third.api.sdk.framework.util;

import com.google.common.base.Throwables;

import static com.google.common.base.Preconditions.checkNotNull;

public class Propagater {
	private Propagater() {
	}

	/**
	 * 
	 * @param throwable
	 * @return
	 */
	public static RuntimeException interrupt(Throwable throwable) {
		checkNotNull(throwable);
		Throwables.throwIfUnchecked(throwable);
		throw new RuntimeException(throwable);
	}

	/**
	 * 
	 * @return
	 */
	public static RuntimeException interrupt() {
		throw new RuntimeException();
	}
	


}
