package org.ristek.strokeapp;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.graphics.Color;





public class HighScoreScreen extends SimpleBaseGameActivity{

	private final int CAMERA_WIDTH = 800;
	private final int CAMERA_HEIGHT = 480;
	
	private Scene mScene;
	private Camera mCamera;
	private ITexture mFontTexture1;
	private ITexture mFontTexture2;
	private Font mFont1;
	private Font mFont2;
	private Text centerText;
	private Text back;
	private TextureRegion mRoundedRect;
	private Sprite mRect;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		EngineOptions engine = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		engine.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engine;
	}

	
	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		FontFactory.setAssetBasePath("fonts/");
		
		mFontTexture1 = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,  TextureOptions.BILINEAR);
		mFontTexture2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,  TextureOptions.BILINEAR);
		mFont1 = FontFactory.createFromAsset(this.getFontManager(), mFontTexture1, this.getAssets(), "VintageOne.ttf", 32f, true, Color.BLACK);
		mFont2 = FontFactory.createFromAsset(this.getFontManager(), mFontTexture2, this.getAssets(), "VintageOne.ttf", 32f, true, Color.RED);
		this.mFont1.load();
		this.mFont2.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("images/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(mEngine.getTextureManager(),1024,1024);
		mRoundedRect = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this , "rounded_rect.png", 0,0);
		
		mBitmapTextureAtlas.load();
		
		
		
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		float pY = mCamera.getHeight()/2-mRoundedRect.getHeight()/2;
		float pX = mCamera.getWidth()/2-mRoundedRect.getWidth()/2; 
		mScene = new Scene();
		mScene.setBackground(new Background(Color.BLUE, CAMERA_HEIGHT, CAMERA_HEIGHT));
		
		this.centerText = new Text(300, 40, this.mFont1, " High Score ", 
				new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
		this.back = new Text(0, 0, 
				this.mFont2, "BACK",new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager()){
		
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				finish();
				
				return true;
			}
			
			
		};
		
		back.setPosition((mCamera.getWidth()/2)-back.getWidth()/2, 
				mCamera.getHeight()-back.getHeight()*2);
		mRect = new Sprite(pX,pY, mRoundedRect, getVertexBufferObjectManager());
		
		
		mScene.attachChild(centerText);
		mScene.attachChild(back);
		mScene.attachChild(mRect);
		mScene.registerTouchArea(back);
		
		
		return mScene;
	}

	
}
