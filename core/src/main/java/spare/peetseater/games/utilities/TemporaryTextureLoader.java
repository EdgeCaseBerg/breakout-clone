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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TemporaryTextureLoader extends AsynchronousAssetLoader<TemporaryTexture, TemporaryTextureLoader.TemporaryTextureParam> {
    public static class TemporaryTextureParam extends AssetLoaderParameters<TemporaryTexture> {
        private final Color color;
        private final int width;
        private final int height;

        public TemporaryTextureParam(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public TemporaryTextureParam(Color color) {
            this(color, 10, 10);
        }
    }

    public TemporaryTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    Pixmap pixmap;
    @Override
    public void loadAsync(AssetManager assetManager, String filename, FileHandle file, TemporaryTextureParam parameter) {
        pixmap = null;
        pixmap = new Pixmap(parameter.width, parameter.height, Pixmap.Format.RGBA8888);
        pixmap.setColor(parameter.color.cpy());
        pixmap.fill();
    }

    @Override
    public TemporaryTexture loadSync(AssetManager manager, String fileName, FileHandle file, TemporaryTextureParam parameter) {
        Pixmap pixmap = this.pixmap;
        Texture tmp = new Texture(pixmap);
        TemporaryTexture texture = new TemporaryTexture(tmp);
        pixmap.dispose();
        this.pixmap = null;
        tmp.dispose();
        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TemporaryTextureParam parameter) {
        return null;
    }
}
