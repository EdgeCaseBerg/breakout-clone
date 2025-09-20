package spare.peetseater.games.screens;

import com.badlogic.gdx.assets.AssetDescriptor;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;

import java.util.List;

public class LevelScreenBundleParams extends SceneAssetBundleLoader.SceneAssetBundleParam {
    @Override
    public List<AssetDescriptor<?>> getDependencies() {
        this.dependencies.add(GameAssets.PLAYER_PADDLE);
        return this.dependencies;
    }
}
