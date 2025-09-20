package spare.peetseater.games.utilities;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class SceneAssetBundleLoader extends AsynchronousAssetLoader<SceneAssetBundle, SceneAssetBundleLoader.SceneAssetBundleParam> {
    public static class SceneAssetBundleParam extends AssetLoaderParameters<SceneAssetBundle> {

        protected final List<AssetDescriptor<?>> dependencies;

        public SceneAssetBundleParam(List<AssetDescriptor<?>> dependencies) {
            this.dependencies = dependencies;
        }

        public SceneAssetBundleParam() {
            this(new ArrayList<>());
        }

        public List<AssetDescriptor<?>> getDependencies() {
            return dependencies;
        }
    }

    public SceneAssetBundleLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    SceneAssetBundle bundle;
    @Override
    public void loadAsync(AssetManager assetManager, String filename, FileHandle file, SceneAssetBundleParam parameter) {
        bundle = new SceneAssetBundle(assetManager);
    }

    @Override
    public SceneAssetBundle loadSync(AssetManager manager, String fileName, FileHandle file, SceneAssetBundleParam parameter) {
        SceneAssetBundle tmp;
        tmp = bundle;
        this.bundle = null;
        return tmp;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SceneAssetBundleParam parameter) {
        if (parameter == null) {
            return new Array<>();
        }

        Array<AssetDescriptor> array = new Array<>();
        for (AssetDescriptor a : parameter.getDependencies()) {
            array.add(a);
        }
        return array;
    }
}
