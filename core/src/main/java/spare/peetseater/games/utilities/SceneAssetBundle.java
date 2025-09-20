package spare.peetseater.games.utilities;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;

public class SceneAssetBundle {
    private final AssetManager assetManager;

    public SceneAssetBundle(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public <T> T get (AssetDescriptor<T> descriptor) {
        return this.assetManager.get(descriptor);
    }
}
