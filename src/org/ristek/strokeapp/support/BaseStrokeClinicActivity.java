package org.ristek.strokeapp.support;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import java.util.HashMap;

/**
 * Membuat semua yang memakai andengine menjadi lebih simpel
 *
 * @author Muhammad Febrian Ramadhana
 */
public abstract class BaseStrokeClinicActivity extends SimpleBaseGameActivity {

    protected final int CAMERA_WIDTH = 800;
    protected final int CAMERA_HEIGHT = 480;

    private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
    private HashMap<String, ITextureRegion> mTextureRegion;

    protected Camera mCamera;

    public BaseStrokeClinicActivity() {
        super();
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        final FillResolutionPolicy resolutionPolicy = new FillResolutionPolicy();

        EngineOptions engineOption = new EngineOptions(true,
                ScreenOrientation.LANDSCAPE_FIXED, resolutionPolicy, mCamera);
        engineOption.getAudioOptions().setNeedsMusic(true);

        return engineOption;
    }

    public void createTextureAtlas(int width, int height) {
        this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
                this.getTextureManager(), width, height,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mTextureRegion = new HashMap<String, ITextureRegion>();
    }

    public void loadTexture(String... textureNames) {
        for (String textureName : textureNames) {
            TextureRegion temp = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(this.mBitmapTextureAtlas, this, "images/"
                            + textureName + ".png");
            mTextureRegion.put(textureName, temp);
        }
    }

    public void loadTiledTexture(int row, int column, String... textureNames) {
        for (String textureName : textureNames) {
            TiledTextureRegion temp = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mBitmapTextureAtlas, this,
                            "images/" + textureName + ".png", column, row);
            mTextureRegion.put(textureName, temp);
        }
    }

    public void buildTextureAtlas() {
        try {
            this.mBitmapTextureAtlas
                    .build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
                            0, 0, 1));
            this.mBitmapTextureAtlas.load();
        } catch (TextureAtlasBuilderException e) {
            Debug.e(e);
        }
    }

    public ITextureRegion getTR(String name) {
        return mTextureRegion.get(name);
    }
}
