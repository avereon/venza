package com.avereon.zerra.javafx;

import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class Fx {

	public static void startup() {
		try {
			Fx.run( () -> {} );
		} catch( IllegalStateException exception ) {
			Platform.startup( () -> {} );
		}
	}

	// Convenience method to call Platform.runLater
	public static void run( Runnable runnable ) {
		Platform.runLater( runnable );
	}

	public static boolean isRunning() {
		try {
			Fx.run( () -> {} );
			return true;
		} catch( Throwable throwable ) {
			return false;
		}
	}

	public static boolean isFxThread() {
		return Platform.isFxApplicationThread();
	}

	public static void checkFxThread() {
		if( !isFxThread() ) throw new IllegalStateException( "Not on FX thread; thread=" + Thread.currentThread().getName() );
	}

	public static void waitFor( long timeout ) {
		try {
			doWaitForWithInterrupt( timeout );
		} catch( InterruptedException exception ) {
			// Intentionally ignore exception
		}
	}

	public static void waitFor( int count, long timeout ) {
		for( int index = 0; index < count; index++ ) {
			waitFor( timeout );
		}
	}

	public static void waitForWithInterrupt( long timeout ) throws InterruptedException {
		waitForWithInterrupt( 2, timeout );
	}

	public static void waitForWithInterrupt( int count, long timeout ) throws InterruptedException {
		for( int index = 0; index < count; index++ ) {
			doWaitForWithInterrupt( timeout );
		}
	}

	private static void doWaitForWithInterrupt( long timeout ) throws InterruptedException {
		WaitToken token = new WaitToken();
		Fx.run( token );
		token.waitFor( timeout, TimeUnit.MILLISECONDS );
	}

	private static class WaitToken implements Runnable {

		boolean released;

		public synchronized void run() {
			this.released = true;
			this.notifyAll();
		}

		public synchronized void waitFor( long timeout, TimeUnit unit ) throws InterruptedException {
			if( !released ) unit.timedWait( this, timeout );
		}

	}
}
