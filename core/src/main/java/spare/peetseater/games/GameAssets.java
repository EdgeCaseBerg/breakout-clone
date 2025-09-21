package spare.peetseater.games;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import spare.peetseater.games.screens.LevelScreenBundleParams;
import spare.peetseater.games.screens.transitions.FadeOutBundleParams;
import spare.peetseater.games.utilities.SceneAssetBundle;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;
import spare.peetseater.games.utilities.TemporaryTexture;
import spare.peetseater.games.utilities.TemporaryTextureLoader;

public class GameAssets {

    public static void configure(AssetManager assetManager) {
        assetManager.setLoader(TemporaryTexture.class, new TemporaryTextureLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(SceneAssetBundle.class, new SceneAssetBundleLoader(assetManager.getFileHandleResolver()));
    }

    public static final String PLAYER_ASSET_KEY = "player";
    public static final AssetDescriptor<TemporaryTexture> PLAYER_PADDLE = new AssetDescriptor<>(PLAYER_ASSET_KEY, TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.BLUE));

    public static final String LEVELSCREEN_BUNDLE_KEY = "levelscreen.bundle";
    public static final AssetDescriptor<SceneAssetBundle> LEVEL_SCREEN_BUNDLE = new AssetDescriptor<>(LEVELSCREEN_BUNDLE_KEY, SceneAssetBundle.class, new LevelScreenBundleParams());

    public static final String INITIAL_LOADING_SCREEN_BUNDLE_KEY = "initial-load.bundle";
    public static final AssetDescriptor<SceneAssetBundle> INITIAL_LOADING_SCREEN_BUNDLE = new AssetDescriptor<>(INITIAL_LOADING_SCREEN_BUNDLE_KEY, SceneAssetBundle.class, new SceneAssetBundleLoader.SceneAssetBundleParam());

    public static final AssetDescriptor<TemporaryTexture> BLACK_SQUARE = new AssetDescriptor<>("blacksquare", TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.BLACK));

    public static final String FADE_OUT_SCREEN_KEY = "fadeout.bundle";
    public static final AssetDescriptor<SceneAssetBundle> FADE_OUT_BUNDLE = new AssetDescriptor<>(FADE_OUT_SCREEN_KEY, SceneAssetBundle.class, new FadeOutBundleParams());

}
