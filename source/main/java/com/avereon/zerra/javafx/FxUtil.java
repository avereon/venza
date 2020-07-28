package com.avereon.zerra.javafx;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BackgroundPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FxUtil {

	public static Pos parseAlign( String align ) {
		switch( align ) {
			case "northwest":
				return Pos.TOP_LEFT;
			case "north":
				return Pos.TOP_CENTER;
			case "northeast":
				return Pos.TOP_RIGHT;
			case "west":
				return Pos.CENTER_LEFT;
			case "center":
				return Pos.CENTER;
			case "east":
				return Pos.CENTER_RIGHT;
			case "southwest":
				return Pos.BOTTOM_LEFT;
			case "south":
				return Pos.BOTTOM_CENTER;
			case "southeast":
				return Pos.BOTTOM_RIGHT;
		}
		return Pos.CENTER;
	}

	public static BackgroundPosition parseBackgroundPosition( String align ) {
		switch( align ) {
			case "northwest":
				return new BackgroundPosition( Side.LEFT, 0, true, Side.TOP, 0, true );
			case "north":
				return new BackgroundPosition( Side.LEFT, 0.5, true, Side.TOP, 0, true );
			case "northeast":
				return new BackgroundPosition( Side.LEFT, 1, true, Side.TOP, 0, true );
			case "west":
				return new BackgroundPosition( Side.LEFT, 0, true, Side.TOP, 0.5, true );
			case "center":
				return new BackgroundPosition( Side.LEFT, 0.5, true, Side.TOP, 0.5, true );
			case "east":
				return new BackgroundPosition( Side.LEFT, 1, true, Side.TOP, 0.5, true );
			case "southwest":
				return new BackgroundPosition( Side.LEFT, 0, true, Side.TOP, 1, true );
			case "south":
				return new BackgroundPosition( Side.LEFT, 0.5, true, Side.TOP, 1, true );
			case "southeast":
				return new BackgroundPosition( Side.LEFT, 1, true, Side.TOP, 1, true );
		}
		return BackgroundPosition.CENTER;
	}

	public static boolean isChildOf( Node node, Node container ) {
		while( (node = node.getParent()) != null ) {
			if( node == container ) return true;
		}
		return false;
	}

	public static boolean isParentOf( TreeItem<?> item, TreeItem<?> child ) {
		TreeItem<?> parent = child;

		while( parent != null ) {
			if( item.equals( parent ) ) return true;
			parent = parent.getParent();
		}

		return false;
	}

	public static Bounds localToParent( Node source, Node target ) {
		return localToParent( source, target, source.getLayoutBounds() );
	}

	public static Bounds localToParent( Node source, Node target, Bounds bounds ) {
		Bounds result = bounds;

		Node parent = source;
		while( parent != null ) {
			if( parent == target ) break;
			result = parent.localToParent( result );
			parent = parent.getParent();
		}

		return result;
	}

	public static Insets add( Insets a, Insets b ) {
		return new Insets( a.getTop() + b.getTop(), a.getRight() + b.getRight(), a.getBottom() + b.getBottom(), a.getLeft() + b.getLeft() );
	}

	public static Bounds add( Bounds a, Insets b ) {
		return new BoundingBox( a.getMinX(), a.getMinY(), a.getWidth() + b.getLeft() + b.getRight(), a.getHeight() + b.getTop() + b.getBottom() );
	}

	public static <T> List<TreeItem<T>> flatTree( TreeItem<T> item ) {
		return flatTree( item, false );
	}

	public static <T> List<TreeItem<T>> flatTree( TreeItem<T> item, boolean includeItem ) {
		List<TreeItem<T>> list = new ArrayList<>();

		if( includeItem ) list.add( item );
		item.getChildren().forEach( ( child ) -> list.addAll( flatTree( child, true ) ) );

		return list;
	}

	public static void assertFxThread() {
		if( !Platform.isFxApplicationThread() ) {
			throw new IllegalStateException( "Not on FX application thread; currentThread = " + Thread.currentThread().getName() );
		}
	}

	public static boolean isFxRunning() {
		try {
			Platform.runLater( () -> {} );
			return true;
		} catch( Throwable throwable ) {
			return false;
		}
	}

	public static void fxWaitIgnoreInterrupted( long timeout ) {
		try {
			fxWait( timeout );
		} catch( InterruptedException exception ) {
			// Intentionally ignore exception
		}
	}

	public static void fxWait( long timeout ) throws InterruptedException {
		WaitToken token = new WaitToken();
		Platform.runLater( token );
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

	/**
	 * Pick the child node in a parent node that contains the scene point. Found
	 * at: http://fxexperience.com/2016/01/node-picking-in-javafx/.
	 *
	 * @param parent
	 * @param sceneX
	 * @param sceneY
	 * @return
	 */
	public static Node pick( Node parent, double sceneX, double sceneY ) {
		Point2D point = parent.sceneToLocal( sceneX, sceneY, true );

		// Check if the given node has the point inside it
		if( !parent.contains( point ) ) return null;

		if( parent instanceof Parent ) {
			Node closest = null;
			List<Node> children = ((Parent)parent).getChildrenUnmodifiable();
			for( int i = children.size() - 1; i >= 0; i-- ) {
				Node child = children.get( i );
				point = child.sceneToLocal( sceneX, sceneY, true );
				if( child.isVisible() && !child.isMouseTransparent() && child.contains( point ) ) {
					closest = child;
					break;
				}
			}

			if( closest != null ) return pick( closest, sceneX, sceneY );
		}

		return parent;
	}

}