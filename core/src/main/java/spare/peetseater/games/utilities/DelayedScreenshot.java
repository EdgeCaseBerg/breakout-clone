package spare.peetseater.games.utilities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import spare.peetseater.games.screens.Scene;

public class DelayedScreenshot {
    private final SpriteBatch batch;
    private final Scene scene;
    Texture screenshot;

    public DelayedScreenshot(SpriteBatch batch, Scene scene) {
        this.batch = batch;
        this.scene = scene;
        this.screenshot = null;
    }

    public Texture screenshot() {
        if (screenshot != null) {
            return screenshot;
        }

        FrameBuffer frameBuffer = new FrameBuffer(
            Pixmap.Format.RGBA8888,
            1280,
            800,
            false
        );
        batch.end();
        frameBuffer.begin();
        batch.begin();
        scene.render(0);
        batch.end();
        frameBuffer.end();
        batch.begin();

        screenshot = frameBuffer.getColorBufferTexture();
        return screenshot;
    }

    public void dispose() {
        screenshot.dispose();
    }
}
