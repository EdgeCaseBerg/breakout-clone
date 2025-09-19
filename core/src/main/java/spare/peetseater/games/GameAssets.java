package spare.peetseater.games;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import spare.peetseater.games.utilities.TemporaryTexture;
import spare.peetseater.games.utilities.TemporaryTextureLoader;

public class GameAssets {
    private final AssetManager assetManager;

    public GameAssets(AssetManager assetManager) {
        this.assetManager = assetManager;

        this.assetManager.setLoader(TemporaryTexture.class, new TemporaryTextureLoader(assetManager.getFileHandleResolver()));
    }

    public static final String PLAYER_ASSET_KEY = "player";
    public static final AssetDescriptor<TemporaryTexture> PLAYER_PADDLE = new AssetDescriptor<>(PLAYER_ASSET_KEY, TemporaryTexture.class, new TemporaryTextureLoader.TemporaryTextureParam(Color.BLUE));

}
