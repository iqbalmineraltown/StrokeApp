package org.ristek.strokeapp;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.FontFactory;





public class HighScoreScreen extends BaseStrokeClinicActivity{
	
	private final static String[] textureName = {"hiscore-bg"};
	
	private Scene mScene;
//	private ITexture mFontTexture1;
//	private ITexture mFontTexture2;
//	private Font mFont1;
//	private Font mFont2;
//	private Text centerText;
//	private Text back;
	//private TextureRegion mRoundedRect;
//	private Sprite mRect;

	
	@Override
	protected void onCreateResources() {
		FontFactory.setAssetBasePath("fonts/");
		
		
		
//		mFontTexture1 = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,  TextureOptions.BILINEAR);
//		mFontTexture2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,  TextureOptions.BILINEAR);
//		mFont1 = FontFactory.createFromAsset(this.getFontManager(), mFontTexture1, this.getAssets(), "VintageOne.ttf", 32f, true, Color.BLACK);
//		mFont2 = FontFactory.createFromAsset(this.getFontManager(), mFontTexture2, this.getAssets(), "VintageOne.ttf", 32f, true, Color.RED);
//		this.mFont1.load();
//		this.mFont2.load();
		
		this.createTextureAtlas(1024, 1024);
		this.loadTexture(textureName);
		this.buildTextureAtlas();
		
		
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		//float pY = mCamera.getHeight()/2-mRoundedRect.getHeight()/2;
		//float pX = mCamera.getWidth()/2-mRoundedRect.getWidth()/2; 
		mScene = new Scene();
		mScene.setBackground(new SpriteBackground(new Sprite(0, 0,
				getTR("hiscore-bg"), getVertexBufferObjectManager())));
		
	//	this.centerText = new Text(300, 40, this.mFont1, " High Score ", 
		//		new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
		//this.back = new Text(0, 0, 
		//		this.mFont2, "BACK",new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager()){
//		
//			@Override
//			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
//					float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				finish();
//				
//				return true;
//			}
//			
//			
//		};
		
		//back.setPosition((mCamera.getWidth()/2)-back.getWidth()/2, 
			//	mCamera.getHeight()-back.getHeight()*2);
		//mRect = new Sprite(pX,pY, mRoundedRect, getVertexBufferObjectManager());
		
		
		//mScene.attachChild(centerText);
		//mScene.attachChild(back);
		//mScene.attachChild(mRect);
		//mScene.registerTouchArea(back);
		
		
		return mScene;
	}

	
}
