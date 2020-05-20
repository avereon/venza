package com.avereon.venza.icon;

import com.avereon.venza.image.Images;
import com.avereon.venza.image.RenderedImage;
import com.avereon.venza.javafx.JavaFxStarter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public abstract class RenderedIcon extends RenderedImage {

	public RenderedIcon() {
		super();
		getStyleClass().add( "xe-icon" );
	}

	public double getSize() {
		return getWidth();
	}

	public static void proof( RenderedIcon icon ) {
		JavaFxStarter.startAndWait( 1000 );

		// Now show the icon window
		Platform.runLater( () -> {
			Application.setUserAgentStylesheet( Application.STYLESHEET_MODENA );

			Pane darkPane = proofPane( icon );
			darkPane.getStyleClass().add( "root" );
			darkPane.getStylesheets().add( DARK_THEME );

			Pane lightPane = proofPane( icon );
			lightPane.getStyleClass().add( "root" );
			lightPane.getStylesheets().add( LIGHT_THEME );

			Scene scene = new Scene( new HBox( darkPane, lightPane ) );

			List<Image> stageIcons = new ArrayList<>();
			stageIcons.add( icon.copy().resize( 256 ).getImage() );
			stageIcons.add( icon.copy().resize( 128 ).getImage() );
			stageIcons.add( icon.copy().resize( 64 ).getImage() );
			stageIcons.add( icon.copy().resize( 48 ).getImage() );
			stageIcons.add( icon.copy().resize( 32 ).getImage() );
			stageIcons.add( icon.copy().resize( 16 ).getImage() );

			Stage stage = new Stage();
			stage.setTitle( icon.getClass().getSimpleName() );
			stage.getIcons().addAll( stageIcons );
			stage.setScene( scene );
			// The following line causes the stage not to show on Linux
			//stage.setResizable( false );
			stage.centerOnScreen();
			stage.sizeToScene();
			stage.show();
		} );
	}

	private static Pane proofPane( RenderedIcon icon ) {
		ImageView imageView16 = new ImageView( Images.resample( icon.copy().resize( 16 ).getImage(), 16 ) );
		ImageView imageView32 = new ImageView( Images.resample( icon.copy().resize( 32 ).getImage(), 8 ) );

		RenderedIcon icon128 = icon.copy().resize( 128 );
		AnchorPane.setTopAnchor( icon128, 0.0 );
		AnchorPane.setLeftAnchor( icon128, 0.0 );

		RenderedIcon icon64 = icon.copy().resize( 64 );
		AnchorPane.setTopAnchor( icon64, 128.0 );
		AnchorPane.setLeftAnchor( icon64, 128.0 );

		RenderedIcon icon32 = icon.copy().resize( 32 );
		AnchorPane.setTopAnchor( icon32, 192.0 );
		AnchorPane.setLeftAnchor( icon32, 192.0 );

		RenderedIcon icon16 = icon.copy().resize( 16 );
		AnchorPane.setTopAnchor( icon16, 224.0 );
		AnchorPane.setLeftAnchor( icon16, 224.0 );

		RenderedIcon icon8 = icon.copy().resize( 8 );
		AnchorPane.setTopAnchor( icon8, 240.0 );
		AnchorPane.setLeftAnchor( icon8, 240.0 );

		AnchorPane iconPane = new AnchorPane();
		iconPane.getChildren().addAll( icon128, icon64, icon32, icon16, icon8 );

		GridPane pane = new GridPane();
		pane.add( icon.copy().resize( DEFAULT_SIZE ), 1, 1 );
		pane.add( imageView16, 2, 1 );
		pane.add( imageView32, 2, 2 );
		pane.add( iconPane, 1, 2 );

		return pane;
	}

}
