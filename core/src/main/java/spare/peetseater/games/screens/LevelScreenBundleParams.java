package spare.peetseater.games.screens;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;
import spare.peetseater.games.utilities.TemporaryTexture;
import spare.peetseater.games.utilities.TemporaryTextureLoader;

import java.util.List;

public class LevelScreenBundleParams extends SceneAssetBundleLoader.SceneAssetBundleParam {
    @Override
    public List<AssetDescriptor<?>> getDependencies() {
        this.dependencies.add(GameAssets.PLAYER_PADDLE);
        this.dependencies.add(GameAssets.GREEN_SQUARE);
        this.dependencies.add(GameAssets.BLACK_SQUARE);
        this.dependencies.add(GameAssets.YELLOW_SQUARE);
        return this.dependencies;
    }
}
