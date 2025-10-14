package spare.peetseater.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import spare.peetseater.games.screens.LevelScreenBundleParams;
import spare.peetseater.games.screens.transitions.FadeOutBundleParams;
import spare.peetseater.games.screens.transitions.LoadingScreenBundleParams;
import spare.peetseater.games.utilities.SceneAssetBundle;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;
import spare.peetseater.games.utilities.TemporaryTexture;
import spare.peetseater.games.utilities.TemporaryTextureLoader;

import static spare.peetseater.games.GameRunner.GAME_HEIGHT;
import static spare.peetseater.games.GameRunner.GAME_WIDTH;

public class GameAssets {

    public static void configure(AssetManager assetManager) {
        assetManager.setLoader(TemporaryTexture.class, new TemporaryTextureLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(SceneAssetBundle.class, new SceneAssetBundleLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(assetManager.getFileHandleResolver()));
    }

    public static final String PLAYER_ASSET_KEY = "player";
    public static final AssetDescriptor<TemporaryTexture> PLAYER_PADDLE = new AssetDescriptor<>(PLAYER_ASSET_KEY, TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.BLUE));

    public static final String LEVELSCREEN_BUNDLE_KEY = "levelscreen.bundle";
    public static final AssetDescriptor<SceneAssetBundle> LEVEL_SCREEN_BUNDLE = new AssetDescriptor<>(LEVELSCREEN_BUNDLE_KEY, SceneAssetBundle.class, new LevelScreenBundleParams());

    public static final String INITIAL_LOADING_SCREEN_BUNDLE_KEY = "initial-load.bundle";
    public static final AssetDescriptor<SceneAssetBundle> INITIAL_LOADING_SCREEN_BUNDLE = new AssetDescriptor<>(INITIAL_LOADING_SCREEN_BUNDLE_KEY, SceneAssetBundle.class, new SceneAssetBundleLoader.SceneAssetBundleParam());

    public static final AssetDescriptor<TemporaryTexture> BLACK_SQUARE = new AssetDescriptor<>("blacksquare", TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.BLACK));
    public static final AssetDescriptor<TemporaryTexture> RED_SQUARE = new AssetDescriptor<>("redsquare", TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.RED));
    public static final AssetDescriptor<TemporaryTexture> YELLOW_SQUARE = new AssetDescriptor<>("yellowsquare", TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.YELLOW));
    public static final AssetDescriptor<TemporaryTexture> GREEN_SQUARE = new AssetDescriptor<>("greensquare", TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.GREEN));
    public static final AssetDescriptor<TemporaryTexture> ORANGE_SQUARE = new AssetDescriptor<>("orangesquare", TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.ORANGE));

    public static final String FADE_OUT_SCREEN_KEY = "fadeout.bundle";
    public static final AssetDescriptor<SceneAssetBundle> FADE_OUT_BUNDLE = new AssetDescriptor<>(FADE_OUT_SCREEN_KEY, SceneAssetBundle.class, new FadeOutBundleParams());

    public static final String LOADING_SCREEN_BUNDLE_KEY = "loadingscreen.bundle";
    public static final AssetDescriptor<SceneAssetBundle> LOADING_SCREEN_BUNDLE = new AssetDescriptor<>(LOADING_SCREEN_BUNDLE_KEY, SceneAssetBundle.class, new LoadingScreenBundleParams());

    public static final String SCORE_FONT_KEY = "visitor50.ttf";
    public static final AssetDescriptor<BitmapFont> scoreFont = visitorFontDescriptor(50, SCORE_FONT_KEY);

    public static AssetDescriptor<BitmapFont> visitorFontDescriptor(int sizeInPixels, String key) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = "visitor/visitor1.ttf";
        params.fontParameters.size = sizeInPixels;
        return new AssetDescriptor<>(key, BitmapFont.class, params);
    }

    static public BitmapFont getScaledFont(AssetDescriptor<BitmapFont> key, AssetManager assetManager) {
        BitmapFont font = assetManager.get(key);
        font.setUseIntegerPositions(false);
        font.getData().setScale(
            (float) GAME_WIDTH / Gdx.graphics.getWidth(),
            (float) GAME_HEIGHT / Gdx.graphics.getHeight()
        );
        return font;
    }
}
