package spare.peetseater.games.screens.transitions;

import com.badlogic.gdx.assets.AssetDescriptor;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;

import java.util.List;

public class FadeOutBundleParams extends SceneAssetBundleLoader.SceneAssetBundleParam {
    @Override
    public List<AssetDescriptor<?>> getDependencies() {
        this.dependencies.add(GameAssets.BLACK_SQUARE);
        return this.dependencies;
    }
}
