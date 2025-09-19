package spare.peetseater.games.utilities;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class TemporaryTextureLoader extends AsynchronousAssetLoader<TemporaryTexture, TemporaryTextureLoader.TemporaryTextureParam> {
    public static class TemporaryTextureParam extends AssetLoaderParameters<TemporaryTexture> {
        private final Color color;
        public TemporaryTextureParam(Color color) {
            this.color = color;
        }
    }


    public TemporaryTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String filename, FileHandle file, TemporaryTextureParam parameter) {
    }

    @Override
    public TemporaryTexture loadSync(AssetManager manager, String fileName, FileHandle file, TemporaryTextureParam parameter) {
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(parameter.color.cpy());
        pixmap.fill();
        Texture tmp = new Texture(pixmap);
        TemporaryTexture texture = new TemporaryTexture(tmp);
        pixmap.dispose();
        tmp.dispose();
        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TemporaryTextureParam parameter) {
        return null;
    }
}
