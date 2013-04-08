package org.ristek.strokeapp;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Typeface;

public class MainActivity extends SimpleBaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_START = 0;
	protected static final int MENU_TIME_TRIAL = 1;
	protected static final int MENU_HIGH_SCORE = 2;
	protected static final int MENU_STROKE_FACT = 3;
	protected static final int MENU_OPTIONS = 4;
	
	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Scene mMenuScene;

	private Font mFont;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final RatioResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(
				CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				resolutionPolicy, mCamera);
	}

	@Override
	protected void onCreateResources() {
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mMenuScene = this.createMenuScene();

		return mMenuScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public Scene createMenuScene() {
		final Scene menuScene = new Scene();
		menuScene.setBackground(new Background(Color.WHITE));

		final VertexBufferObjectManager VBOManager = this
				.getVertexBufferObjectManager();

		
		final Text titleText = new Text(100, 40, this.mFont, "Stroke Game",
				new TextOptions(HorizontalAlign.LEFT), VBOManager);
		
		final String[] menuText = {"Start","Time Trial","High Score","Stroke Fact","Options"};
		
		final Text[] menuItem = new Text[menuText.length];
		
		for (int i = 0; i < menuItem.length; i++) {
			menuItem[i] = new Text(100,100+(i*40),this.mFont,menuText[i],VBOManager){
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					System.out.println(this.getText()+" ditekan!");
					return super
							.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};
			menuScene.attachChild(menuItem[i]);
			menuScene.registerTouchArea(menuItem[i]);
		}
		
		menuScene.attachChild(titleText);
		
		return menuScene;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
