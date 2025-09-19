package spare.peetseater.games.utilities;

import com.badlogic.gdx.graphics.Texture;

public class TemporaryTexture extends Texture {
    public TemporaryTexture(Texture texture) {
        super(texture.getTextureData());
    }
}
