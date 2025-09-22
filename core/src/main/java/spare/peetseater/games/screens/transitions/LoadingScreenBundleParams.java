package spare.peetseater.games.screens.transitions;

import com.badlogic.gdx.assets.AssetDescriptor;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;

import java.util.List;

public class LoadingScreenBundleParams extends SceneAssetBundleLoader.SceneAssetBundleParam {
    @Override
    public List<AssetDescriptor<?>> getDependencies() {
        // TODO load a font here
        this.dependencies.add(GameAssets.BLACK_SQUARE);
        this.dependencies.add(GameAssets.RED_SQUARE);
        this.dependencies.add(GameAssets.YELLOW_SQUARE);
        this.dependencies.add(GameAssets.GREEN_SQUARE);
        return super.getDependencies();
    }
}
