package com.avereon.venza.image;

@Deprecated
public abstract class ProgramIcon extends ProgramImage {

	public ProgramIcon() {
		super();
		getStyleClass().add( "xe-icon-old" );
	}

	public double getSize() {
		return getWidth();
	}

}
